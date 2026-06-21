package com.price_tracker.scrapers;

import com.price_tracker.domain.dto.hybrid_dtos.GPUDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GenericPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.domain.entities.price_point_entities.GPUPricePoint;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.GPUPricePointJDBCTemplate;
import com.price_tracker.services.price_point_services.GPUPricePointService;
import com.price_tracker.services.product_services.GPUService;
import com.price_tracker.testing_data.RestPage;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartGPUScrapingService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
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
import static com.price_tracker.testing_data.gpu_data.GPUTestingData.TESTING_GPU_MODEL_NUMBER;
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_ASUS_5070TI;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class GPUScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final GPUTestingUtility gpuTestingUtility;
    private final UmartGPUScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final GPUPricePointJDBCTemplate gpuPricePointJDBCTemplate;
    private final GPUService gpuService;
    private final GenericMapper<GPUPricePoint, GenericPricePointDTO> gpuPricePointMapper;
    private final GPUPricePointService gpuPricePointService;

    @Autowired
    public GPUScraperIntegrationTests(MockMvc mockMVC,
                                      GPUTestingUtility gpuTestingUtility,
                                      UmartGPUScrapingService scraper,
                                      ObjectMapper objectMapper,
                                      MapperFactory mapperFactory,
                                      GPUPricePointJDBCTemplate gpuPricePointJDBCTemplate,
                                      GPUService gpuService,
                                      GPUPricePointService gpuPricePointService) {
        this.mockMVC = mockMVC;
        this.gpuTestingUtility = gpuTestingUtility;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.gpuPricePointJDBCTemplate = gpuPricePointJDBCTemplate;
        this.gpuService = gpuService;
        this.gpuPricePointMapper = mapperFactory.create(GPUPricePoint.class, GenericPricePointDTO.class);
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
                        scraper.createGPUPricePoint(gpuTestingUtility.createSampleGPUPricePointData()))
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
                        scraper.createGPUPricePoint(gpuTestingUtility.createSampleGPUPricePointData()))
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
        RestPage<GenericPricePointDTO> actualPage = objectMapper.readValue(
                contentAsString,
                new TypeReference<>(){}
        );
        List<GenericPricePointDTO> actualList = actualPage.getContent();

        // store the sequence of expected IDs
        List<Long> expectedIds = returnList.stream()
                .map(GPUPricePoint::getId)
                .toList();

        // ensure that the return instance has the expected size and corresponding IDs
        assertThat(actualList)
                .hasSize(returnList.size())
                .extracting(GenericPricePointDTO::getId)
                .containsExactlyElementsOf(expectedIds);
    }

    @Test
    public void testThatGPUPricePointInsertionReturnsExpectedNumberOfObjectsAndIDs() throws Exception {

        testThatGPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(10);
    }

    @Test
    @Disabled
    public void testThatGPUPricePointInsertionReturnsExpectedNumberAfterMultipleInsertions() throws Exception {

        // 110 price points -> three round-trips or three insertions
        testThatGPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(110);
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatus200Ok() throws Exception {

        GPUDTO savedGPU = gpuService.save(gpuTestingUtility.createTestGPU());

        List<GPUPricePoint> sampleList = Stream.generate(() ->
                        scraper.createGPUPricePoint(gpuTestingUtility.createSampleGPUPricePointData()))
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
    public void testThatFindByModelNumberReturnsHttpStatusWithRandomModelNumber() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/gpu_pricepoints/" + "random_model_number")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedPricePoints() {

        // first we save the GPU to the DB
        GPUDTO savedGPU = gpuService.save(gpuTestingUtility.createTestGPU());

        // next we generate and save a collection of price points with the same model number to the DB
        List<GPUPricePoint> sampleList = Stream.generate(() ->
                        scraper.createGPUPricePoint(gpuTestingUtility.createSampleGPUPricePointData()))
                .limit(10)
                .toList();

        gpuPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // convert price points to DTO for comparison's sake
        List<GenericPricePointDTO> pricePointDTOS =  sampleList.stream()
                .map(gpuPricePointMapper::mapTo)
                .toList()
                .reversed();

        // next we query by the GPU's model number - this should return a collection of composite DTOs
        Optional<GPUDataAndPricePointDTO> returnList = gpuPricePointService
                .findByModelNumber(savedGPU.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getGpuPricePointDTOList())
                .hasSize(10)
                .containsExactlyElementsOf(pricePointDTOS);
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedGPUData() {

        // first we save the GPU to the DB
        GPUDTO savedGPU = gpuService.save(gpuTestingUtility.createTestGPU());

        // next we generate and save a collection of price points with the same model number to the DB
        List<GPUPricePoint> sampleList = Stream.generate(() ->
                        scraper.createGPUPricePoint(gpuTestingUtility.createSampleGPUPricePointData()))
                .limit(10)
                .toList();

        gpuPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // next we query by the GPU's model number - this should return a collection of composite DTOs
        Optional<GPUDataAndPricePointDTO> returnList = gpuPricePointService
                .findByModelNumber(savedGPU.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getGpuDTO().equals(savedGPU));
    }
}
