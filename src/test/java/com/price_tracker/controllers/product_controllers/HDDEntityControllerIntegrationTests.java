package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.HDDDTO;
import com.price_tracker.services.product_services.GenericProductService;
import com.price_tracker.testing_data.hdd_data.HDDTestingUtility;
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
import static com.price_tracker.testing_data.hdd_data.HDDTestingData.TESTING_HDD_MODEL_NUMBER;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class HDDEntityControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final HDDTestingUtility hddTestingUtility;
    private final GenericProductService<HDDDTO> hddService;

    @Autowired
    public HDDEntityControllerIntegrationTests(MockMvc mockMvc,
                                               HDDTestingUtility hddTestingUtility,
                                               GenericProductService<HDDDTO> hddService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.hddTestingUtility = hddTestingUtility;
        this.hddService = hddService;
    }

    /// CREATE TESTS
    @Test
    public void testThatCreateHDDReturnsHttpStatus201Created() throws Exception {
        HDDDTO testHDD = hddTestingUtility.createTestHDD();
        String hddString = objectMapper.writeValueAsString(testHDD);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/hdds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hddString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateHDDReturnsSavedHDD() throws Exception {
        HDDDTO testHDD = hddTestingUtility.createTestHDD();
        String hddString = objectMapper.writeValueAsString(testHDD);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/hdds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hddString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(TESTING_HDD_MODEL_NUMBER)
        );
    }

    @Test
    public void testThatCreateListOfHDDsReturns201Created() throws Exception {
        List<HDDDTO> testHDDDTOs = hddTestingUtility.createListOfHDDs();
        String listString = objectMapper.writeValueAsString(testHDDDTOs);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/hdds/saveall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(listString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    /// VALIDATION WIRING TESTS
    @Test
    public void testThatCreateHDDWithInvalidFieldsReturnsHttpStatus400BadRequest() throws Exception {
        HDDDTO testHDD = hddTestingUtility.createTestHDD();
        testHDD.setModelNumber("");
        testHDD.setCapacity(null);
        testHDD.setRpm(-1);
        String hddString = objectMapper.writeValueAsString(testHDD);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/hdds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hddString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateHDDWithInvalidFieldsReturnsExpectedValidationErrors() throws Exception {
        HDDDTO testHDD = hddTestingUtility.createTestHDD();
        testHDD.setModelNumber("");
        testHDD.setCapacity(null);
        testHDD.setRpm(-1);
        String hddString = objectMapper.writeValueAsString(testHDD);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/hdds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hddString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.modelNumber").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.capacity").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.rpm").exists()
        );
    }

    @Test
    public void testThatGetHDDByBlankIDReturnsHttpStatus400BadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/hdds/{id}", " ")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    /// READ TESTS
    @Test
    public void testThatHDDReadAllReturnsHttpStatus200ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/hdds")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetHDDByIDReturnsHttpStatusOkWhenHDDExists() throws Exception {
        HDDDTO testHDD = hddTestingUtility.createTestHDD();
        HDDDTO savedHDD = hddService.save(testHDD);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/hdds/" + savedHDD.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatHDDGetByIDReturnsHttpStatusNotFoundWhenHDDDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/hdds/hddDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    /// UPDATE TESTS
    @Test
    public void testThatFullUpdateReturns200ok() throws Exception {
        HDDDTO testHDD = hddTestingUtility.createTestHDD();
        HDDDTO savedHDD = hddService.save(testHDD);

        HDDDTO updatedHDD = hddTestingUtility.createTestHDD();
        updatedHDD.setName("Updated HDD name");
        String hddJson = objectMapper.writeValueAsString(updatedHDD);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/hdds/" + savedHDD.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hddJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateReturnsUpdatedHDD() throws Exception {
        HDDDTO testHDD = hddTestingUtility.createTestHDD();
        HDDDTO savedHDD = hddService.save(testHDD);

        HDDDTO updatedHDD = hddTestingUtility.createTestHDD();
        updatedHDD.setName("Updated HDD name");
        updatedHDD.setBrand("Updated brand");
        updatedHDD.setCapacity(4000);
        updatedHDD.setFormFactor(2.5f);
        String hddJson = objectMapper.writeValueAsString(updatedHDD);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/hdds/" + savedHDD.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hddJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Updated HDD name")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.brand").value("Updated brand")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.capacity").value(4000)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.formFactor").value(2.5)
        );
    }

    /// DELETE TESTS
    @Test
    public void testThatDeleteHDDReturnsHttpStatus204FromNonExistingHDD() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/hdds/hddDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteHDDReturnsHttpStatus204ForExisting() throws Exception {
        HDDDTO testHDD = hddTestingUtility.createTestHDD();
        HDDDTO savedHDD = hddService.save(testHDD);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/hdds/" + savedHDD.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}