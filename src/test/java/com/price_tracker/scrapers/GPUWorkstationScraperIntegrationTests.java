package com.price_tracker.scrapers;

import com.price_tracker.TestDataUtility;
import com.price_tracker.domain.dto.price_point_dtos.GPUWorkstationPricePointDTO;
import com.price_tracker.domain.entities.price_point_entities.GPUWorkstationPricePoint;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.GPUWorkstationPricePointJDBCTemplate;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartGPUWorkstationScrapingService;
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
import static com.price_tracker.constants.TestingConstants.TESTING_WS_GPU_MODEL_NUMBER;
import static com.price_tracker.constants.WebDomainNames.UMART_RTX_PRO_6000;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GPUWorkstationScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final TestDataUtility tdl;
    private final UmartGPUWorkstationScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final GPUWorkstationPricePointJDBCTemplate gpuWorkstationPricePointJDBCTemplate;

    @Autowired
    public GPUWorkstationScraperIntegrationTests(MockMvc mockMVC,
                                                 TestDataUtility tdl,
                                                 UmartGPUWorkstationScrapingService scraper,
                                                 ObjectMapper objectMapper,
                                                 GPUWorkstationPricePointJDBCTemplate gpuWorkstationPricePointJDBCTemplate) {
        this.mockMVC = mockMVC;
        this.tdl = tdl;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.gpuWorkstationPricePointJDBCTemplate = gpuWorkstationPricePointJDBCTemplate;
    }

    @Test
    public void testThatUmartWSGPUScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_RTX_PRO_6000);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_WS_GPU_MODEL_NUMBER);
    }

    @Test
    public void testThatWSGPUPricePointInsertionWithJDBCTemplateReturnsOK() throws Exception {
        List<GPUWorkstationPricePoint> returnList = Stream.generate(() ->
                        scraper.createGPUWorkstationPricePoint(tdl.createSampleWSGPUPricePointData()))
                .limit(10)
                .toList();

        gpuWorkstationPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/workstation_gpu_pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatWSGPUPricePointInsertionWithJDBCTemplateReturnsExpectedModelNumber() throws Exception {
        List<GPUWorkstationPricePoint> returnList = Stream.generate(() ->
                        scraper.createGPUWorkstationPricePoint(tdl.createSampleWSGPUPricePointData()))
                .limit(10)
                .toList();

        gpuWorkstationPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/workstation_gpu_pricepoints/" + returnList.getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(returnList.getFirst().getModelNumber())
        );
    }

    public void testThatWSGPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(int insertionCount)
            throws Exception {
        List<GPUWorkstationPricePoint> returnList = Stream.generate(() ->
                        scraper.createGPUWorkstationPricePoint(tdl.createSampleWSGPUPricePointData()))
                .limit(insertionCount)
                .toList();

        gpuWorkstationPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/workstation_gpu_pricepoints")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // de-serialize the return object so that it's size and contents can be tested
        String contentAsString = result.getResponse().getContentAsString();
        List<GPUWorkstationPricePointDTO> actualList = objectMapper.readValue(
                contentAsString,
                new TypeReference<>(){}
        );

        // store the sequence of expected IDs
        List<Long> expectedIds = returnList.stream()
                .map(GPUWorkstationPricePoint::getId)
                .toList();

        // ensure that the return instance has the expected size and corresponding IDs
        assertThat(actualList)
                .hasSize(returnList.size())
                .extracting(GPUWorkstationPricePointDTO::getId)
                .containsExactlyElementsOf(expectedIds);
    }

    @Test
    public void testThatWSGPUPricePointInsertionReturnsExpectedNumberOfObjectsAndIDs() throws Exception {

        testThatWSGPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(10);
    }

    @Test
    public void testThatWSGPUPricePointInsertionReturnsExpectedNumberAfterMultipleInsertions() throws Exception {

        // 110 price points -> three round-trips or three insertions
        testThatWSGPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(110);
    }
}
