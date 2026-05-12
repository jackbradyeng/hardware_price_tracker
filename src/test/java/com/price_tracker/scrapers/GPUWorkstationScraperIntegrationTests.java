package com.price_tracker.scrapers;

import com.price_tracker.domain.dto.hybrid_dtos.GPUWorkstationDataAndPricePointDTO;
import com.price_tracker.domain.dto.price_point_dtos.GPUWorkstationPricePointDTO;
import com.price_tracker.domain.dto.product_dtos.GPUWorkstationDTO;
import com.price_tracker.domain.entities.price_point_entities.GPUWorkstationPricePoint;
import com.price_tracker.mappers.price_point_mappers.GPUWorkstationPricePointMapper;
import com.price_tracker.repositories.price_point_repos.jdbc_templates.GPUWorkstationPricePointJDBCTemplate;
import com.price_tracker.services.price_point_services.GPUWorkstationPricePointService;
import com.price_tracker.services.product_services.GPUWorkstationService;
import com.price_tracker.testing_data.RestPage;
import com.price_tracker.testing_data.wsgpu_data.WorkstationGPUTestingUtility;
import com.price_tracker.webscraper.dtos.ScrapedDataDTO;
import com.price_tracker.webscraper.product_services.impl.UmartGPUWorkstationScrapingService;
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
import static com.price_tracker.testing_data.vendor_data.UmartWebDomainNames.UMART_RTX_PRO_6000;
import static com.price_tracker.testing_data.wsgpu_data.WorkstationGPUTestingData.TESTING_WS_GPU_MODEL_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GPUWorkstationScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final WorkstationGPUTestingUtility workstationGPUTestingUtility;
    private final UmartGPUWorkstationScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final GPUWorkstationPricePointJDBCTemplate gpuWorkstationPricePointJDBCTemplate;
    private final GPUWorkstationService gpuWorkstationService;
    private final GPUWorkstationPricePointMapper gpuWorkstationPricePointMapper;
    private final GPUWorkstationPricePointService gpuWorkstationPricePointService;

    @Autowired
    public GPUWorkstationScraperIntegrationTests(MockMvc mockMVC,
                                                 WorkstationGPUTestingUtility workstationGPUTestingUtility,
                                                 UmartGPUWorkstationScrapingService scraper,
                                                 ObjectMapper objectMapper,
                                                 GPUWorkstationPricePointJDBCTemplate gpuWorkstationPricePointJDBCTemplate,
                                                 GPUWorkstationService gpuWorkstationService,
                                                 GPUWorkstationPricePointMapper gpuWorkstationPricePointMapper,
                                                 GPUWorkstationPricePointService gpuWorkstationPricePointService) {
        this.mockMVC = mockMVC;
        this.workstationGPUTestingUtility = workstationGPUTestingUtility;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.gpuWorkstationPricePointJDBCTemplate = gpuWorkstationPricePointJDBCTemplate;
        this.gpuWorkstationService = gpuWorkstationService;
        this.gpuWorkstationPricePointMapper = gpuWorkstationPricePointMapper;
        this.gpuWorkstationPricePointService = gpuWorkstationPricePointService;
    }

    @Test
    public void testThatUmartWSGPUScraperReturnsExpectedModelNumber() {
        Optional<ScrapedDataDTO> scrapedDataDTO = scraper.scrapeProductData(UMART_RTX_PRO_6000);
        assert scrapedDataDTO.isPresent() && scrapedDataDTO.get().modelNumber().equals(TESTING_WS_GPU_MODEL_NUMBER);
    }

    @Test
    public void testThatWSGPUPricePointInsertionWithJDBCTemplateReturnsOK() throws Exception {
        List<GPUWorkstationPricePoint> returnList = Stream.generate(() ->
                        scraper.createGPUWorkstationPricePoint(workstationGPUTestingUtility
                                .createSampleWSGPUPricePointData()))
                .limit(10)
                .toList();

        gpuWorkstationPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/workstation_gpu_pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    public void testThatWSGPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(int insertionCount)
            throws Exception {
        List<GPUWorkstationPricePoint> returnList = Stream.generate(() ->
                        scraper.createGPUWorkstationPricePoint(workstationGPUTestingUtility
                                .createSampleWSGPUPricePointData()))
                .limit(insertionCount)
                .toList();

        gpuWorkstationPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/api/workstation_gpu_pricepoints")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // de-serialize the return object so that it's size and contents can be tested
        String contentAsString = result.getResponse().getContentAsString();
        RestPage<GPUWorkstationPricePointDTO> actualPage = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {}
        );
        List<GPUWorkstationPricePointDTO> actualList = actualPage.getContent();

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
    @Disabled
    public void testThatWSGPUPricePointInsertionReturnsExpectedNumberAfterMultipleInsertions() throws Exception {

        // 110 price points -> three round-trips or three insertions
        testThatWSGPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(110);
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatus200Ok() throws Exception {

        GPUWorkstationDTO savedWSGPU = gpuWorkstationService.save(workstationGPUTestingUtility
                .createTestWorkstationGPUDTO());

        List<GPUWorkstationPricePoint> sampleList = Stream.generate(() ->
                        scraper.createGPUWorkstationPricePoint(workstationGPUTestingUtility
                                .createSampleWSGPUPricePointData()))
                .limit(10)
                .toList();

        gpuWorkstationPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/workstation_gpu_pricepoints/" + savedWSGPU.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatusWithRandomModelNumber() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/workstation_gpu_pricepoints/" + "random_model_number")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedPricePoints() {

        // first we save the workstation GPU to the DB
        GPUWorkstationDTO savedWSGPU = gpuWorkstationService.save(workstationGPUTestingUtility
                .createTestWorkstationGPUDTO());

        // next we generate and save a collection of price points with the same model number to the DB
        List<GPUWorkstationPricePoint> sampleList = Stream.generate(() ->
                        scraper.createGPUWorkstationPricePoint(workstationGPUTestingUtility
                                .createSampleWSGPUPricePointData()))
                .limit(10)
                .toList()
                .reversed();

        gpuWorkstationPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // convert price points to DTO for comparison's sake
        List<GPUWorkstationPricePointDTO> pricePointDTOS = sampleList.stream()
                .map(gpuWorkstationPricePointMapper::mapTo)
                .toList();

        // next we query by the workstation GPU's model number - this should return a collection of composite DTOs
        Optional<GPUWorkstationDataAndPricePointDTO> returnList = gpuWorkstationPricePointService
                .findByModelNumber(savedWSGPU.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getGpuWorkstationPricePointDTOList())
                .hasSize(10)
                .containsExactlyElementsOf(pricePointDTOS);
    }

    @Test
    public void testThatFindByModelNumberReturnsExpectedWSGPUData() {

        // first we save the workstation GPU to the DB
        GPUWorkstationDTO savedWSGPU = gpuWorkstationService.save(workstationGPUTestingUtility
                .createTestWorkstationGPUDTO());

        // next we generate and save a collection of price points with the same model number to the DB
        List<GPUWorkstationPricePoint> sampleList = Stream.generate(() ->
                        scraper.createGPUWorkstationPricePoint(workstationGPUTestingUtility
                                .createSampleWSGPUPricePointData()))
                .limit(10)
                .toList();

        gpuWorkstationPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // next we query by the workstation GPU's model number - this should return a collection of composite DTOs
        Optional<GPUWorkstationDataAndPricePointDTO> returnList = gpuWorkstationPricePointService
                .findByModelNumber(savedWSGPU.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getGpuWorkstationDTO().equals(savedWSGPU));
    }
}
