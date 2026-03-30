package com.price_tracker.scrapers;

import com.price_tracker.TestDataUtility;
import com.price_tracker.domain.dto.hybrid_dtos.GPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GPUPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.domain.entities.price_point_entities.GPUPricePoint;
import com.price_tracker.domain.entities.product_entities.GPUEntity;
import com.price_tracker.mappers.price_point_mappers.GPUPricePointMapper;
import com.price_tracker.mappers.product_mappers.GPUMapper;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.GPUPricePointJDBCTemplate;
import com.price_tracker.services.price_point_services.GPUPricePointService;
import com.price_tracker.services.product_services.GPUService;
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
import static com.price_tracker.testing_data.TestingConstants.TESTING_GPU_MODEL_NUMBER;
import static com.price_tracker.testing_data.UmartWebDomainNames.UMART_ASUS_5070TI;
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
    private final GPUService gpuService;
    private final GPUMapper gpuMapper;
    private final GPUPricePointMapper gpuPricePointMapper;
    private final GPUPricePointService gpuPricePointService;
    @Autowired
    public GPUScraperIntegrationTests(MockMvc mockMVC,
                                      TestDataUtility tdl,
                                      UmartGPUScrapingService scraper,
                                      ObjectMapper objectMapper,
                                      GPUPricePointJDBCTemplate gpuPricePointJDBCTemplate,
                                      GPUService gpuService,
                                      GPUMapper gpuMapper,
                                      GPUPricePointMapper gpuPricePointMapper,
                                      GPUPricePointService gpuPricePointService) {
        this.mockMVC = mockMVC;
        this.tdl = tdl;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.gpuPricePointJDBCTemplate = gpuPricePointJDBCTemplate;
        this.gpuService = gpuService;
        this.gpuMapper = gpuMapper;
        this.gpuPricePointMapper = gpuPricePointMapper;
        this.gpuPricePointService = gpuPricePointService;
    }

    @Test
    public void testThatUmartGPUScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_ASUS_5070TI);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_GPU_MODEL_NUMBER);
    }

    @Test
    public void testThatGPUPricePointInsertionWithJDBCTemplateReturnsHttpStatus200Ok() throws Exception {
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

    @Test
    public void testThatFindByModelNumberReturnsHttpStatus200Ok() throws Exception {

        GPUEntity savedGPU = gpuService.save(tdl.createTestGPU());

        List<GPUPricePoint> sampleList = Stream.generate(() ->
                        scraper.createGPUPricePoint(tdl.createSampleGPUPricePointData()))
                .limit(10)
                .toList();

        gpuPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/gpu_pricepoints/" + savedGPU.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedPricePoints() throws Exception {

        // first we save the GPU to the DB
        GPUEntity savedGPU = gpuService.save(tdl.createTestGPU());

        // next we generate and save a collection of price points with the same model number to the DB
        List<GPUPricePoint> sampleList = Stream.generate(() ->
                        scraper.createGPUPricePoint(tdl.createSampleGPUPricePointData()))
                .limit(10)
                .toList();

        gpuPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // convert price points to DTO for comparison's sake
        List<GPUPricePointDTO> pricePointDTOS =  sampleList.stream()
                .map(gpuPricePointMapper::mapTo)
                .toList();

        // next we query by the GPU's model number - this should return a collection of composite DTOs
        GPUDataAndPricePointDTO returnList = gpuPricePointService.findByModelNumber(savedGPU.getModelNumber());

        assertThat(returnList.getGpuPricePointDTOList())
                .hasSize(10)
                .containsExactlyElementsOf(pricePointDTOS);
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedGPUData() throws Exception {

        // first we save the GPU to the DB
        GPUEntity savedGPU = gpuService.save(tdl.createTestGPU());

        // map to a DTO for comparison's sake
        GPUDTO gpuDTO = gpuMapper.mapTo(savedGPU);

        // next we generate and save a collection of price points with the same model number to the DB
        List<GPUPricePoint> sampleList = Stream.generate(() ->
                        scraper.createGPUPricePoint(tdl.createSampleGPUPricePointData()))
                .limit(10)
                .toList();

        gpuPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // next we query by the GPU's model number - this should return a collection of composite DTOs
        GPUDataAndPricePointDTO returnList = gpuPricePointService.findByModelNumber(savedGPU.getModelNumber());

        assert returnList.getGpuDTO().equals(gpuDTO);
    }
}
