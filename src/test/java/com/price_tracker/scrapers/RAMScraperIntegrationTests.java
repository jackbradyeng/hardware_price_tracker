package com.price_tracker.scrapers;

import com.price_tracker.TestDataUtility;
import com.price_tracker.domain.dto.price_point_dtos.RAMPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.RAMPricePoint;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.RAMPricePointJDBCTemplate;
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
import static com.price_tracker.constants.TestingConstants.TESTING_RAM_MODEL_NUMBER;
import static com.price_tracker.constants.WebDomainNames.UMART_KINGSTON_KINGSTON_F64G;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RAMScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final TestDataUtility tdl;
    private final UmartRAMScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final RAMPricePointJDBCTemplate ramPricePointJDBCTemplate;

    @Autowired
    public RAMScraperIntegrationTests(MockMvc mockMVC,
                                      TestDataUtility tdl,
                                      UmartRAMScrapingService scraper,
                                      ObjectMapper objectMapper,
                                      RAMPricePointJDBCTemplate ramPricePointJDBCTemplate) {
        this.mockMVC = mockMVC;
        this.tdl = tdl;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.ramPricePointJDBCTemplate = ramPricePointJDBCTemplate;
    }

    @Test
    public void testThatUmartRAMScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_KINGSTON_KINGSTON_F64G);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_RAM_MODEL_NUMBER);
    }

    @Test
    public void testThatRAMPricePointInsertionWithJDBCTemplateReturnsOK() throws Exception {
        List<RAMPricePoint> returnList = Stream.generate(() ->
                        scraper.createRAMPricePoint(tdl.createSampleRAMPricePointData()))
                .limit(10)
                .toList();

        ramPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/ram_pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatRAMPricePointInsertionWithJDBCTemplateReturnsExpectedModelNumber() throws Exception {
        List<RAMPricePoint> returnList = Stream.generate(() ->
                        scraper.createRAMPricePoint(tdl.createSampleRAMPricePointData()))
                .limit(10)
                .toList();

        ramPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/ram_pricepoints/" + returnList.getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(returnList.getFirst().getModelNumber())
        );
    }

    public void testThatRAMPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(int insertionCount)
            throws Exception {
        List<RAMPricePoint> returnList = Stream.generate(() ->
                        scraper.createRAMPricePoint(tdl.createSampleRAMPricePointData()))
                .limit(insertionCount)
                .toList();

        ramPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/ram_pricepoints")
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
}
