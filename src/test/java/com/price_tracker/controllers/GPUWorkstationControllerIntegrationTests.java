package com.price_tracker.controllers;

import com.price_tracker.TestDataUtility;
import com.price_tracker.domain.dto.GPUDTO;
import com.price_tracker.domain.dto.GPUWorkstationDTO;
import com.price_tracker.domain.entities.GPUEntity;
import com.price_tracker.domain.entities.GPUWorkstationEntity;
import com.price_tracker.mappers.impl.GPUWorkstationMapper;
import com.price_tracker.services.GPUWorkstationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static com.price_tracker.constants.TestingConstants.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GPUWorkstationControllerIntegrationTests {

    private final MockMvc mockMVC;
    private final ObjectMapper objectMapper;
    private final TestDataUtility tdl;
    private final GPUWorkstationService gpuWorkstationService;
    private final GPUWorkstationMapper gpuWorkstationMapper;

    @Autowired
    public GPUWorkstationControllerIntegrationTests(
            MockMvc mockMVC,
            ObjectMapper objectMapper,
            TestDataUtility tdl,
            GPUWorkstationService gpuWorkstationService,
            GPUWorkstationMapper gpuWorkstationMapper) {

        this.mockMVC = mockMVC;
        this.objectMapper = objectMapper;
        this.tdl = tdl;
        this.gpuWorkstationService = gpuWorkstationService;
        this.gpuWorkstationMapper = gpuWorkstationMapper;
    }

    /// create tests
    @Test
    public void testThatCreateWSGPUReturnsHttpStatus201Created() throws Exception {
        GPUWorkstationEntity testGPUEntity = tdl.createTestWorkstationGPU();
        String gpuString = objectMapper.writeValueAsString(testGPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/workstaion_gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateWSGPUReturnsSavedGPU() throws Exception {
        GPUWorkstationEntity testGPUEntity = tdl.createTestWorkstationGPU();
        String gpuString = objectMapper.writeValueAsString(testGPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/workstaion_gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(TESTING_WS_GPU_MODEL_NUMBER)
        );
    }

    /// read tests
    @Test
    public void testThatWSGPUReadAllReturnsHttpStatus200ok() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/workstation_gpus")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetWSGPUByIDReturnsHttpStatusOkWhenGPUExists() throws Exception {
        GPUWorkstationEntity testGPUEntity = tdl.createTestWorkstationGPU();
        GPUWorkstationDTO savedGPUEntity = gpuWorkstationService.save(gpuWorkstationMapper.mapTo(testGPUEntity));

        mockMVC.perform(
                MockMvcRequestBuilders.get("/workstation_gpus/" + savedGPUEntity.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatWSGPUGetByIDReturnsHttpStatusNotFoundWhenGPUDoesNotExist() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/workstation_gpus/gpuDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    /// update tests
    @Test
    public void testThatFullUpdateReturns200ok() throws Exception {
        GPUWorkstationEntity testGPUEntity = tdl.createTestWorkstationGPU();
        GPUWorkstationDTO savedGPUEntity = gpuWorkstationService.save(gpuWorkstationMapper.mapTo(testGPUEntity));

        savedGPUEntity.setName("Updated GPU name");
        String gpuJson = objectMapper.writeValueAsString(savedGPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.put("/workstation_gpus/" + savedGPUEntity.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateReturnsUpdatedWSGPU() throws Exception {
        GPUWorkstationEntity testGPUEntity = tdl.createTestWorkstationGPU();
        GPUWorkstationDTO savedGPUEntity = gpuWorkstationService.save(gpuWorkstationMapper.mapTo(testGPUEntity));
        savedGPUEntity.setName("Updated GPU name");
        savedGPUEntity.setChipManufacturer("Updated GPU chip manufacturer");
        savedGPUEntity.setGpuMemory(32);
        String gpuJson = objectMapper.writeValueAsString(savedGPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.put("/workstation_gpus/" + savedGPUEntity.getModelNumber())
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

    /// delete tests
    @Test
    public void testThatDeleteWSGPUReturnsHttpStatus204FromNonExistingGPU() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.delete("/workstation_gpus/gpuDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteWSGPUReturnsHttpStatus204ForExisting() throws Exception {
        GPUWorkstationEntity testGPUEntity = tdl.createTestWorkstationGPU();
        GPUWorkstationDTO savedGPUEntity = gpuWorkstationService.save(gpuWorkstationMapper.mapTo(testGPUEntity));

        mockMVC.perform(
                MockMvcRequestBuilders.delete("/workstation_gpus/" + savedGPUEntity.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
