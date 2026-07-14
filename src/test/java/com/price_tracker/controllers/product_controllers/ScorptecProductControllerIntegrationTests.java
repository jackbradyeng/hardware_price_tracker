package com.price_tracker.controllers.product_controllers;

import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.testing_data.vendor_data.ScorptecTestDataUtility;
import com.price_tracker.domain.dto.vendor_dtos.VendorProductDTO;
import com.price_tracker.repositories.vendor_repos.ScorptecProductRepository;
import com.price_tracker.services.vendor_services.impl.ScorptecProductServiceImpl;
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
import static com.price_tracker.testing_data.vendor_data.VendorWebDomainNames.SCORPTEC_ASUS_5070TI;
import static com.price_tracker.testing_data.gpu_data.GPUTestingData.PRODUCT_TYPE_GPU;
import static com.price_tracker.testing_data.gpu_data.GPUTestingData.TESTING_GPU_MODEL_NUMBER;
import static com.price_tracker.constants.vendor_constants.VendorNames.SCORPTEC;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ScorptecProductControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ScorptecProductServiceImpl scorptecProductService;
    private final GPUTestingUtility gpuTestingUtility;
    private final ScorptecTestDataUtility tdl;

    @Autowired
    public ScorptecProductControllerIntegrationTests(MockMvc mockMvc,
                                                     ScorptecProductRepository scorptecProductRepository,
                                                     ModelMapper modelMapper,
                                                     MapperFactory mapperFactory,
                                                     GPUTestingUtility gpuTestingUtility,
                                                     ScorptecTestDataUtility tdl) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.scorptecProductService = new ScorptecProductServiceImpl(scorptecProductRepository, modelMapper, mapperFactory);
        this.gpuTestingUtility = gpuTestingUtility;
        this.tdl = tdl;
    }

    // CREATE TESTS
    @Test
    public void testThatCreateScorptecProductReturnsHttpStatus201Created() throws Exception {
        VendorProductDTO testProductEntity = gpuTestingUtility.createTestScorptecGPU();
        String testProductString = objectMapper.writeValueAsString(testProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/scorptecproducts")
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
                MockMvcRequestBuilders.post("/api/scorptecproducts")
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
                MockMvcRequestBuilders.post("/api/scorptecproducts/saveall")
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
                MockMvcRequestBuilders.get("/api/scorptecproducts")
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
                MockMvcRequestBuilders.get("/api/scorptecproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatScorptecProductGetByIDReturnsHttpStatusNotFoundWhenProductDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/scorptecproducts/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
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
                MockMvcRequestBuilders.put("/api/scorptecproducts/" + savedProduct.getId())
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
                MockMvcRequestBuilders.put("/api/scorptecproducts/" + savedProduct.getId())
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
                MockMvcRequestBuilders.put("/api/scorptecproducts/" + Long.MAX_VALUE)
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
                MockMvcRequestBuilders.delete("/api/scorptecproducts/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteScorptecProductReturnsHttpStatus204ForExistingProduct() throws Exception {
        VendorProductDTO scorptecProductEntity = gpuTestingUtility.createTestScorptecGPU();
        VendorProductDTO savedProduct = scorptecProductService.save(scorptecProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/scorptecproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}