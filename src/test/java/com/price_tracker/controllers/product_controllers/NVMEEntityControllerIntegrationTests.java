package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.NVMEDTO;
import com.price_tracker.services.product_services.GenericProductService;
import com.price_tracker.testing_data.nvme_data.NVMETestingUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static com.price_tracker.testing_data.nvme_data.NVMETestingData.TESTING_NVME_MODEL_NUMBER;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NVMEEntityControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final NVMETestingUtility nvmeTestingUtility;
    private final GenericProductService<NVMEDTO> nvmeService;

    @Autowired
    public NVMEEntityControllerIntegrationTests(MockMvc mockMvc,
                                                NVMETestingUtility nvmeTestingUtility,
                                                GenericProductService<NVMEDTO> nvmeService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.nvmeTestingUtility = nvmeTestingUtility;
        this.nvmeService = nvmeService;
    }

    /// CREATE TESTS
    @Test
    public void testThatCreateNVMEReturnsHttpStatus201Created() throws Exception {
        NVMEDTO testNVME = nvmeTestingUtility.createTestNVME();
        String nvmeString = objectMapper.writeValueAsString(testNVME);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/nvmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nvmeString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateNVMEReturnsSavedNVME() throws Exception {
        NVMEDTO testNVME = nvmeTestingUtility.createTestNVME();
        String nvmeString = objectMapper.writeValueAsString(testNVME);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/nvmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nvmeString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(TESTING_NVME_MODEL_NUMBER)
        );
    }

    @Test
    public void testThatCreateListOfNVMEsReturns201Created() throws Exception {
        List<NVMEDTO> testNVMEDTOs = nvmeTestingUtility.createListOfNVMEs();
        String listString = objectMapper.writeValueAsString(testNVMEDTOs);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/nvmes/saveall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(listString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    /// VALIDATION WIRING TESTS
    @Test
    public void testThatCreateNVMEWithInvalidFieldsReturnsHttpStatus400BadRequest() throws Exception {
        NVMEDTO testNVME = nvmeTestingUtility.createTestNVME();
        testNVME.setModelNumber("");
        testNVME.setCapacity(null);
        testNVME.setSequentialRead(-1);
        String nvmeString = objectMapper.writeValueAsString(testNVME);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/nvmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nvmeString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateNVMEWithInvalidFieldsReturnsExpectedValidationErrors() throws Exception {
        NVMEDTO testNVME = nvmeTestingUtility.createTestNVME();
        testNVME.setModelNumber("");
        testNVME.setCapacity(null);
        testNVME.setSequentialRead(-1);
        String nvmeString = objectMapper.writeValueAsString(testNVME);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/nvmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nvmeString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.modelNumber").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.capacity").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.sequentialRead").exists()
        );
    }

    /// READ TESTS
    @Test
    public void testThatNVMEReadAllReturnsHttpStatus200ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/nvmes")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetNVMEByIDReturnsHttpStatusOkWhenNVMEExists() throws Exception {
        NVMEDTO testNVME = nvmeTestingUtility.createTestNVME();
        NVMEDTO savedNVME = nvmeService.save(testNVME);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/nvmes/" + savedNVME.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatNVMEGetByIDReturnsHttpStatusNotFoundWhenNVMEDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/nvmes/nvmeDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    /// UPDATE TESTS
    @Test
    public void testThatFullUpdateReturns200ok() throws Exception {
        NVMEDTO testNVME = nvmeTestingUtility.createTestNVME();
        NVMEDTO savedNVME = nvmeService.save(testNVME);

        NVMEDTO updatedNVME = nvmeTestingUtility.createTestNVME();
        updatedNVME.setName("Updated NVME name");
        String nvmeJson = objectMapper.writeValueAsString(updatedNVME);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/nvmes/" + savedNVME.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nvmeJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateReturnsUpdatedNVME() throws Exception {
        NVMEDTO testNVME = nvmeTestingUtility.createTestNVME();
        NVMEDTO savedNVME = nvmeService.save(testNVME);

        NVMEDTO updatedNVME = nvmeTestingUtility.createTestNVME();
        updatedNVME.setName("Updated NVME name");
        updatedNVME.setBrand("Updated brand");
        updatedNVME.setCapacity(2000);
        updatedNVME.setIncludesHeatSink(true);
        String nvmeJson = objectMapper.writeValueAsString(updatedNVME);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/nvmes/" + savedNVME.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nvmeJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Updated NVME name")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.brand").value("Updated brand")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.capacity").value(2000)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.includesHeatSink").value(true)
        );
    }

    /// DELETE TESTS
    @Test
    public void testThatDeleteNVMEReturnsHttpStatus204FromNonExistingNVME() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/nvmes/nvmeDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteNVMEReturnsHttpStatus204ForExisting() throws Exception {
        NVMEDTO testNVME = nvmeTestingUtility.createTestNVME();
        NVMEDTO savedNVME = nvmeService.save(testNVME);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/nvmes/" + savedNVME.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}