package com.priceTracker.scrapers.integrationTests;

import com.priceTracker.domain.dto.hybridDTOs.GPUDataAndPricePointDTO;
import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.domain.dto.productDTOs.GPUDTO;
import com.priceTracker.domain.entities.pricePointEntities.GPUPricePoint;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.repositories.pricePointRepositories.jdbcTemplates.GenericPricePointJdbcTemplate;
import com.priceTracker.services.pricePointServices.GenericPricePointService;
import com.priceTracker.services.productServices.GenericProductService;
import com.priceTracker.testingData.RestPage;
import com.priceTracker.testingData.gpuData.GPUTestingUtility;
import com.priceTracker.webscraper.productServices.GenericScrapingService;
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
import static com.priceTracker.constants.otherConstants.CurrencyConstants.AUD;
import static com.priceTracker.constants.vendorConstants.VendorNames.UMART;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class GPUScraperIntegrationTests {

    private final MockMvc mockMVC;
    private final GPUTestingUtility gpuTestingUtility;
    private final GenericScrapingService scraper;
    private final ObjectMapper objectMapper;
    private final GenericPricePointJdbcTemplate<GPUPricePoint> gpuGenericPricePointJDBCTemplate;
    private final GenericProductService<GPUDTO> gpuService;
    private final GenericMapper<GPUPricePoint, GenericPricePointDTO> gpuPricePointMapper;
    private final GenericPricePointService<GPUDataAndPricePointDTO> gpuPricePointService;

    @Autowired
    public GPUScraperIntegrationTests(MockMvc mockMVC,
                                      GPUTestingUtility gpuTestingUtility,
                                      GenericScrapingService scraper,
                                      ObjectMapper objectMapper,
                                      MapperFactory mapperFactory,
                                      GenericPricePointJdbcTemplate<GPUPricePoint> gpuGenericPricePointJDBCTemplate,
                                      GenericProductService<GPUDTO> gpuService,
                                      GenericPricePointService<GPUDataAndPricePointDTO> gpuPricePointService) {
        this.mockMVC = mockMVC;
        this.gpuTestingUtility = gpuTestingUtility;
        this.scraper = scraper;
        this.objectMapper = objectMapper;
        this.gpuGenericPricePointJDBCTemplate = gpuGenericPricePointJDBCTemplate;
        this.gpuService = gpuService;
        this.gpuPricePointMapper = mapperFactory.create(GPUPricePoint.class, GenericPricePointDTO.class);
        this.gpuPricePointService = gpuPricePointService;
    }

    @Test
    public void testThatGPUPricePointInsertionWithJDBCTemplateReturnsHttpStatus200Ok() throws Exception {
        List<GPUPricePoint> returnList = Stream.generate(() ->
                        gpuPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                gpuTestingUtility.createSampleGPUPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        gpuGenericPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/v1/gpu_pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    public void testThatGPUPricePointInsertReturnsExpectedNumberAfterGivenNumberOfInsertions(int insertionCount)
            throws Exception {
        List<GPUPricePoint> returnList = Stream.generate(() ->
                        gpuPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                gpuTestingUtility.createSampleGPUPricePointData(), UMART, AUD)))
                .limit(insertionCount)
                .toList();

        gpuGenericPricePointJDBCTemplate.batchInsertPricePoints(returnList);

        MvcResult result = mockMVC.perform(
                        MockMvcRequestBuilders.get("/api/v1/gpu_pricepoints")
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
    public void testThatFindByModelNumberReturnsHttpStatus200Ok() throws Exception {

        GPUDTO savedGPU = gpuService.save(gpuTestingUtility.createTestGPU());

        List<GPUPricePoint> sampleList = Stream.generate(() ->
                        gpuPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                gpuTestingUtility.createSampleGPUPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        gpuGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/v1/gpu_pricepoints/" + savedGPU.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindByModelNumberReturnsHttpStatusWithRandomModelNumber() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/v1/gpu_pricepoints/" + "random_model_number")
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
                        gpuPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                gpuTestingUtility.createSampleGPUPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        gpuGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

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
                        gpuPricePointMapper.mapFrom(scraper.createGenericPricePoint(
                                gpuTestingUtility.createSampleGPUPricePointData(), UMART, AUD)))
                .limit(10)
                .toList();

        gpuGenericPricePointJDBCTemplate.batchInsertPricePoints(sampleList);

        // next we query by the GPU's model number - this should return a collection of composite DTOs
        Optional<GPUDataAndPricePointDTO> returnList = gpuPricePointService
                .findByModelNumber(savedGPU.getModelNumber(), Pageable.unpaged());

        assertThat(returnList).isPresent();
        assertThat(returnList.get().getGpuDTO().equals(savedGPU));
    }
}