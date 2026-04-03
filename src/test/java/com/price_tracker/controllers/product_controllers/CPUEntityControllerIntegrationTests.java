package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.domain.entities.product_entities.CPUEntity;
import com.price_tracker.mappers.product_mappers.CPUMapper;
import com.price_tracker.services.product_services.CPUService;
import com.price_tracker.testing_data.cpu_data.CPUTestingUtility;
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
import static com.price_tracker.testing_data.cpu_data.CPUTestingData.TESTING_CPU_MODEL_NUMBER;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CPUEntityControllerIntegrationTests {

    private final MockMvc mockMVC;
    private final ObjectMapper objectMapper;
    private final CPUTestingUtility cpuTestingUtility;
    private final CPUService cpuService;
    private final CPUMapper cpuMapper;

    @Autowired
    public CPUEntityControllerIntegrationTests(MockMvc mockMVC,
                                               CPUTestingUtility cpuTestingUtility,
                                               CPUService cpuService,
                                               CPUMapper cpuMapper) {
        this.mockMVC = mockMVC;
        this.objectMapper = new ObjectMapper();
        this.cpuTestingUtility = cpuTestingUtility;
        this.cpuService = cpuService;
        this.cpuMapper = cpuMapper;
    }

    /// create tests
    @Test
    public void testThatCreateCPUReturnsHttpStatus201Created() throws Exception {
        CPUEntity testCPUEntity = cpuTestingUtility.createTestCPU();
        String cpuString = objectMapper.writeValueAsString(testCPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/api/cpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cpuString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateCPUReturnsSavedCPU() throws Exception {
        CPUEntity testCPUEntity = cpuTestingUtility.createTestCPU();
        String cpuString = objectMapper.writeValueAsString(testCPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/api/cpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cpuString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(TESTING_CPU_MODEL_NUMBER)
        );
    }

    /// read tests
    @Test
    public void testThatCPUReadAllReturnsHttpStatus200ok() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/cpus")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetCPUByIDReturnsHttpStatusOkWhenCPUExists() throws Exception {
        CPUEntity testCPUEntity = cpuTestingUtility.createTestCPU();
        CPUEntity savedCPUEntity = cpuService.save(testCPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/cpus/" + savedCPUEntity.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatCPUGetByIDReturnsHttpStatusNotFoundWhenCPUDoesNotExist() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/cpus/cpuDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    /// update tests
    @Test
    public void testThatFullUpdateReturns200ok() throws Exception {
        CPUEntity testCPUEntity = cpuTestingUtility.createTestCPU();
        CPUEntity savedCPU = cpuService.save(testCPUEntity);

        CPUDTO updatedCPU = cpuMapper.mapTo(testCPUEntity);
        updatedCPU.setName("Updated CPU name");
        String cpuJson = objectMapper.writeValueAsString(updatedCPU);

        mockMVC.perform(
                MockMvcRequestBuilders.put("/api/cpus/" + savedCPU.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cpuJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateReturnsUpdatedCPU() throws Exception {
        CPUEntity testCPUEntity = cpuTestingUtility.createTestCPU();
        CPUEntity savedCPU = cpuService.save(testCPUEntity);

        CPUDTO updatedCPU = cpuMapper.mapTo(cpuTestingUtility.createTestCPU());
        updatedCPU.setName("Updated CPU name");
        updatedCPU.setChipManufacturer("Updated CPU chip manufacturer");
        updatedCPU.setSeries("Updated CPU series");
        String cpuJson = objectMapper.writeValueAsString(updatedCPU);

        mockMVC.perform(
                MockMvcRequestBuilders.put("/api/cpus/" + savedCPU.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cpuJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Updated CPU name")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.chipManufacturer").value("Updated CPU chip manufacturer")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.series").value("Updated CPU series")
        );
    }

    /// delete tests
    @Test
    public void testThatDeleteCPUReturnsHttpStatus204FromNonExistingCPU() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.delete("/api/cpus/cpuDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteCPUReturnsHttpStatus204ForExisting() throws Exception {
        CPUEntity testCPUEntity = cpuTestingUtility.createTestCPU();
        CPUEntity savedCPUEntity = cpuService.save(testCPUEntity);

        mockMVC.perform(
                MockMvcRequestBuilders.delete("/api/cpus/" + savedCPUEntity.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
