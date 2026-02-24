package com.price_tracker.controllers;

import com.price_tracker.TestDataUtility;
import com.price_tracker.domain.entities.UmartProductEntity;
import com.price_tracker.mappers.impl.UmartProductMapper;
import com.price_tracker.repositories.UmartProductRepository;
import com.price_tracker.services.UmartProductService;
import com.price_tracker.services.impl.UmartProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;
import static com.price_tracker.constants.WebDomainNames.UMART_ASUS_5070TI;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UmartProductControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UmartProductService umartProductService;
    private final UmartProductMapper umartProductMapper;
    private final TestDataUtility tdl;

    @Autowired
    public UmartProductControllerIntegrationTests(MockMvc mockMvc, UmartProductRepository umartProductRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.umartProductService = new UmartProductServiceImpl(umartProductRepository);
        this.umartProductMapper = new UmartProductMapper(new ModelMapper());
        this.tdl = new TestDataUtility();
    }

    // create tests
    @Test
    public void testThatCreateUmartProductReturnsHttpStatus201Created() throws Exception {
        UmartProductEntity testProductEntity = tdl.createTestUmartProduct();
        String testProductString = objectMapper.writeValueAsString(testProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/umartproducts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testProductString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void TestThatCreatedUmartProductReturnsSavedUmartProduct() throws Exception {
        UmartProductEntity testProductEntity = tdl.createTestUmartProduct();
        String testProductString = objectMapper.writeValueAsString(testProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/umartproducts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testProductString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productType").value("GPU")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value("PRIME-RTX5070TI-O16G")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.url").value(UMART_ASUS_5070TI)
        );
    }

    // read tests
    @Test
    public void testThatUmartProductReadAllReturnsHttpStatus200ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/umartproducts")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetUmartProductByIDReturnsHttpStatusOkWhenProductExists() throws Exception {
        UmartProductEntity umartProductEntity = tdl.createTestUmartProduct();
        UmartProductEntity savedProduct = umartProductService.save(umartProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/umartproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUmartProductGetByIDReturnsHttpStatusNotFoundWhenProductDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/umartproducts/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    // update tests

    // delete tests
    @Test
    public void testThatDeleteGPUReturnsHttpStatus204FromNonExistingProduct() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/umartproducts/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteGPUReturnsHttpStatus204ForExistingProduct() throws Exception {
        UmartProductEntity umartProductEntity = tdl.createTestUmartProduct();
        UmartProductEntity savedProduct = umartProductService.save(umartProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/umartproducts/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
