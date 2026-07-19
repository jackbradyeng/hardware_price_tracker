package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import com.price_tracker.services.product_services.GenericProductService;
import com.price_tracker.testing_data.ram_data.RAMTestingUtility;
import static com.price_tracker.testing_data.ram_data.RAMTestingData.TESTING_RAM_MODEL_NUMBER;
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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RAMEntityControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final RAMTestingUtility ramTestingUtility;
    private final GenericProductService<RAMDTO> ramService;

    @Autowired
    public RAMEntityControllerIntegrationTests(MockMvc mockMvc,
                                               ObjectMapper objectMapper,
                                               RAMTestingUtility ramTestingUtility,
                                               GenericProductService<RAMDTO> ramService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.ramTestingUtility = ramTestingUtility;
        this.ramService = ramService;
    }

    /// CREATE TESTS
    @Test
    public void testThatCreateRAMReturnsHttpStatus201Created()  throws Exception {
        RAMDTO testRAM = ramTestingUtility.createTestRAM();
        String ramString = objectMapper.writeValueAsString(testRAM);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/ram")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ramString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateRAMReturnsSavedRAM() throws Exception {
        RAMDTO testRAM = ramTestingUtility.createTestRAM();
        String ramString = objectMapper.writeValueAsString(testRAM);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/ram")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ramString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(TESTING_RAM_MODEL_NUMBER)
        );
    }

    @Test
    public void testThatCreateListOfRAMReturns201Created() throws Exception {
        List<RAMDTO> testRAMDTOs = ramTestingUtility.createListOfRAM();
        String listString = objectMapper.writeValueAsString(testRAMDTOs);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/ram/saveall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(listString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    /// VALIDATION WIRING TESTS
    @Test
    public void testThatCreateRAMWithInvalidFieldsReturnsHttpStatus400BadRequest() throws Exception {
        RAMDTO testRAM = ramTestingUtility.createTestRAM();
        testRAM.setModelNumber("");
        testRAM.setVolume(null);
        testRAM.setDimmCount(13);
        String ramString = objectMapper.writeValueAsString(testRAM);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/ram")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ramString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateRAMWithInvalidFieldsReturnsExpectedValidationErrors() throws Exception {
        RAMDTO testRAM = ramTestingUtility.createTestRAM();
        testRAM.setModelNumber("");
        testRAM.setVolume(null);
        testRAM.setDimmCount(13);
        String ramString = objectMapper.writeValueAsString(testRAM);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/ram")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ramString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.modelNumber").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.volume").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.dimmCount").exists()
        );
    }

    @Test
    public void testThatGetRAMByBlankIDReturnsHttpStatus400BadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/ram/{id}", " ")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    /// READ TESTS
    @Test
    public void testThatRAMReadAllReturnsHttpStatus200ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/ram")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetRAMByIDReturnsHttpStatusOkWhenRAMExists() throws Exception {
        RAMDTO testRAM = ramTestingUtility.createTestRAM();
        RAMDTO savedRAM = ramService.save(testRAM);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/ram/" + savedRAM.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatRAMGetByIDReturnsHttpStatusNotFoundWhenRAMDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/ram/ramDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    /// UPDATE TESTS
    @Test
    public void testThatFullUpdateReturns200ok() throws Exception {
        RAMDTO testRAM = ramTestingUtility.createTestRAM();
        RAMDTO savedRAM = ramService.save(testRAM);

        RAMDTO updatedRAM = ramTestingUtility.createTestRAM();
        updatedRAM.setName("Updated RAM name");
        String ramJson = objectMapper.writeValueAsString(updatedRAM);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/ram/" + savedRAM.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ramJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateReturnsUpdatedRAM() throws Exception {
        RAMDTO testRAM = ramTestingUtility.createTestRAM();
        RAMDTO savedRAM = ramService.save(testRAM);

        RAMDTO updatedRAM = ramTestingUtility.createTestRAM();
        updatedRAM.setName("Updated RAM name");
        updatedRAM.setBrand("Updated brand");
        updatedRAM.setClockRate(1000);
        updatedRAM.setVolume(128);
        String ramJson = objectMapper.writeValueAsString(updatedRAM);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/ram/" + savedRAM.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ramJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Updated RAM name")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.brand").value("Updated brand")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.clockRate").value(1000)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.volume").value(128)
        );
    }

    /// DELETE TESTS
    @Test
    public void testThatDeleteRAMReturnsHttpStatus204FromNonExistingRAM() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/ram/ramDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteRAMReturnsHttpStatus204ForExisting() throws Exception {
        RAMDTO testRAM = ramTestingUtility.createTestRAM();
        RAMDTO savedRAM = ramService.save(testRAM);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/ram/" + savedRAM.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}