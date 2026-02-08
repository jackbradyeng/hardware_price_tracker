package com.price_tracker.controllers;

import com.price_tracker.domain.entities.GPU;
import com.price_tracker.repositories.TestDataUtility;
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
public class GPUControllerIntegrationTests {

    private MockMvc mockMVC;
    private ObjectMapper objectMapper;
    private TestDataUtility tdl;

    @Autowired
    public GPUControllerIntegrationTests(MockMvc mockMVC) {
        this.mockMVC = mockMVC;
        this.objectMapper = new ObjectMapper();
        this.tdl = new TestDataUtility();
    }

    @Test
    public void testThatCreateGPUReturnsSuccessful() throws Exception {
        GPU testGPU = tdl.createTestGPU();
        String gpuString = objectMapper.writeValueAsString(testGPU);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gpuString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }
}
