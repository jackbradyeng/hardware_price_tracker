package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.GPUWorkstationDTO;
import com.price_tracker.domain.entities.product_entities.GPUWorkstationEntity;
import com.price_tracker.services.product_services.GenericProductService;
import com.price_tracker.testing_data.wsgpu_data.WorkstationGPUTestingUtility;
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
import static com.price_tracker.testing_data.wsgpu_data.WorkstationGPUTestingData.TESTING_WS_GPU_MODEL_NUMBER;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GPUWorkstationControllerIntegrationTests {

    private final MockMvc mockMVC;
    private final ObjectMapper objectMapper;
    private final WorkstationGPUTestingUtility workstationGPUTestingUtility;
    private final GenericProductService<GPUWorkstationDTO> gpuWorkstationService;

    @Autowired
    public GPUWorkstationControllerIntegrationTests(
            MockMvc mockMVC,
            ObjectMapper objectMapper,
            WorkstationGPUTestingUtility workstationGPUTestingUtility,
            GenericProductService<GPUWorkstationDTO> gpuWorkstationService) {

        this.mockMVC = mockMVC;
        this.objectMapper = objectMapper;
        this.workstationGPUTestingUtility = workstationGPUTestingUtility;
        this.gpuWorkstationService = gpuWorkstationService;
    }

    /// CREATE TESTS
    @Test
    public void testThatCreateWSGPUReturnsHttpStatus201Created() throws Exception {
        GPUWorkstationEntity testGPUEntity = workstationGPUTestingUtility.createTestWorkstationGPU();
        String gpuString = objectMapper.writeValueAsString(testGPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/api/workstation_gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateWSGPUReturnsSavedGPU() throws Exception {
        GPUWorkstationEntity testGPUEntity = workstationGPUTestingUtility.createTestWorkstationGPU();
        String gpuString = objectMapper.writeValueAsString(testGPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/api/workstation_gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(TESTING_WS_GPU_MODEL_NUMBER)
        );
    }

    /// VALIDATION WIRING TESTS
    @Test
    public void testThatCreateWSGPUWithInvalidFieldsReturnsHttpStatus400BadRequest() throws Exception {
        GPUWorkstationDTO testGPUDTO = workstationGPUTestingUtility.createTestWorkstationGPUDTO();
        testGPUDTO.setModelNumber("");
        testGPUDTO.setGpuMemory(null);
        testGPUDTO.setMaxPower(-1);
        String gpuJson = objectMapper.writeValueAsString(testGPUDTO);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/api/workstation_gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateWSGPUWithInvalidFieldsReturnsExpectedValidationErrors() throws Exception {
        GPUWorkstationDTO testGPUDTO = workstationGPUTestingUtility.createTestWorkstationGPUDTO();
        testGPUDTO.setModelNumber("");
        testGPUDTO.setGpuMemory(null);
        testGPUDTO.setMaxPower(-1);
        String gpuJson = objectMapper.writeValueAsString(testGPUDTO);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/api/workstation_gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.modelNumber").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.gpuMemory").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.maxPower").exists()
        );
    }

    /// READ TESTS
    @Test
    public void testThatWSGPUReadAllReturnsHttpStatus200ok() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/workstation_gpus")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetWSGPUByIDReturnsHttpStatusOkWhenGPUExists() throws Exception {
        GPUWorkstationDTO testGPUDTO = workstationGPUTestingUtility.createTestWorkstationGPUDTO();
        GPUWorkstationDTO savedGPUEntity = gpuWorkstationService.save(testGPUDTO);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/workstation_gpus/" + savedGPUEntity.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatWSGPUGetByIDReturnsHttpStatusNotFoundWhenGPUDoesNotExist() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/workstation_gpus/gpuDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    /// UPDATE TESTS
    @Test
    public void testThatFullUpdateReturns200ok() throws Exception {
        GPUWorkstationDTO testGPUDTO = workstationGPUTestingUtility.createTestWorkstationGPUDTO();
        GPUWorkstationDTO savedGPUEntity = gpuWorkstationService.save(testGPUDTO);

        savedGPUEntity.setName("Updated GPU name");
        String gpuJson = objectMapper.writeValueAsString(savedGPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.put("/api/workstation_gpus/" + savedGPUEntity.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateReturnsUpdatedWSGPU() throws Exception {
        GPUWorkstationDTO testGPUDTO = workstationGPUTestingUtility.createTestWorkstationGPUDTO();
        GPUWorkstationDTO savedGPUEntity = gpuWorkstationService.save(testGPUDTO);
        savedGPUEntity.setName("Updated GPU name");
        savedGPUEntity.setChipManufacturer("Updated GPU chip manufacturer");
        savedGPUEntity.setGpuMemory(32);
        String gpuJson = objectMapper.writeValueAsString(savedGPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.put("/api/workstation_gpus/" + savedGPUEntity.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name")
                        .value("Updated GPU name")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.chipManufacturer")
                        .value("Updated GPU chip manufacturer")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.gpuMemory").
                        value(32)
        );
    }

    /// DELETE TESTS
    @Test
    public void testThatDeleteWSGPUReturnsHttpStatus204FromNonExistingGPU() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.delete("/api/workstation_gpus/gpuDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteWSGPUReturnsHttpStatus204ForExisting() throws Exception {
        GPUWorkstationDTO testGPUDTO = workstationGPUTestingUtility.createTestWorkstationGPUDTO();
        GPUWorkstationDTO savedGPUEntity = gpuWorkstationService.save(testGPUDTO);

        mockMVC.perform(
                MockMvcRequestBuilders.delete("/api/workstation_gpus/" + savedGPUEntity.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}