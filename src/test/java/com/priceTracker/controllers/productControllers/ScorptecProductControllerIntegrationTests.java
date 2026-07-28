package com.priceTracker.controllers.productControllers;

import com.priceTracker.domain.dto.productDTOs.CPUDTO;
import com.priceTracker.domain.dto.productDTOs.GPUDTO;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.services.productServices.GenericProductService;
import com.priceTracker.testingData.cpuData.CPUTestingUtility;
import com.priceTracker.testingData.vendorData.ScorptecTestDataUtility;
import com.priceTracker.domain.dto.vendorDTOs.VendorProductDTO;
import com.priceTracker.repositories.vendorRepositories.ScorptecProductRepository;
import com.priceTracker.services.vendorServices.impl.ScorptecProductServiceImpl;
import com.priceTracker.testingData.gpuData.GPUTestingUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;
import java.util.List;
import static com.priceTracker.testingData.vendorData.VendorWebDomainNames.SCORPTEC_ASUS_5070TI;
import static com.priceTracker.testingData.gpuData.GPUTestingData.PRODUCT_TYPE_GPU;
import static com.priceTracker.testingData.gpuData.GPUTestingData.TESTING_GPU_MODEL_NUMBER;
import static com.priceTracker.constants.VendorNames.SCORPTEC;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ScorptecProductControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ScorptecProductServiceImpl scorptecProductService;
    private final GenericProductService<GPUDTO> gpuService;
    private final GenericProductService<CPUDTO> cpuService;
    private final GPUTestingUtility gpuTestingUtility;
    private final CPUTestingUtility cpuTestingUtility;
    private final ScorptecTestDataUtility tdl;

    @Autowired
    public ScorptecProductControllerIntegrationTests(MockMvc mockMvc,
                                                     ScorptecProductRepository scorptecProductRepository,
                                                     ModelMapper modelMapper,
                                                     MapperFactory mapperFactory,
                                                     GenericProductService<GPUDTO> gpuService,
                                                     GenericProductService<CPUDTO> cpuService,
                                                     GPUTestingUtility gpuTestingUtility,
                                                     CPUTestingUtility cpuTestingUtility,
                                                     ScorptecTestDataUtility tdl) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.scorptecProductService = new ScorptecProductServiceImpl(scorptecProductRepository, modelMapper, mapperFactory);
        this.gpuService = gpuService;
        this.cpuService = cpuService;
        this.gpuTestingUtility = gpuTestingUtility;
        this.cpuTestingUtility = cpuTestingUtility;
        this.tdl = tdl;
    }

    // CREATE TESTS
    @Test
    public void testThatCreateScorptecProductReturnsHttpStatus201Created() throws Exception {
        VendorProductDTO testProductEntity = gpuTestingUtility.createTestScorptecGPU();
        String testProductString = objectMapper.writeValueAsString(testProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/scorptecproducts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testProductString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void TestThatCreatedScorptecProductReturnsSavedScorptecProduct() throws Exception {
        VendorProductDTO testProductEntity = gpuTestingUtility.createTestScorptecGPU();
        String testProductString = objectMapper.writeValueAsString(testProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/scorptecproducts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testProductString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productType").value(PRODUCT_TYPE_GPU)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(TESTING_GPU_MODEL_NUMBER)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.url").value(SCORPTEC_ASUS_5070TI)
        );
    }

    @Test
    public void testThatCreateListOfScorptecProductsReturns201Created() throws Exception {
        List<VendorProductDTO> testVendorProductDTOS = tdl.createTestScorptecProducts();
        String jsonString = objectMapper.writeValueAsString(testVendorProductDTOS);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/scorptecproducts/saveall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    // READ TESTS
    @Test
    public void testThatScorptecProductReadAllReturnsHttpStatus200ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scorptecproducts")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetScorptecProductByIDReturnsHttpStatusOkWhenProductExists() throws Exception {
        VendorProductDTO scorptecProductEntity = gpuTestingUtility.createTestScorptecGPU();
        VendorProductDTO savedProduct = scorptecProductService.save(scorptecProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scorptecproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatScorptecProductGetByIDReturnsHttpStatusNotFoundWhenProductDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scorptecproducts/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetScorptecProductByBlankIDReturnsHttpStatus400BadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scorptecproducts/{id}", " ")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    // GET TESTS (PRODUCT URLS)
    @Test
    public void testThatGetScorptecGPULinksReturnsUrlForActiveGPU() throws Exception {
        gpuService.save(gpuTestingUtility.createTestGPU());
        VendorProductDTO savedProduct = scorptecProductService.save(gpuTestingUtility.createTestScorptecGPU());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scorptecproducts/gpu-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]").value(savedProduct.getUrl())
        );
    }

    @Test
    public void testThatGetScorptecGPULinksDoesNotReturnUrlForInactiveGPU() throws Exception {
        GPUDTO gpuEntity = gpuTestingUtility.createTestGPU();
        gpuEntity.setIsActive(false);
        gpuService.save(gpuEntity);
        scorptecProductService.save(gpuTestingUtility.createTestScorptecGPU());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scorptecproducts/gpu-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$").isEmpty()
        );
    }

    @Test
    public void testThatGetScorptecCPULinksReturnsUrlForActiveCPU() throws Exception {
        cpuService.save(cpuTestingUtility.createTestCPU());
        VendorProductDTO savedProduct = scorptecProductService.save(cpuTestingUtility.createTestScorptecCPU());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scorptecproducts/cpu-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]").value(savedProduct.getUrl())
        );
    }

    @Test
    public void testThatGetScorptecCPULinksDoesNotReturnUrlForInactiveCPU() throws Exception {
        CPUDTO cpuEntity = cpuTestingUtility.createTestCPU();
        cpuEntity.setIsActive(false);
        cpuService.save(cpuEntity);
        scorptecProductService.save(cpuTestingUtility.createTestScorptecCPU());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scorptecproducts/cpu-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$").isEmpty()
        );
    }

    @Test
    public void testThatGetScorptecRAMLinksReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scorptecproducts/ram-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetScorptecWorkstationGPULinksReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scorptecproducts/workstation-gpu-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetScorptecHDDLinksReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scorptecproducts/hdd-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetScorptecSSDLinksReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scorptecproducts/ssd-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetScorptecNVMELinksReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scorptecproducts/nvme-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    // UPDATE TESTS
    @Test
    public void testThatFullUpdateScorptecProductReturnsHttpStatus200ok() throws Exception {
        VendorProductDTO testProductEntity = gpuTestingUtility.createTestScorptecGPU();
        VendorProductDTO savedProduct = scorptecProductService.save(testProductEntity);

        VendorProductDTO updatedProduct = VendorProductDTO.builder()
                .vendor(SCORPTEC)
                .productType(PRODUCT_TYPE_GPU)
                .modelNumber(TESTING_GPU_MODEL_NUMBER)
                .url("Updated product url")
                .build();
        String updatedProductString = objectMapper.writeValueAsString(updatedProduct);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/scorptecproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductString)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateReturnsUpdatedScorptecProduct() throws Exception {
        VendorProductDTO testProductEntity = gpuTestingUtility.createTestScorptecGPU();
        VendorProductDTO savedProduct = scorptecProductService.save(testProductEntity);

        VendorProductDTO updatedProduct = VendorProductDTO.builder()
                .vendor(SCORPTEC)
                .productType(PRODUCT_TYPE_GPU)
                .modelNumber("Updated model number")
                .url("Updated product url")
                .build();
        String updatedProductString = objectMapper.writeValueAsString(updatedProduct);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/scorptecproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value("Updated model number")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.url").value("Updated product url")
        );
    }

    @Test
    public void testThatFullUpdateScorptecProductReturnsHttpStatus404NotFoundForNonExistingProduct() throws Exception {
        VendorProductDTO updatedProduct = VendorProductDTO.builder()
                .vendor(SCORPTEC)
                .productType(PRODUCT_TYPE_GPU)
                .modelNumber(TESTING_GPU_MODEL_NUMBER)
                .url(SCORPTEC_ASUS_5070TI)
                .build();
        String updatedProductString = objectMapper.writeValueAsString(updatedProduct);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/scorptecproducts/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductString)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    // DELETE TESTS
    @Test
    public void testThatDeleteScorptecProductReturnsHttpStatus204FromNonExistingProduct() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/scorptecproducts/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteScorptecProductReturnsHttpStatus204ForExistingProduct() throws Exception {
        VendorProductDTO scorptecProductEntity = gpuTestingUtility.createTestScorptecGPU();
        VendorProductDTO savedProduct = scorptecProductService.save(scorptecProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/scorptecproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}