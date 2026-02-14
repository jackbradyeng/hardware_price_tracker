package com.price_tracker.controllers;

import com.price_tracker.domain.entities.GPUEntity;
import com.price_tracker.repositories.GPURepository;
import com.price_tracker.repositories.TestDataUtility;
import com.price_tracker.services.GPUService;
import com.price_tracker.services.impl.GPUServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Autowired
    public GPUEntityControllerIntegrationTests(MockMvc mockMVC, GPURepository gpuRepository) {
        this.mockMVC = mockMVC;
        this.objectMapper = new ObjectMapper();
        this.tdl = new TestDataUtility();
        this.gpuService = new GPUServiceImpl(gpuRepository);
    }

    /// create tests
    @Test
    public void testThatCreateGPUReturnsHttpStatus200ok() throws Exception {
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
