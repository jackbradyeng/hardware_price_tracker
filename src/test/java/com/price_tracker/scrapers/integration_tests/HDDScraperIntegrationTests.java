package com.price_tracker.scrapers.integration_tests;

import com.price_tracker.domain.dto.hybrid_dtos.HDDDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.HDDDTO;
import com.price_tracker.domain.entities.price_point_entities.HDDPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.HDDPricePointJDBCTemplate;
import com.price_tracker.services.price_point_services.HDDPricePointService;
import com.price_tracker.services.product_services.HDDService;
import com.price_tracker.testing_data.RestPage;
import com.price_tracker.testing_data.hdd_data.HDDTestingUtility;
import com.price_tracker.webscraper.product_services.impl.VendorProductScrapingService;
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
public class HDDScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final HDDTestingUtility hddTestingUtility;
    private final VendorProductScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final HDDPricePointJDBCTemplate hddPricePointJDBCTemplate;
    private final HDDService hddService;
    private final GenericMapper<HDDPricePoint, GenericPricePointDTO> hddPricePointMapper;
    private final HDDPricePointService hddPricePointService;

    @Autowired
    public HDDScraperIntegrationTests(MockMvc mockMVC,
                                      HDDTestingUtility hddTestingUtility,
                                      VendorProductScrapingService scraper,
                                      ObjectMapper objectMapper,
                                      MapperFactory mapperFactory,
                                      HDDPricePointJDBCTemplate hddPricePointJDBCTemplate,
                                      HDDService hddService,
                                      HDDPricePointService hddPricePointService) {
        this.mockMVC = mockMVC;
        this.hddTestingUtility = hddTestingUtility;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.hddPricePointJDBCTemplate = hddPricePointJDBCTemplate;
        this.hddService = hddService;
        this.hddPricePointMapper = mapperFactory.create(HDDPricePoint.class, GenericPricePointDTO.class);
        this.hddPricePointService = hddPricePointService;
    }

    @Test
    public void testThatHDDPricePointInsertionWithJDBCTemplateReturnsHttpStatus200Ok() throws Exception {
        List<HDDPricePoint> returnList = Stream.generate(() ->
                        hddPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                hddTestingUtility.createSampleHDDPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        hddPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/hdd_pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    public void testThatHDDPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(int insertionCount)
            throws Exception {
        List<HDDPricePoint> returnList = Stream.generate(() ->
                        hddPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                hddTestingUtility.createSampleHDDPricePointData(), UMART, AUD)))
                .limit(insertionCount)
                .toList();

        hddPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/api/hdd_pricepoints")
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
                .map(HDDPricePoint::getId)
                .toList();

        assertThat(actualList)
                .hasSize(returnList.size())
                .extracting(GenericPricePointDTO::getId)
                .containsExactlyElementsOf(expectedIds);
    }

    @Test
    public void testThatHDDPricePointInsertionReturnsExpectedNumberOfObjectsAndIDs() throws Exception {
        testThatHDDPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(10);
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatus200Ok() throws Exception {

        HDDDTO savedHDD = hddService.save(hddTestingUtility.createTestHDD());

        List<HDDPricePoint> sampleList = Stream.generate(() ->
                        hddPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                hddTestingUtility.createSampleHDDPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        hddPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/hdd_pricepoints/" + savedHDD.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatusWithRandomModelNumber() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/hdd_pricepoints/" + "random_model_number")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedPricePoints() {

        HDDDTO savedHDD = hddService.save(hddTestingUtility.createTestHDD());

        List<HDDPricePoint> sampleList = Stream.generate(() ->
                        hddPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                hddTestingUtility.createSampleHDDPricePointData(), UMART, AUD)))
                .limit(10)
                .toList()
                .reversed();

        hddPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        List<GenericPricePointDTO> pricePointDTOS = sampleList.stream()
                .map(hddPricePointMapper::mapTo)
                .toList();

        Optional<HDDDataAndPricePointDTO> returnList = hddPricePointService
                .findByModelNumber(savedHDD.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getHddPricePointDTOList())
                .hasSize(10)
                .containsExactlyElementsOf(pricePointDTOS);
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedHDDData() {

        HDDDTO savedHDD = hddService.save(hddTestingUtility.createTestHDD());

        List<HDDPricePoint> sampleList = Stream.generate(() ->
                        hddPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                hddTestingUtility.createSampleHDDPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        hddPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        Optional<HDDDataAndPricePointDTO> returnList = hddPricePointService
                .findByModelNumber(savedHDD.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getHddDTO().equals(savedHDD));
    }
}