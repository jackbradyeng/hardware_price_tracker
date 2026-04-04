package com.price_tracker.scrapers.ram_tests;

import com.price_tracker.domain.dto.hybrid_dtos.RAMDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.RAMPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import com.price_tracker.domain.entities.price_point_entities.RAMPricePoint;
import com.price_tracker.domain.entities.product_entities.RAMEntity;
import com.price_tracker.mappers.price_point_mappers.RAMPricePointMapper;
import com.price_tracker.mappers.product_mappers.RAMMapper;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.RAMPricePointJDBCTemplate;
import com.price_tracker.services.price_point_services.RAMPricePointService;
import com.price_tracker.services.product_services.RAMService;
import com.price_tracker.testing_data.ram_data.RAMTestingUtility;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartRAMScrapingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
import static com.price_tracker.testing_data.ram_data.RAMTestingData.TESTING_RAM_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_KINGSTON_KINGSTON_F64G;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RAMScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final RAMTestingUtility ramTestingUtility;
    private final UmartRAMScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final RAMPricePointJDBCTemplate ramPricePointJDBCTemplate;
    private final RAMService ramService;
    private final RAMMapper ramMapper;
    private final RAMPricePointMapper ramPricePointMapper;
    private final RAMPricePointService ramPricePointService;

    @Autowired
    public RAMScraperIntegrationTests(MockMvc mockMVC,
                                      RAMTestingUtility ramTestingUtility,
                                      UmartRAMScrapingService scraper,
                                      ObjectMapper objectMapper,
                                      RAMPricePointJDBCTemplate ramPricePointJDBCTemplate,
                                      RAMService ramService,
                                      RAMMapper ramMapper,
                                      RAMPricePointMapper ramPricePointMapper,
                                      RAMPricePointService ramPricePointService) {
        this.mockMVC = mockMVC;
        this.ramTestingUtility = ramTestingUtility;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.ramPricePointJDBCTemplate = ramPricePointJDBCTemplate;
        this.ramService = ramService;
        this.ramMapper = ramMapper;
        this.ramPricePointMapper = ramPricePointMapper;
        this.ramPricePointService = ramPricePointService;
    }

    @Test
    public void testThatUmartRAMScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_KINGSTON_KINGSTON_F64G);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_RAM_MODEL_NUMBER);
    }

    @Test
    public void testThatRAMPricePointInsertionWithJDBCTemplateReturnsOK() throws Exception {
        List<RAMPricePoint> returnList = Stream.generate(() ->
                        scraper.createRAMPricePoint(ramTestingUtility.createSampleRAMPricePointData()))
                .limit(10)
                .toList();

        ramPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/ram_pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    public void testThatRAMPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(int insertionCount)
            throws Exception {
        List<RAMPricePoint> returnList = Stream.generate(() ->
                        scraper.createRAMPricePoint(ramTestingUtility.createSampleRAMPricePointData()))
                .limit(insertionCount)
                .toList();

        ramPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/api/ram_pricepoints")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // de-serialize the return object so that it's size and contents can be tested
        String contentAsString = result.getResponse().getContentAsString();
        List<RAMPricePointDTO> actualList = objectMapper.readValue(
                contentAsString,
                new TypeReference<>(){}
        );

        // store the sequence of expected IDs
        List<Long> expectedIds = returnList.stream()
                .map(RAMPricePoint::getId)
                .toList();

        // ensure that the return instance has the expected size and corresponding IDs
        assertThat(actualList)
                .hasSize(returnList.size())
                .extracting(RAMPricePointDTO::getId)
                .containsExactlyElementsOf(expectedIds);
    }

    @Test
    public void testThatRAMPricePointInsertionReturnsExpectedNumberOfObjectsAndIDs() throws Exception {

        testThatRAMPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(10);
    }

    @Test
    public void testThatRAMPricePointInsertionReturnsExpectedNumberAfterMultipleInsertions() throws Exception {

        // 110 price points -> three round-trips or three insertions
        testThatRAMPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(110);
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatus200Ok() throws Exception {

        RAMEntity savedRAM = ramService.save(ramTestingUtility.createTestRAM());

        List<RAMPricePoint> sampleList = Stream.generate(() ->
                        scraper.createRAMPricePoint(ramTestingUtility.createSampleRAMPricePointData()))
                .limit(10)
                .toList();

        ramPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/ram_pricepoints/" + savedRAM.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedPricePoints() {

        // first we save the RAM to the DB
        RAMEntity savedRAM = ramService.save(ramTestingUtility.createTestRAM());

        // next we generate and save a collection of price points with the same model number to the DB
        List<RAMPricePoint> sampleList = Stream.generate(() ->
                        scraper.createRAMPricePoint(ramTestingUtility.createSampleRAMPricePointData()))
                .limit(10)
                .toList();

        ramPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // convert price points to DTO for comparison's sake
        List<RAMPricePointDTO> pricePointDTOS = sampleList.stream()
                .map(ramPricePointMapper::mapTo)
                .toList();

        // next we query by the RAM's model number - this should return a collection of composite DTOs
        Optional<RAMDataAndPricePointDTO> returnList = ramPricePointService
                .findByModelNumber(savedRAM.getModelNumber());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getRamPricePointDTOList())
                .hasSize(10)
                .containsExactlyElementsOf(pricePointDTOS);
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedRAMData() {

        // first we save the RAM to the DB
        RAMEntity savedRAM = ramService.save(ramTestingUtility.createTestRAM());

        // map to a DTO for comparison's sake
        RAMDTO ramDTO = ramMapper.mapTo(savedRAM);

        // next we generate and save a collection of price points with the same model number to the DB
        List<RAMPricePoint> sampleList = Stream.generate(() ->
                        scraper.createRAMPricePoint(ramTestingUtility.createSampleRAMPricePointData()))
                .limit(10)
                .toList();

        ramPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // next we query by the RAM's model number - this should return a collection of composite DTOs
        Optional<RAMDataAndPricePointDTO> returnList = ramPricePointService
                .findByModelNumber(savedRAM.getModelNumber());

        assertThat(returnList.isPresent());
        assertThat(returnList.get().getRamDTO().equals(ramDTO));
    }
}
