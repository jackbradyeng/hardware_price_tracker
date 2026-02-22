package com.price_tracker.controllers;

import com.price_tracker.domain.dto.GPUDTO;
import com.price_tracker.domain.entities.GPUEntity;
import com.price_tracker.mappers.impl.GPUMapper;
import com.price_tracker.repositories.GPURepository;
import com.price_tracker.repositories.TestDataUtility;
import com.price_tracker.services.GPUService;
import com.price_tracker.services.impl.GPUServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class GPUEntityControllerIntegrationTests {

    private final MockMvc mockMVC;
    private final ObjectMapper objectMapper;
    private final TestDataUtility tdl;
    private final GPUService gpuService;
    private final GPUMapper gpuMapper;

    @Autowired
    public GPUEntityControllerIntegrationTests(MockMvc mockMVC, GPURepository gpuRepository) {
        this.mockMVC = mockMVC;
        this.objectMapper = new ObjectMapper();
        this.tdl = new TestDataUtility();
        this.gpuService = new GPUServiceImpl(gpuRepository);
        this.gpuMapper = new GPUMapper(new ModelMapper());
    }

    /// create tests
    @Test
    public void testThatCreateGPUReturnsHttpStatus201Created() throws Exception {
        GPUEntity testGPUEntity = tdl.createTestGPU();
        String gpuString = objectMapper.writeValueAsString(testGPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateGPUReturnsSavedGPU() throws Exception {
        GPUEntity testGPUEntity = tdl.createTestGPU();
        String gpuString = objectMapper.writeValueAsString(testGPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value("PRIME-RTX5070TI-O16G")
        );
    }

    /// read tests
    @Test
    public void testThatGPUReadAllReturnsHttpStatus200ok() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/gpus")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetGPUByIDReturnsHttpStatusOkWhenGPUExists() throws Exception {
        GPUEntity testGPUEntity = tdl.createTestGPU();
        GPUEntity savedGPUEntity = gpuService.save(testGPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/gpus/" + savedGPUEntity.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGPUGetByIDReturnsHttpStatusNotFoundWhenGPUDoesNotExist() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/gpus/gpuDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    /// update tests
    @Test
    public void testThatFullUpdateReturns200ok() throws Exception {
        GPUEntity testGPUENtity = tdl.createTestGPU();
        GPUEntity savedGPU = gpuService.save(testGPUENtity);

        GPUDTO updatedGPU = gpuMapper.mapTo(tdl.createTestGPU());
        updatedGPU.setName("Updated GPU name");
        String gpuJson = objectMapper.writeValueAsString(updatedGPU);

        mockMVC.perform(
                MockMvcRequestBuilders.put("/gpus/" + savedGPU.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }


    @Test
    public void testThatFullUpdateReturnsUpdatedGPU() throws Exception {
        GPUEntity testGPUENtity = tdl.createTestGPU();
        GPUEntity savedGPU = gpuService.save(testGPUENtity);

        GPUDTO updatedGPU = gpuMapper.mapTo(tdl.createTestGPU());
        updatedGPU.setName("Updated GPU name");
        updatedGPU.setChip("Updated GPU chip");
        updatedGPU.setChipManufacturer("Updated GPU chip manufacturer");
        updatedGPU.setBoardManufacturer("Updated GPU board manufacturer");
        String gpuJson = objectMapper.writeValueAsString(updatedGPU);

        mockMVC.perform(
                MockMvcRequestBuilders.put("/gpus/" + savedGPU.getModelNumber())
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

    /// delete tests
    @Test
    public void testThatDeleteGPUReturnsHttpStatus204FromNonExistingGPU() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.delete("/gpus/gpuDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteGPUReturnsHttpStatus204ForExisting() throws Exception {
        GPUEntity testGPUEntity = tdl.createTestGPU();
        GPUEntity savedGPUEntity = gpuService.save(testGPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.delete("/gpus/" + savedGPUEntity.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
