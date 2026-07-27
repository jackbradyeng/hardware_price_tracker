package com.priceTracker.controllers.productControllers;

import com.priceTracker.domain.dto.productDTOs.GPUDTO;
import com.priceTracker.domain.dto.productDTOs.RAMDTO;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.services.productServices.GenericProductService;
import com.priceTracker.testingData.ramData.RAMTestingUtility;
import com.priceTracker.testingData.vendorData.UmartTestDataUtility;
import com.priceTracker.domain.dto.vendorDTOs.VendorProductDTO;
import com.priceTracker.repositories.vendorRepositories.UmartProductRepository;
import com.priceTracker.services.vendorServices.impl.UmartProductServiceImpl;
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
import static com.priceTracker.testingData.vendorData.VendorWebDomainNames.UMART_ASUS_5070TI;
import static com.priceTracker.testingData.gpuData.GPUTestingData.PRODUCT_TYPE_GPU;
import static com.priceTracker.testingData.gpuData.GPUTestingData.TESTING_GPU_MODEL_NUMBER;
import static com.priceTracker.constants.VendorNames.UMART;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UmartProductControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UmartProductServiceImpl umartProductService;
    private final GenericProductService<GPUDTO> gpuService;
    private final GenericProductService<RAMDTO> ramService;
    private final GPUTestingUtility gpuTestingUtility;
    private final RAMTestingUtility ramTestingUtility;
    private final UmartTestDataUtility tdl;

    @Autowired
    public UmartProductControllerIntegrationTests(MockMvc mockMvc,
                                                  UmartProductRepository umartProductRepository,
                                                  ModelMapper modelMapper,
                                                  MapperFactory mapperFactory,
                                                  GenericProductService<GPUDTO> gpuService,
                                                  GenericProductService<RAMDTO> ramService,
                                                  GPUTestingUtility gpuTestingUtility,
                                                  RAMTestingUtility ramTestingUtility,
                                                  UmartTestDataUtility tdl) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.umartProductService = new UmartProductServiceImpl(umartProductRepository, modelMapper, mapperFactory);
        this.gpuService = gpuService;
        this.ramService = ramService;
        this.gpuTestingUtility = gpuTestingUtility;
        this.ramTestingUtility = ramTestingUtility;
        this.tdl = tdl;
    }

    // CREATE TESTS
    @Test
    public void testThatCreateUmartProductReturnsHttpStatus201Created() throws Exception {
        VendorProductDTO testProductEntity = gpuTestingUtility.createTestUmartGPU();
        String testProductString = objectMapper.writeValueAsString(testProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/umartproducts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testProductString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void TestThatCreatedUmartProductReturnsSavedUmartProduct() throws Exception {
        VendorProductDTO testProductEntity = gpuTestingUtility.createTestUmartGPU();
        String testProductString = objectMapper.writeValueAsString(testProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/umartproducts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testProductString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productType").value(PRODUCT_TYPE_GPU)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(TESTING_GPU_MODEL_NUMBER)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.url").value(UMART_ASUS_5070TI)
        );
    }

    @Test
    public void testThatCreateListOfUmartProductsReturns201Created() throws Exception {
        List<VendorProductDTO> testVendorProductDTOS = tdl.createTestUmartProducts();
        String jsonString = objectMapper.writeValueAsString(testVendorProductDTOS);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/umartproducts/saveall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    // READ TESTS
    @Test
    public void testThatUmartProductReadAllReturnsHttpStatus200ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/umartproducts")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetUmartProductByIDReturnsHttpStatusOkWhenProductExists() throws Exception {
        VendorProductDTO umartProductEntity = gpuTestingUtility.createTestUmartGPU();
        VendorProductDTO savedProduct = umartProductService.save(umartProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/umartproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUmartProductGetByIDReturnsHttpStatusNotFoundWhenProductDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/umartproducts/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetUmartProductByBlankIDReturnsHttpStatus400BadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/umartproducts/{id}", " ")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    // GET TESTS (PRODUCT URLS)
    @Test
    public void testThatGetUmartGPULinksReturnsUrlForActiveGPU() throws Exception {
        gpuService.save(gpuTestingUtility.createTestGPU());
        VendorProductDTO savedProduct = umartProductService.save(gpuTestingUtility.createTestUmartGPU());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/umartproducts/gpu-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]").value(savedProduct.getUrl())
        );
    }

    @Test
    public void testThatGetUmartGPULinksDoesNotReturnUrlForInactiveGPU() throws Exception {
        GPUDTO gpuEntity = gpuTestingUtility.createTestGPU();
        gpuEntity.setIsActive(false);
        gpuService.save(gpuEntity);
        umartProductService.save(gpuTestingUtility.createTestUmartGPU());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/umartproducts/gpu-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$").isEmpty()
        );
    }

    @Test
    public void testThatGetUmartRAMLinksReturnsUrlForActiveRAM() throws Exception {
        ramService.save(ramTestingUtility.createTestRAM());
        VendorProductDTO savedProduct = umartProductService.save(ramTestingUtility.createTestUmartRAM());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/umartproducts/ram-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]").value(savedProduct.getUrl())
        );
    }

    @Test
    public void testThatGetUmartRAMLinksDoesNotReturnUrlForInactiveRAM() throws Exception {
        RAMDTO ramEntity = ramTestingUtility.createTestRAM();
        ramEntity.setIsActive(false);
        ramService.save(ramEntity);
        umartProductService.save(ramTestingUtility.createTestUmartRAM());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/umartproducts/ram-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$").isEmpty()
        );
    }

    @Test
    public void testThatGetUmartCPULinksReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/umartproducts/cpu-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetUmartWorkstationGPULinksReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/umartproducts/workstation-gpu-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetUmartHDDLinksReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/umartproducts/hdd-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetUmartSSDLinksReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/umartproducts/ssd-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetUmartNVMELinksReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/umartproducts/nvme-page-links")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    // UPDATE TESTS
    @Test
    public void testThatFullUpdateUmartProductReturnsHttpStatus200ok() throws Exception {
        VendorProductDTO testProductEntity = gpuTestingUtility.createTestUmartGPU();
        VendorProductDTO savedProduct = umartProductService.save(testProductEntity);

        VendorProductDTO updatedProduct = VendorProductDTO.builder()
                .vendor(UMART)
                .productType(PRODUCT_TYPE_GPU)
                .modelNumber(TESTING_GPU_MODEL_NUMBER)
                .url("Updated product url")
                .build();
        String updatedProductString = objectMapper.writeValueAsString(updatedProduct);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/umartproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductString)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateReturnsUpdatedUmartProduct() throws Exception {
        VendorProductDTO testProductEntity = gpuTestingUtility.createTestUmartGPU();
        VendorProductDTO savedProduct = umartProductService.save(testProductEntity);

        VendorProductDTO updatedProduct = VendorProductDTO.builder()
                .vendor(UMART)
                .productType(PRODUCT_TYPE_GPU)
                .modelNumber("Updated model number")
                .url("Updated product url")
                .build();
        String updatedProductString = objectMapper.writeValueAsString(updatedProduct);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/umartproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value("Updated model number")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.url").value("Updated product url")
        );
    }

    @Test
    public void testThatFullUpdateUmartProductReturnsHttpStatus404NotFoundForNonExistingProduct() throws Exception {
        VendorProductDTO updatedProduct = VendorProductDTO.builder()
                .vendor(UMART)
                .productType(PRODUCT_TYPE_GPU)
                .modelNumber(TESTING_GPU_MODEL_NUMBER)
                .url(UMART_ASUS_5070TI)
                .build();
        String updatedProductString = objectMapper.writeValueAsString(updatedProduct);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/umartproducts/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductString)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    // DELETE TESTS
    @Test
    public void testThatDeleteGPUReturnsHttpStatus204FromNonExistingProduct() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/umartproducts/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteGPUReturnsHttpStatus204ForExistingProduct() throws Exception {
        VendorProductDTO umartProductEntity = gpuTestingUtility.createTestUmartGPU();
        VendorProductDTO savedProduct = umartProductService.save(umartProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/umartproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}