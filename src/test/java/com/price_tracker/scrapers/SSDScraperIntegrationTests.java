package com.price_tracker.scrapers;

import com.price_tracker.domain.dto.hybrid_dtos.SSDDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.SSDPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.SSDDTO;
import com.price_tracker.domain.entities.price_point_entities.SSDPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.SSDPricePointJDBCTemplate;
import com.price_tracker.services.price_point_services.SSDPricePointService;
import com.price_tracker.services.product_services.SSDService;
import com.price_tracker.testing_data.RestPage;
import com.price_tracker.testing_data.ssd_data.SSDTestingUtility;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartSSDScrapingService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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
import static com.price_tracker.testing_data.ssd_data.SSDTestingData.TESTING_SSD_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_CRUCIAL_BX500_1TB;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SSDScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final SSDTestingUtility ssdTestingUtility;
    private final UmartSSDScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final SSDPricePointJDBCTemplate ssdPricePointJDBCTemplate;
    private final SSDService ssdService;
    private final GenericMapper<SSDPricePoint, SSDPricePointDTO> ssdPricePointMapper;
    private final SSDPricePointService ssdPricePointService;

    @Autowired
    public SSDScraperIntegrationTests(MockMvc mockMVC,
                                      SSDTestingUtility ssdTestingUtility,
                                      UmartSSDScrapingService scraper,
                                      ObjectMapper objectMapper,
                                      MapperFactory mapperFactory,
                                      SSDPricePointJDBCTemplate ssdPricePointJDBCTemplate,
                                      SSDService ssdService,
                                      SSDPricePointService ssdPricePointService) {
        this.mockMVC = mockMVC;
        this.ssdTestingUtility = ssdTestingUtility;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.ssdPricePointJDBCTemplate = ssdPricePointJDBCTemplate;
        this.ssdService = ssdService;
        this.ssdPricePointMapper = mapperFactory.create(SSDPricePoint.class, SSDPricePointDTO.class);
        this.ssdPricePointService = ssdPricePointService;
    }

    @Test
    @Disabled
    public void testThatUmartSSDScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_CRUCIAL_BX500_1TB);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_SSD_MODEL_NUMBER);
    }

    @Test
    public void testThatSSDPricePointInsertionWithJDBCTemplateReturnsHttpStatus200Ok() throws Exception {
        List<SSDPricePoint> returnList = Stream.generate(() ->
                        scraper.createSSDPricePoint(ssdTestingUtility.createSampleSSDPricePointData()))
                .limit(10)
                .toList();

        ssdPricePointJDBCTemplate.batchInsertPricePoints(returnList);

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
                        scraper.createSSDPricePoint(ssdTestingUtility.createSampleSSDPricePointData()))
                .limit(insertionCount)
                .toList();

        ssdPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/api/ssd_pricepoints")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        RestPage<SSDPricePointDTO> actualPage = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {}
        );
        List<SSDPricePointDTO> actualList = actualPage.getContent();

        List<Long> expectedIds = returnList.stream()
                .map(SSDPricePoint::getId)
                .toList();

        assertThat(actualList)
                .hasSize(returnList.size())
                .extracting(SSDPricePointDTO::getId)
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
                        scraper.createSSDPricePoint(ssdTestingUtility.createSampleSSDPricePointData()))
                .limit(10)
                .toList();

        ssdPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

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
                        scraper.createSSDPricePoint(ssdTestingUtility.createSampleSSDPricePointData()))
                .limit(10)
                .toList()
                .reversed();

        ssdPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        List<SSDPricePointDTO> pricePointDTOS = sampleList.stream()
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
                        scraper.createSSDPricePoint(ssdTestingUtility.createSampleSSDPricePointData()))
                .limit(10)
                .toList();

        ssdPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        Optional<SSDDataAndPricePointDTO> returnList = ssdPricePointService
                .findByModelNumber(savedSSD.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getSsdDTO().equals(savedSSD));
    }
}