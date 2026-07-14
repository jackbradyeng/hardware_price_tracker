package com.price_tracker.controllers.product_controllers;

import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.testing_data.vendor_data.UmartTestDataUtility;
import com.price_tracker.domain.dto.vendor_dtos.VendorProductDTO;
import com.price_tracker.repositories.vendor_repos.UmartProductRepository;
import com.price_tracker.services.vendor_services.impl.UmartProductServiceImpl;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
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
import static com.price_tracker.testing_data.vendor_data.VendorWebDomainNames.UMART_ASUS_5070TI;
import static com.price_tracker.testing_data.gpu_data.GPUTestingData.PRODUCT_TYPE_GPU;
import static com.price_tracker.testing_data.gpu_data.GPUTestingData.TESTING_GPU_MODEL_NUMBER;
import static com.price_tracker.constants.vendor_constants.VendorNames.UMART;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UmartProductControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UmartProductServiceImpl umartProductService;
    private final GPUTestingUtility gpuTestingUtility;
    private final UmartTestDataUtility tdl;

    @Autowired
    public UmartProductControllerIntegrationTests(MockMvc mockMvc,
                                                  UmartProductRepository umartProductRepository,
                                                  ModelMapper modelMapper,
                                                  MapperFactory mapperFactory,
                                                  GPUTestingUtility gpuTestingUtility,
                                                  UmartTestDataUtility tdl) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.umartProductService = new UmartProductServiceImpl(umartProductRepository, modelMapper, mapperFactory);
        this.gpuTestingUtility = gpuTestingUtility;
        this.tdl = tdl;
    }

    // create tests
    @Test
    public void testThatCreateUmartProductReturnsHttpStatus201Created() throws Exception {
        VendorProductDTO testProductEntity = gpuTestingUtility.createTestUmartGPU();
        String testProductString = objectMapper.writeValueAsString(testProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/umartproducts")
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
                MockMvcRequestBuilders.post("/api/umartproducts")
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
                MockMvcRequestBuilders.post("/api/umartproducts/saveall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    // read tests
    @Test
    public void testThatUmartProductReadAllReturnsHttpStatus200ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/umartproducts")
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
                MockMvcRequestBuilders.get("/api/umartproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUmartProductGetByIDReturnsHttpStatusNotFoundWhenProductDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/umartproducts/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    // update tests
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
                MockMvcRequestBuilders.put("/api/umartproducts/" + savedProduct.getId())
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
                MockMvcRequestBuilders.put("/api/umartproducts/" + savedProduct.getId())
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
                MockMvcRequestBuilders.put("/api/umartproducts/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductString)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    // delete tests
    @Test
    public void testThatDeleteGPUReturnsHttpStatus204FromNonExistingProduct() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/umartproducts/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteGPUReturnsHttpStatus204ForExistingProduct() throws Exception {
        VendorProductDTO umartProductEntity = gpuTestingUtility.createTestUmartGPU();
        VendorProductDTO savedProduct = umartProductService.save(umartProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/umartproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}