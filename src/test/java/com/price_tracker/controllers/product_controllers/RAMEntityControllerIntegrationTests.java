package com.price_tracker.controllers.product_controllers;

import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import com.price_tracker.services.product_services.RAMService;
import com.price_tracker.testing_data.ram_data.RAMTestingUtility;
import static com.price_tracker.testing_data.ram_data.RAMTestingData.TESTING_RAM_MODEL_NUMBER;
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
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RAMEntityControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final RAMTestingUtility ramTestingUtility;
    private final RAMService ramService;

    @Autowired
    public RAMEntityControllerIntegrationTests(MockMvc mockMvc,
                                               ObjectMapper objectMapper,
                                               RAMTestingUtility ramTestingUtility,
                                               RAMService ramService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.ramTestingUtility = ramTestingUtility;
        this.ramService = ramService;
    }

    /// create tests
    @Test
    public void testThatCreateRAMReturnsHttpStatus201Created()  throws Exception {
        RAMDTO testRAM = ramTestingUtility.createTestRAM();
        String ramString = objectMapper.writeValueAsString(testRAM);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/ram")
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
                MockMvcRequestBuilders.post("/api/ram")
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
                MockMvcRequestBuilders.post("/api/ram/saveall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(listString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    /// read tests
    @Test
    public void testThatRAMReadAllReturnsHttpStatus200ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/ram")
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
                MockMvcRequestBuilders.get("/api/ram/" + savedRAM.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatRAMGetByIDReturnsHttpStatusNotFoundWhenRAMDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/ram/ramDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    /// update tests
    @Test
    public void testThatFullUpdateReturns200ok() throws Exception {
        RAMDTO testRAM = ramTestingUtility.createTestRAM();
        RAMDTO savedRAM = ramService.save(testRAM);

        RAMDTO updatedRAM = ramTestingUtility.createTestRAM();
        updatedRAM.setName("Updated RAM name");
        String ramJson = objectMapper.writeValueAsString(updatedRAM);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/ram/" + savedRAM.getModelNumber())
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
                MockMvcRequestBuilders.put("/api/ram/" + savedRAM.getModelNumber())
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

    /// delete tests
    @Test
    public void testThatDeleteRAMReturnsHttpStatus204FromNonExistingRAM() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/ram/ramDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteRAMReturnsHttpStatus204ForExisting() throws Exception {
        RAMDTO testRAM = ramTestingUtility.createTestRAM();
        RAMDTO savedRAM = ramService.save(testRAM);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/ram/" + savedRAM.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}