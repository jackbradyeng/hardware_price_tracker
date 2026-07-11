package com.price_tracker.scrapers.integration_tests;

import com.price_tracker.domain.dto.hybrid_dtos.SSDDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.SSDDTO;
import com.price_tracker.domain.entities.price_point_entities.SSDPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.GenericPricePointJdbcTemplate;
import com.price_tracker.services.price_point_services.GenericPricePointService;
import com.price_tracker.services.product_services.GenericProductService;
import com.price_tracker.testing_data.RestPage;
import com.price_tracker.testing_data.ssd_data.SSDTestingUtility;
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
public class SSDScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final SSDTestingUtility ssdTestingUtility;
    private final GenericScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final GenericPricePointJdbcTemplate<SSDPricePoint> ssdGenericPricePointJDBCTemplate;
    private final GenericProductService<SSDDTO> ssdService;
    private final GenericMapper<SSDPricePoint, GenericPricePointDTO> ssdPricePointMapper;
    private final GenericPricePointService<SSDDataAndPricePointDTO> ssdPricePointService;

    @Autowired
    public SSDScraperIntegrationTests(MockMvc mockMVC,
                                      SSDTestingUtility ssdTestingUtility,
                                      GenericScrapingService scraper,
                                      ObjectMapper objectMapper,
                                      MapperFactory mapperFactory,
                                      GenericPricePointJdbcTemplate<SSDPricePoint> ssdGenericPricePointJDBCTemplate,
                                      GenericProductService<SSDDTO> ssdService,
                                      GenericPricePointService<SSDDataAndPricePointDTO> ssdPricePointService) {
        this.mockMVC = mockMVC;
        this.ssdTestingUtility = ssdTestingUtility;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.ssdGenericPricePointJDBCTemplate = ssdGenericPricePointJDBCTemplate;
        this.ssdService = ssdService;
        this.ssdPricePointMapper = mapperFactory.create(SSDPricePoint.class, GenericPricePointDTO.class);
        this.ssdPricePointService = ssdPricePointService;
    }

    @Test
    public void testThatSSDPricePointInsertionWithJDBCTemplateReturnsHttpStatus200Ok() throws Exception {
        List<SSDPricePoint> returnList = Stream.generate(() ->
                        ssdPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                ssdTestingUtility.createSampleSSDPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        ssdGenericPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/ssd_pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    public void testThatSSDPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(int insertionCount)
            throws Exception {
        List<SSDPricePoint> returnList = Stream.generate(() ->
                        ssdPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                ssdTestingUtility.createSampleSSDPricePointData(), UMART, AUD)))
                .limit(insertionCount)
                .toList();

        ssdGenericPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/api/ssd_pricepoints")
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
                .map(SSDPricePoint::getId)
                .toList();

        assertThat(actualList)
                .hasSize(returnList.size())
                .extracting(GenericPricePointDTO::getId)
                .containsExactlyElementsOf(expectedIds);
    }

    @Test
    public void testThatSSDPricePointInsertionReturnsExpectedNumberOfObjectsAndIDs() throws Exception {
        testThatSSDPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(10);
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatus200Ok() throws Exception {

        SSDDTO savedSSD = ssdService.save(ssdTestingUtility.createTestSSD());

        List<SSDPricePoint> sampleList = Stream.generate(() ->
                        ssdPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                ssdTestingUtility.createSampleSSDPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        ssdGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/ssd_pricepoints/" + savedSSD.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatusWithRandomModelNumber() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/ssd_pricepoints/" + "random_model_number")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedPricePoints() {

        SSDDTO savedSSD = ssdService.save(ssdTestingUtility.createTestSSD());

        List<SSDPricePoint> sampleList = Stream.generate(() ->
                        ssdPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                ssdTestingUtility.createSampleSSDPricePointData(), UMART, AUD)))
                .limit(10)
                .toList()
                .reversed();

        ssdGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        List<GenericPricePointDTO> pricePointDTOS = sampleList.stream()
                .map(ssdPricePointMapper::mapTo)
                .toList();

        Optional<SSDDataAndPricePointDTO> returnList = ssdPricePointService
                .findByModelNumber(savedSSD.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getSsdPricePointDTOList())
                .hasSize(10)
                .containsExactlyElementsOf(pricePointDTOS);
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedSSDData() {

        SSDDTO savedSSD = ssdService.save(ssdTestingUtility.createTestSSD());

        List<SSDPricePoint> sampleList = Stream.generate(() ->
                        ssdPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                ssdTestingUtility.createSampleSSDPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        ssdGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        Optional<SSDDataAndPricePointDTO> returnList = ssdPricePointService
                .findByModelNumber(savedSSD.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getSsdDTO().equals(savedSSD));
    }
}