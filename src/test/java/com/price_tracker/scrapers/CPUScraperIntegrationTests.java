package com.price_tracker.scrapers;

import com.price_tracker.domain.dto.hybrid_dtos.CPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.domain.entities.price_point_entities.CPUPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.CPUPricePointJDBCTemplate;
import com.price_tracker.services.price_point_services.CPUPricePointService;
import com.price_tracker.services.product_services.CPUService;
import com.price_tracker.testing_data.RestPage;
import com.price_tracker.testing_data.cpu_data.CPUTestingUtility;
import com.price_tracker.webscraper.product_services.impl.UmartCPUScrapingService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CPUScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final CPUTestingUtility cpuTestingUtility;
    private final UmartCPUScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final CPUPricePointJDBCTemplate cpuPricePointJDBCTemplate;
    private final CPUService cpuService;
    private final GenericMapper<CPUPricePoint, GenericPricePointDTO> cpuPricePointMapper;
    private final CPUPricePointService cpuPricePointService;

    @Autowired
    public CPUScraperIntegrationTests(MockMvc mockMVC,
                                      CPUTestingUtility cpuTestingUtility,
                                      UmartCPUScrapingService scraper,
                                      ObjectMapper objectMapper,
                                      MapperFactory mapperFactory,
                                      CPUPricePointJDBCTemplate cpuPricePointJDBCTemplate,
                                      CPUService cpuService,
                                      CPUPricePointService cpuPricePointService) {
        this.mockMVC = mockMVC;
        this.cpuTestingUtility = cpuTestingUtility;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.cpuPricePointJDBCTemplate = cpuPricePointJDBCTemplate;
        this.cpuService = cpuService;
        this.cpuPricePointMapper = mapperFactory.create(CPUPricePoint.class, GenericPricePointDTO.class);
        this.cpuPricePointService = cpuPricePointService;
    }

    @Test
    public void testThatCPUPricePointInsertionWithJDBCTemplateReturnsOK() throws Exception {
        List<CPUPricePoint> returnList = Stream.generate(() ->
                        scraper.createCPUPricePoint(cpuTestingUtility.createSampleCPUPricePointData()))
                .limit(10)
                .toList();

        cpuPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/cpu_pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    public void testThatCPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(int insertionCount)
            throws Exception {
        List<CPUPricePoint> returnList = Stream.generate(() ->
                        scraper.createCPUPricePoint(cpuTestingUtility.createSampleCPUPricePointData()))
                .limit(insertionCount)
                .toList();

        cpuPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/api/cpu_pricepoints")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // de-serialize the return object so that it's size and contents can be tested
        String contentAsString = result.getResponse().getContentAsString();
        RestPage<GenericPricePointDTO> actualPage = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {}
        );
        List<GenericPricePointDTO> actualList = actualPage.getContent();

        // store the sequence of expected IDs
        List<Long> expectedIds = returnList.stream()
                .map(CPUPricePoint::getId)
                .toList();

        // ensure that the return instance has the expected size and corresponding IDs
        assertThat(actualList)
                .hasSize(returnList.size())
                .extracting(GenericPricePointDTO::getId)
                .containsExactlyElementsOf(expectedIds);
    }

    @Test
    public void testThatCPUPricePointInsertionReturnsExpectedNumberOfObjectsAndIDs() throws Exception {

        testThatCPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(10);
    }

    @Test
    @Disabled
    public void testThatCPUPricePointInsertionReturnsExpectedNumberAfterMultipleInsertions() throws Exception {

        // 110 price points -> three round-trips or three insertions
        testThatCPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(110);
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatus200Ok() throws Exception {

        CPUDTO savedCPU = cpuService.save(cpuTestingUtility.createTestCPU());

        List<CPUPricePoint> sampleList = Stream.generate(() ->
                        scraper.createCPUPricePoint(cpuTestingUtility.createSampleCPUPricePointData()))
                .limit(10)
                .toList();

        cpuPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/cpu_pricepoints/" + savedCPU.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatusWithRandomModelNumber() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/cpu_pricepoints/" + "random_model_number")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedPricePoints() {

        // first we save the CPU to the DB
        CPUDTO savedCPU = cpuService.save(cpuTestingUtility.createTestCPU());

        // next we generate and save a collection of price points with the same model number to the DB
        List<CPUPricePoint> sampleList = Stream.generate(() ->
                        scraper.createCPUPricePoint(cpuTestingUtility.createSampleCPUPricePointData()))
                .limit(10)
                .toList()
                .reversed();

        cpuPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // convert price points to DTO for comparison's sake
        List<GenericPricePointDTO> pricePointDTOS = sampleList.stream()
                .map(cpuPricePointMapper::mapTo)
                .toList();

        // next we query by the CPU's model number - this should return a collection of composite DTOs
        Optional<CPUDataAndPricePointDTO> returnList = cpuPricePointService
                .findByModelNumber(savedCPU.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getCpuPricePointDTOList())
                .hasSize(10)
                .containsExactlyElementsOf(pricePointDTOS);
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedCPUData() {

        // first we save the CPU to the DB
        CPUDTO savedCPU = cpuService.save(cpuTestingUtility.createTestCPU());

        // next we generate and save a collection of price points with the same model number to the DB
        List<CPUPricePoint> sampleList = Stream.generate(() ->
                        scraper.createCPUPricePoint(cpuTestingUtility.createSampleCPUPricePointData()))
                .limit(10)
                .toList();

        cpuPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // next we query by the CPU's model number - this should return a collection of composite DTOs
        Optional<CPUDataAndPricePointDTO> returnList = cpuPricePointService
                .findByModelNumber(savedCPU.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getCpuDTO().equals(savedCPU));
    }
}
