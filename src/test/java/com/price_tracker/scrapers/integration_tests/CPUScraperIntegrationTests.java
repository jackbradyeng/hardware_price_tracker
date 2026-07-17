package com.price_tracker.scrapers.integration_tests;

import com.price_tracker.domain.dto.hybrid_dtos.CPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.domain.entities.price_point_entities.CPUPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.GenericPricePointJdbcTemplate;
import com.price_tracker.services.price_point_services.GenericPricePointService;
import com.price_tracker.services.product_services.GenericProductService;
import com.price_tracker.testing_data.RestPage;
import com.price_tracker.testing_data.cpu_data.CPUTestingUtility;
import com.price_tracker.webscraper.product_services.GenericScrapingService;
import jakarta.transaction.Transactional;
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
import static com.price_tracker.constants.other_constants.CurrencyConstants.AUD;
import static com.price_tracker.constants.vendor_constants.VendorNames.UMART;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CPUScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final CPUTestingUtility cpuTestingUtility;
    private final GenericScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final GenericPricePointJdbcTemplate<CPUPricePoint> cpuGenericPricePointJDBCTemplate;
    private final GenericProductService<CPUDTO> cpuService;
    private final GenericMapper<CPUPricePoint, GenericPricePointDTO> cpuPricePointMapper;
    private final GenericPricePointService<CPUDataAndPricePointDTO> cpuPricePointService;

    @Autowired
    public CPUScraperIntegrationTests(MockMvc mockMVC,
                                      CPUTestingUtility cpuTestingUtility,
                                      GenericScrapingService scraper,
                                      ObjectMapper objectMapper,
                                      MapperFactory mapperFactory,
                                      GenericPricePointJdbcTemplate<CPUPricePoint> cpuGenericPricePointJDBCTemplate,
                                      GenericProductService<CPUDTO> cpuService,
                                      GenericPricePointService<CPUDataAndPricePointDTO> cpuPricePointService) {
        this.mockMVC = mockMVC;
        this.cpuTestingUtility = cpuTestingUtility;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.cpuGenericPricePointJDBCTemplate = cpuGenericPricePointJDBCTemplate;
        this.cpuService = cpuService;
        this.cpuPricePointMapper = mapperFactory.create(CPUPricePoint.class, GenericPricePointDTO.class);
        this.cpuPricePointService = cpuPricePointService;
    }

    @Test
    public void testThatCPUPricePointInsertionWithJDBCTemplateReturnsOK() throws Exception {
        List<CPUPricePoint> returnList = Stream.generate(() ->
                        cpuPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                cpuTestingUtility.createSampleCPUPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        cpuGenericPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/v1/cpu_pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    public void testThatCPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(int insertionCount)
            throws Exception {
        List<CPUPricePoint> returnList = Stream.generate(() ->
                        cpuPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                cpuTestingUtility.createSampleCPUPricePointData(), UMART, AUD)))
                .limit(insertionCount)
                .toList();

        cpuGenericPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/api/v1/cpu_pricepoints")
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
    public void testThatFindByModelNumberReturnsHttpStatus200Ok() throws Exception {

        CPUDTO savedCPU = cpuService.save(cpuTestingUtility.createTestCPU());

        List<CPUPricePoint> sampleList = Stream.generate(() ->
                        cpuPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                cpuTestingUtility.createSampleCPUPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        cpuGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/v1/cpu_pricepoints/" + savedCPU.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatusWithRandomModelNumber() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/v1/cpu_pricepoints/" + "random_model_number")
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
                        cpuPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                cpuTestingUtility.createSampleCPUPricePointData(), UMART, AUD)))
                .limit(10)
                .toList()
                .reversed();

        cpuGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

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
                        cpuPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                cpuTestingUtility.createSampleCPUPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        cpuGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // next we query by the CPU's model number - this should return a collection of composite DTOs
        Optional<CPUDataAndPricePointDTO> returnList = cpuPricePointService
                .findByModelNumber(savedCPU.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getCpuDTO().equals(savedCPU));
    }
}