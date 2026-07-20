package com.priceTracker.controllers.productControllers;

import com.priceTracker.domain.dto.productDTOs.SSDDTO;
import com.priceTracker.services.productServices.GenericProductService;
import com.priceTracker.testingData.ssdData.SSDTestingUtility;
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
import static com.priceTracker.testingData.ssdData.SSDTestingData.TESTING_SSD_MODEL_NUMBER;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SSDEntityControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SSDTestingUtility ssdTestingUtility;
    private final GenericProductService<SSDDTO> ssdService;

    @Autowired
    public SSDEntityControllerIntegrationTests(MockMvc mockMvc,
                                               SSDTestingUtility ssdTestingUtility,
                                               GenericProductService<SSDDTO> ssdService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.ssdTestingUtility = ssdTestingUtility;
        this.ssdService = ssdService;
    }

    /// CREATE TESTS
    @Test
    public void testThatCreateSSDReturnsHttpStatus201Created() throws Exception {
        SSDDTO testSSD = ssdTestingUtility.createTestSSD();
        String ssdString = objectMapper.writeValueAsString(testSSD);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/ssds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ssdString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateSSDReturnsSavedSSD() throws Exception {
        SSDDTO testSSD = ssdTestingUtility.createTestSSD();
        String ssdString = objectMapper.writeValueAsString(testSSD);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/ssds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ssdString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value(TESTING_SSD_MODEL_NUMBER)
        );
    }

    @Test
    public void testThatCreateListOfSSDsReturns201Created() throws Exception {
        List<SSDDTO> testSSDDTOs = ssdTestingUtility.createListOfSSDs();
        String listString = objectMapper.writeValueAsString(testSSDDTOs);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/ssds/saveall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(listString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    /// VALIDATION WIRING TESTS
    @Test
    public void testThatCreateSSDWithInvalidFieldsReturnsHttpStatus400BadRequest() throws Exception {
        SSDDTO testSSD = ssdTestingUtility.createTestSSD();
        testSSD.setModelNumber("");
        testSSD.setCapacity(null);
        testSSD.setSequentialRead(-1);
        String ssdString = objectMapper.writeValueAsString(testSSD);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/ssds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ssdString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateSSDWithInvalidFieldsReturnsExpectedValidationErrors() throws Exception {
        SSDDTO testSSD = ssdTestingUtility.createTestSSD();
        testSSD.setModelNumber("");
        testSSD.setCapacity(null);
        testSSD.setSequentialRead(-1);
        String ssdString = objectMapper.writeValueAsString(testSSD);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/ssds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ssdString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.modelNumber").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.capacity").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors.sequentialRead").exists()
        );
    }

    @Test
    public void testThatGetSSDByBlankIDReturnsHttpStatus400BadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/ssds/{id}", " ")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    /// READ TESTS
    @Test
    public void testThatSSDReadAllReturnsHttpStatus200ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/ssds")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetSSDByIDReturnsHttpStatusOkWhenSSDExists() throws Exception {
        SSDDTO testSSD = ssdTestingUtility.createTestSSD();
        SSDDTO savedSSD = ssdService.save(testSSD);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/ssds/" + savedSSD.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatSSDGetByIDReturnsHttpStatusNotFoundWhenSSDDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/ssds/ssdDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    /// UPDATE TESTS
    @Test
    public void testThatFullUpdateReturns200ok() throws Exception {
        SSDDTO testSSD = ssdTestingUtility.createTestSSD();
        SSDDTO savedSSD = ssdService.save(testSSD);

        SSDDTO updatedSSD = ssdTestingUtility.createTestSSD();
        updatedSSD.setName("Updated SSD name");
        String ssdJson = objectMapper.writeValueAsString(updatedSSD);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/ssds/" + savedSSD.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ssdJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateReturnsUpdatedSSD() throws Exception {
        SSDDTO testSSD = ssdTestingUtility.createTestSSD();
        SSDDTO savedSSD = ssdService.save(testSSD);

        SSDDTO updatedSSD = ssdTestingUtility.createTestSSD();
        updatedSSD.setName("Updated SSD name");
        updatedSSD.setBrand("Updated brand");
        updatedSSD.setCapacity(1000);
        updatedSSD.setStorageInterface("Updated interface");
        String ssdJson = objectMapper.writeValueAsString(updatedSSD);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/ssds/" + savedSSD.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ssdJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Updated SSD name")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.brand").value("Updated brand")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.capacity").value(1000)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.storageInterface").value("Updated interface")
        );
    }

    /// DELETE TESTS
    @Test
    public void testThatDeleteSSDReturnsHttpStatus204FromNonExistingSSD() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/ssds/ssdDoesNotExist")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteSSDReturnsHttpStatus204ForExisting() throws Exception {
        SSDDTO testSSD = ssdTestingUtility.createTestSSD();
        SSDDTO savedSSD = ssdService.save(testSSD);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/ssds/" + savedSSD.getModelNumber())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}