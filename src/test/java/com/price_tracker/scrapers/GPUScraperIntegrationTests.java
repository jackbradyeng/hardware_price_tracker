package com.price_tracker.scrapers;

import com.price_tracker.TestDataUtility;
import com.price_tracker.domain.dto.price_point_dtos.GPUPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.GPUPricePoint;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.GPUPricePointJDBCTemplate;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartGPUScrapingService;
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
import static com.price_tracker.constants.TestingConstants.TESTING_GPU_MODEL_NUMBER;
import static com.price_tracker.constants.WebDomainNames.UMART_ASUS_5070TI;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GPUScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final TestDataUtility tdl;
    private final UmartGPUScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final GPUPricePointJDBCTemplate gpuPricePointJDBCTemplate;

    @Autowired
    public GPUScraperIntegrationTests(MockMvc mockMVC,
                                      TestDataUtility tdl,
                                      UmartGPUScrapingService scraper,
                                      ObjectMapper objectMapper,
                                      GPUPricePointJDBCTemplate gpuPricePointJDBCTemplate) {
        this.mockMVC = mockMVC;
        this.tdl = tdl;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.gpuPricePointJDBCTemplate = gpuPricePointJDBCTemplate;
    }

    @Test
    public void testThatUmartGPUScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_ASUS_5070TI);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_GPU_MODEL_NUMBER);
    }

    @Test
    public void testThatGPUPricePointInsertionWithJDBCTemplateReturnsOK() throws Exception {
        List<GPUPricePoint> returnList = Stream.generate(() ->
                        scraper.createGPUPricePoint(tdl.createSampleGPUPricePointData()))
                .limit(10)
                .toList();

        gpuPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/gpu_pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGPUPricePointInsertionWithJDBCTemplateReturnsExpectedModelNumber() throws Exception {
        List<GPUPricePoint> returnList = Stream.generate(() ->
                        scraper.createGPUPricePoint(tdl.createSampleGPUPricePointData()))
                .limit(10)
                .toList();

        gpuPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/gpu_pricepoints/" + returnList.getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(returnList.getFirst().getModelNumber())
        );
    }

    public void testThatGPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(int insertionCount)
            throws Exception {
        List<GPUPricePoint> returnList = Stream.generate(() ->
                        scraper.createGPUPricePoint(tdl.createSampleGPUPricePointData()))
                .limit(insertionCount)
                .toList();

        gpuPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/api/gpu_pricepoints")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // de-serialize the return object so that it's size and contents can be tested
        String contentAsString = result.getResponse().getContentAsString();
        List<GPUPricePointDTO> actualList = objectMapper.readValue(
                contentAsString,
                new TypeReference<>(){}
        );

        // store the sequence of expected IDs
        List<Long> expectedIds = returnList.stream()
                .map(GPUPricePoint::getId)
                .toList();

        // ensure that the return instance has the expected size and corresponding IDs
        assertThat(actualList)
                .hasSize(returnList.size())
                .extracting(GPUPricePointDTO::getId)
                .containsExactlyElementsOf(expectedIds);
    }

    @Test
    public void testThatGPUPricePointInsertionReturnsExpectedNumberOfObjectsAndIDs() throws Exception {

        testThatGPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(10);
    }

    @Test
    public void testThatGPUPricePointInsertionReturnsExpectedNumberAfterMultipleInsertions() throws Exception {

        // 110 price points -> three round-trips or three insertions
        testThatGPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(110);
    }
}
