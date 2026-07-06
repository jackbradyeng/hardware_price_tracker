package com.price_tracker.scrapers.integration_tests;

import com.price_tracker.domain.dto.hybrid_dtos.NVMEDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.NVMEDTO;
import com.price_tracker.domain.entities.price_point_entities.NVMEPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.GenericPricePointJdbcTemplate;
import com.price_tracker.services.price_point_services.NVMEPricePointService;
import com.price_tracker.services.product_services.NVMEService;
import com.price_tracker.testing_data.RestPage;
import com.price_tracker.testing_data.nvme_data.NVMETestingUtility;
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
public class NVMEScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final NVMETestingUtility nvmeTestingUtility;
    private final GenericScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final GenericPricePointJdbcTemplate<NVMEPricePoint> nvmeGenericPricePointJDBCTemplate;
    private final NVMEService nvmeService;
    private final GenericMapper<NVMEPricePoint, GenericPricePointDTO> nvmePricePointMapper;
    private final NVMEPricePointService nvmePricePointService;

    @Autowired
    public NVMEScraperIntegrationTests(MockMvc mockMVC,
                                       NVMETestingUtility nvmeTestingUtility,
                                       GenericScrapingService scraper,
                                       ObjectMapper objectMapper,
                                       MapperFactory mapperFactory,
                                       GenericPricePointJdbcTemplate<NVMEPricePoint> nvmeGenericPricePointJDBCTemplate,
                                       NVMEService nvmeService,
                                       NVMEPricePointService nvmePricePointService) {
        this.mockMVC = mockMVC;
        this.nvmeTestingUtility = nvmeTestingUtility;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.nvmeGenericPricePointJDBCTemplate = nvmeGenericPricePointJDBCTemplate;
        this.nvmeService = nvmeService;
        this.nvmePricePointMapper = mapperFactory.create(NVMEPricePoint.class, GenericPricePointDTO.class);
        this.nvmePricePointService = nvmePricePointService;
    }

    @Test
    public void testThatNVMEPricePointInsertionWithJDBCTemplateReturnsHttpStatus200Ok() throws Exception {
        List<NVMEPricePoint> returnList = Stream.generate(() ->
                        nvmePricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                nvmeTestingUtility.createSampleNVMEPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        nvmeGenericPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/nvme_pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    public void testThatNVMEPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(int insertionCount)
            throws Exception {
        List<NVMEPricePoint> returnList = Stream.generate(() ->
                        nvmePricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                nvmeTestingUtility.createSampleNVMEPricePointData(), UMART, AUD)))
                .limit(insertionCount)
                .toList();

        nvmeGenericPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/api/nvme_pricepoints")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        RestPage<GenericPricePointDTO> actualPage = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {}
        );
        List<GenericPricePointDTO> actualList = actualPage.getContent();

        List<Long> expectedIds = returnList.stream()
                .map(NVMEPricePoint::getId)
                .toList();

        assertThat(actualList)
                .hasSize(returnList.size())
                .extracting(GenericPricePointDTO::getId)
                .containsExactlyElementsOf(expectedIds);
    }

    @Test
    public void testThatNVMEPricePointInsertionReturnsExpectedNumberOfObjectsAndIDs() throws Exception {
        testThatNVMEPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(10);
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatus200Ok() throws Exception {

        NVMEDTO savedNVME = nvmeService.save(nvmeTestingUtility.createTestNVME());

        List<NVMEPricePoint> sampleList = Stream.generate(() ->
                        nvmePricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                nvmeTestingUtility.createSampleNVMEPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        nvmeGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/nvme_pricepoints/" + savedNVME.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatusWithRandomModelNumber() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/nvme_pricepoints/" + "random_model_number")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedPricePoints() {

        NVMEDTO savedNVME = nvmeService.save(nvmeTestingUtility.createTestNVME());

        List<NVMEPricePoint> sampleList = Stream.generate(() ->
                        nvmePricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                nvmeTestingUtility.createSampleNVMEPricePointData(), UMART, AUD)))
                .limit(10)
                .toList()
                .reversed();

        nvmeGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        List<GenericPricePointDTO> pricePointDTOS = sampleList.stream()
                .map(nvmePricePointMapper::mapTo)
                .toList();

        Optional<NVMEDataAndPricePointDTO> returnList = nvmePricePointService
                .findByModelNumber(savedNVME.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getNvmePricePointDTOList())
                .hasSize(10)
                .containsExactlyElementsOf(pricePointDTOS);
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedNVMEData() {

        NVMEDTO savedNVME = nvmeService.save(nvmeTestingUtility.createTestNVME());

        List<NVMEPricePoint> sampleList = Stream.generate(() ->
                        nvmePricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                nvmeTestingUtility.createSampleNVMEPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        nvmeGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        Optional<NVMEDataAndPricePointDTO> returnList = nvmePricePointService
                .findByModelNumber(savedNVME.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getNvmeDTO().equals(savedNVME));
    }
}