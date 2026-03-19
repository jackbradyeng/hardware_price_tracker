package com.price_tracker.scrapers;

import com.price_tracker.TestDataUtility;
import com.price_tracker.domain.dto.price_point_dtos.CPUPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.CPUPricePoint;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.CPUPricePointJDBCTemplate;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartCPUScrapingService;
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
import static com.price_tracker.constants.TestingConstants.TESTING_CPU_MODEL_NUMBER;
import static com.price_tracker.constants.WebDomainNames.UMART_RYZEN_9_9800X3D;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CPUScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final TestDataUtility tdl;
    private final UmartCPUScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final CPUPricePointJDBCTemplate cpuPricePointJDBCTemplate;

    @Autowired
    public CPUScraperIntegrationTests(MockMvc mockMVC,
                                      TestDataUtility tdl,
                                      UmartCPUScrapingService scraper,
                                      ObjectMapper objectMapper,
                                      CPUPricePointJDBCTemplate cpuPricePointJDBCTemplate) {
        this.mockMVC = mockMVC;
        this.tdl = tdl;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.cpuPricePointJDBCTemplate = cpuPricePointJDBCTemplate;
    }

    @Test
    public void testThatUmartCPUScrapingServiceReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_RYZEN_9_9800X3D);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_CPU_MODEL_NUMBER);
    }

    @Test
    public void testThatCPUPricePointInsertionWithJDBCTemplateReturnsOK() throws Exception {
        List<CPUPricePoint> returnList = Stream.generate(() ->
                        scraper.createCPUPricePoint(tdl.createSampleCPUPricePointData()))
                .limit(10)
                .toList();

        cpuPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/cpu_pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatCPUPricePointInsertionWithJDBCTemplateReturnsExpectedModelNumber() throws Exception {
        List<CPUPricePoint> returnList = Stream.generate(() ->
                        scraper.createCPUPricePoint(tdl.createSampleCPUPricePointData()))
                .limit(10)
                .toList();

        cpuPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/cpu_pricepoints/" + returnList.getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(returnList.getFirst().getModelNumber())
        );
    }

    public void testThatCPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(int insertionCount)
            throws Exception {
        List<CPUPricePoint> returnList = Stream.generate(() ->
                        scraper.createCPUPricePoint(tdl.createSampleCPUPricePointData()))
                .limit(insertionCount)
                .toList();

        cpuPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/cpu_pricepoints")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // de-serialize the return object so that it's size and contents can be tested
        String contentAsString = result.getResponse().getContentAsString();
        List<CPUPricePointDTO> actualList = objectMapper.readValue(
                contentAsString,
                new TypeReference<>(){}
        );

        // store the sequence of expected IDs
        List<Long> expectedIds = returnList.stream()
                .map(CPUPricePoint::getId)
                .toList();

        // ensure that the return instance has the expected size and corresponding IDs
        assertThat(actualList)
                .hasSize(returnList.size())
                .extracting(CPUPricePointDTO::getId)
                .containsExactlyElementsOf(expectedIds);
    }

    @Test
    public void testThatCPUPricePointInsertionReturnsExpectedNumberOfObjectsAndIDs() throws Exception {

        testThatCPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(10);
    }

    @Test
    public void testThatCPUPricePointInsertionReturnsExpectedNumberAfterMultipleInsertions() throws Exception {

        // 110 price points -> three round-trips or three insertions
        testThatCPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(110);
    }
}
