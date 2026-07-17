package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.services.product_services.GenericProductService;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
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
import static com.price_tracker.testing_data.gpu_data.GPUTestingData.TESTING_GPU_MODEL_NUMBER;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GPUEntityControllerIntegrationTests {

    private final MockMvc mockMVC;
    private final ObjectMapper objectMapper;
    private final GPUTestingUtility gpuTestingUtility;
    private final GenericProductService<GPUDTO> gpuService;

    @Autowired
    public GPUEntityControllerIntegrationTests(MockMvc mockMVC,
                                               GPUTestingUtility gpuTestingUtility,
                                               GenericProductService<GPUDTO> gpuService) {
        this.mockMVC = mockMVC;
        this.objectMapper = new ObjectMapper();
        this.gpuTestingUtility = gpuTestingUtility;
        this.gpuService = gpuService;
    }

    /// CREATE TESTS
    @Test
    public void testThatCreateGPUReturnsHttpStatus201Created() throws Exception {
        GPUDTO testGPU = gpuTestingUtility.createTestGPU();
        String gpuString = objectMapper.writeValueAsString(testGPU);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/api/v1/gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateGPUReturnsSavedGPU() throws Exception {
        GPUDTO testGPU = gpuTestingUtility.createTestGPU();
        String gpuString = objectMapper.writeValueAsString(testGPU);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/api/v1/gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(TESTING_GPU_MODEL_NUMBER)
        );
    }

    @Test
    public void testThatCreateListOfGPUsReturns201Created() throws Exception {
        List<GPUDTO> testGPUDTOs = gpuTestingUtility.createListOfGPUs();
        String listString = objectMapper.writeValueAsString(testGPUDTOs);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/api/v1/gpus/saveall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(listString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    /// VALIDATION WIRING TESTS
    @Test
    public void testThatCreateGPUWithInvalidFieldsReturnsHttpStatus400BadRequest() throws Exception {
        GPUDTO testGPU = gpuTestingUtility.createTestGPU();
        testGPU.setModelNumber("");
        testGPU.setChip("");
        testGPU.setIsActive(null);
        String gpuString = objectMapper.writeValueAsString(testGPU);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/api/v1/gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateGPUWithInvalidFieldsReturnsExpectedValidationErrors() throws Exception {
        GPUDTO testGPU = gpuTestingUtility.createTestGPU();
        testGPU.setModelNumber("");
        testGPU.setChip("");
        testGPU.setIsActive(null);
        String gpuString = objectMapper.writeValueAsString(testGPU);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/api/v1/gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.modelNumber").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.chip").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.isActive").exists()
        );
    }

    @Test
    public void testThatGetGPUByBlankIDReturnsHttpStatus400BadRequest() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/v1/gpus/{id}", " ")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    /// READ TESTS
    @Test
    public void testThatGPUReadAllReturnsHttpStatus200ok() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/v1/gpus")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetGPUByIDReturnsHttpStatusOkWhenGPUExists() throws Exception {
        GPUDTO testGPU = gpuTestingUtility.createTestGPU();
        GPUDTO savedGPU = gpuService.save(testGPU);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/v1/gpus/" + savedGPU.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGPUGetByIDReturnsHttpStatusNotFoundWhenGPUDoesNotExist() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/v1/gpus/gpuDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    /// UPDATE TESTS
    @Test
    public void testThatFullUpdateReturns200ok() throws Exception {
        GPUDTO testGPU = gpuTestingUtility.createTestGPU();
        GPUDTO savedGPU = gpuService.save(testGPU);

        GPUDTO updatedGPU = gpuTestingUtility.createTestGPU();
        updatedGPU.setName("Updated GPU name");
        String gpuJson = objectMapper.writeValueAsString(updatedGPU);

        mockMVC.perform(
                MockMvcRequestBuilders.put("/api/v1/gpus/" + savedGPU.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateReturnsUpdatedGPU() throws Exception {
        GPUDTO testGPU = gpuTestingUtility.createTestGPU();
        GPUDTO savedGPU = gpuService.save(testGPU);

        GPUDTO updatedGPU = gpuTestingUtility.createTestGPU();
        updatedGPU.setName("Updated GPU name");
        updatedGPU.setChip("Updated GPU chip");
        updatedGPU.setChipManufacturer("Updated GPU chip manufacturer");
        updatedGPU.setBoardManufacturer("Updated GPU board manufacturer");
        String gpuJson = objectMapper.writeValueAsString(updatedGPU);

        mockMVC.perform(
                MockMvcRequestBuilders.put("/api/v1/gpus/" + savedGPU.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Updated GPU name")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.chip").value("Updated GPU chip")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.chipManufacturer")
                        .value("Updated GPU chip manufacturer")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.boardManufacturer").
                        value("Updated GPU board manufacturer")
        );
    }

    /// DELETE TESTS
    @Test
    public void testThatDeleteGPUReturnsHttpStatus204FromNonExistingGPU() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.delete("/api/v1/gpus/gpuDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteGPUReturnsHttpStatus204ForExisting() throws Exception {
        GPUDTO testGPU = gpuTestingUtility.createTestGPU();
        GPUDTO savedGPU = gpuService.save(testGPU);

        mockMVC.perform(
                MockMvcRequestBuilders.delete("/api/v1/gpus/" + savedGPU.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}