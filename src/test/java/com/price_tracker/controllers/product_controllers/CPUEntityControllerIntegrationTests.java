package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.services.product_services.GenericProductService;
import com.price_tracker.testing_data.cpu_data.CPUTestingUtility;
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
import static com.price_tracker.testing_data.cpu_data.CPUTestingData.TESTING_CPU_MODEL_NUMBER;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CPUEntityControllerIntegrationTests {

    private final MockMvc mockMVC;
    private final ObjectMapper objectMapper;
    private final CPUTestingUtility cpuTestingUtility;
    private final GenericProductService<CPUDTO> cpuService;

    @Autowired
    public CPUEntityControllerIntegrationTests(MockMvc mockMVC,
                                               CPUTestingUtility cpuTestingUtility,
                                               GenericProductService<CPUDTO> cpuService) {
        this.mockMVC = mockMVC;
        this.objectMapper = new ObjectMapper();
        this.cpuTestingUtility = cpuTestingUtility;
        this.cpuService = cpuService;
    }

    /// CREATE TESTS
    @Test
    public void testThatCreateCPUReturnsHttpStatus201Created() throws Exception {
        CPUDTO testCPU = cpuTestingUtility.createTestCPU();
        String cpuString = objectMapper.writeValueAsString(testCPU);

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
        CPUDTO testCPU = cpuTestingUtility.createTestCPU();
        String cpuString = objectMapper.writeValueAsString(testCPU);

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

    /// VALIDATION WIRING TESTS
    @Test
    public void testThatCreateCPUWithInvalidFieldsReturnsHttpStatus400BadRequest() throws Exception {
        CPUDTO testCPU = cpuTestingUtility.createTestCPU();
        testCPU.setModelNumber("");
        testCPU.setCores(null);
        testCPU.setThermalDesignPower(-1);
        String cpuString = objectMapper.writeValueAsString(testCPU);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/api/cpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cpuString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateCPUWithInvalidFieldsReturnsExpectedValidationErrors() throws Exception {
        CPUDTO testCPU = cpuTestingUtility.createTestCPU();
        testCPU.setModelNumber("");
        testCPU.setCores(null);
        testCPU.setThermalDesignPower(-1);
        String cpuString = objectMapper.writeValueAsString(testCPU);

        mockMVC.perform(
                MockMvcRequestBuilders.post("/api/cpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cpuString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.modelNumber").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.cores").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.thermalDesignPower").exists()
        );
    }

    /// READ TESTS
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
        CPUDTO testCPU = cpuTestingUtility.createTestCPU();
        CPUDTO savedCPU = cpuService.save(testCPU);

        mockMVC.perform(
                MockMvcRequestBuilders.get("/api/cpus/" + savedCPU.getModelNumber())
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

    /// UPDATE TESTS
    @Test
    public void testThatFullUpdateReturns200ok() throws Exception {
        CPUDTO testCPU = cpuTestingUtility.createTestCPU();
        CPUDTO savedCPU = cpuService.save(testCPU);

        CPUDTO updatedCPU = cpuTestingUtility.createTestCPU();
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
        CPUDTO testCPU = cpuTestingUtility.createTestCPU();
        CPUDTO savedCPU = cpuService.save(testCPU);

        CPUDTO updatedCPU = cpuTestingUtility.createTestCPU();
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

    /// DELETE TESTS
    @Test
    public void testThatDeleteCPUReturnsHttpStatus204FromNonExistingCPU() throws Exception {
        mockMVC.perform(
                MockMvcRequestBuilders.delete("/api/cpus/cpuDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteCPUReturnsHttpStatus204ForExisting() throws Exception {
        CPUDTO testCPU = cpuTestingUtility.createTestCPU();
        CPUDTO savedCPU = cpuService.save(testCPU);

        mockMVC.perform(
                MockMvcRequestBuilders.delete("/api/cpus/" + savedCPU.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}