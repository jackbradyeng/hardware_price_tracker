package com.price_tracker.scrapers.integration_tests;

import com.price_tracker.domain.dto.hybrid_dtos.RAMDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import com.price_tracker.domain.entities.price_point_entities.RAMPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.GenericPricePointJdbcTemplate;
import com.price_tracker.services.price_point_services.RAMPricePointService;
import com.price_tracker.services.product_services.RAMService;
import com.price_tracker.testing_data.RestPage;
import com.price_tracker.testing_data.ram_data.RAMTestingUtility;
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
public class RAMScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final RAMTestingUtility ramTestingUtility;
    private final GenericScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final GenericPricePointJdbcTemplate<RAMPricePoint> ramGenericPricePointJDBCTemplate;
    private final RAMService ramService;
    private final GenericMapper<RAMPricePoint, GenericPricePointDTO> ramPricePointMapper;
    private final RAMPricePointService ramPricePointService;

    @Autowired
    public RAMScraperIntegrationTests(MockMvc mockMVC,
                                      RAMTestingUtility ramTestingUtility,
                                      GenericScrapingService scraper,
                                      ObjectMapper objectMapper,
                                      MapperFactory mapperFactory,
                                      GenericPricePointJdbcTemplate<RAMPricePoint> ramGenericPricePointJDBCTemplate,
                                      RAMService ramService,
                                      RAMPricePointService ramPricePointService) {
        this.mockMVC = mockMVC;
        this.ramTestingUtility = ramTestingUtility;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.ramGenericPricePointJDBCTemplate = ramGenericPricePointJDBCTemplate;
        this.ramService = ramService;
        this.ramPricePointMapper = mapperFactory.create(RAMPricePoint.class, GenericPricePointDTO.class);
        this.ramPricePointService = ramPricePointService;
    }

    @Test
    public void testThatRAMPricePointInsertionWithJDBCTemplateReturnsOK() throws Exception {
        List<RAMPricePoint> returnList = Stream.generate(() ->
                        ramPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                ramTestingUtility.createSampleRAMPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        ramGenericPricePointJDBCTemplate.batchInsertPricePoints(returnList);

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
                        ramPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                ramTestingUtility.createSampleRAMPricePointData(), UMART, AUD)))
                .limit(insertionCount)
                .toList();

        ramGenericPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/api/ram_pricepoints")
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
                .map(RAMPricePoint::getId)
                .toList();

        // ensure that the return instance has the expected size and corresponding IDs
        assertThat(actualList)
                .hasSize(returnList.size())
                .extracting(GenericPricePointDTO::getId)
                .containsExactlyElementsOf(expectedIds);
    }

    @Test
    public void testThatRAMPricePointInsertionReturnsExpectedNumberOfObjectsAndIDs() throws Exception {

        testThatRAMPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(10);
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatus200Ok() throws Exception {

        RAMDTO savedRAM = ramService.save(ramTestingUtility.createTestRAM());

        List<RAMPricePoint> sampleList = Stream.generate(() ->
                        ramPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                ramTestingUtility.createSampleRAMPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        ramGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/ram_pricepoints/" + savedRAM.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatusWithRandomModelNumber() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/ram_pricepoints/" + "random_model_number")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedPricePoints() {

        // first we save the RAM to the DB
        RAMDTO savedRAM = ramService.save(ramTestingUtility.createTestRAM());

        // next we generate and save a collection of price points with the same model number to the DB
        List<RAMPricePoint> sampleList = Stream.generate(() ->
                        ramPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                ramTestingUtility.createSampleRAMPricePointData(), UMART, AUD)))
                .limit(10)
                .toList()
                .reversed();

        ramGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // convert price points to DTO for comparison's sake
        List<GenericPricePointDTO> pricePointDTOS = sampleList.stream()
                .map(ramPricePointMapper::mapTo)
                .toList();

        // next we query by the RAM's model number - this should return a collection of composite DTOs
        Optional<RAMDataAndPricePointDTO> returnList = ramPricePointService
                .findByModelNumber(savedRAM.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getRamPricePointDTOList())
                .hasSize(10)
                .containsExactlyElementsOf(pricePointDTOS);
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedRAMData() {

        // first we save the RAM to the DB
        RAMDTO savedRAM = ramService.save(ramTestingUtility.createTestRAM());

        // next we generate and save a collection of price points with the same model number to the DB
        List<RAMPricePoint> sampleList = Stream.generate(() ->
                        ramPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                ramTestingUtility.createSampleRAMPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        ramGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // next we query by the RAM's model number - this should return a collection of composite DTOs
        Optional<RAMDataAndPricePointDTO> returnList = ramPricePointService
                .findByModelNumber(savedRAM.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getRamDTO().equals(savedRAM));
    }
}
