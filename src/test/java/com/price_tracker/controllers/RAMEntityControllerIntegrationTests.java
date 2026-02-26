package com.price_tracker.controllers;

import com.price_tracker.TestDataUtility;
import com.price_tracker.domain.entities.RAMEntity;
import com.price_tracker.mappers.impl.RAMMapper;
import com.price_tracker.services.RAMService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode =  DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RAMEntityControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TestDataUtility tdl;
    private final RAMService ramService;
    private final RAMMapper ramMapper;

    @Autowired
    public RAMEntityControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, TestDataUtility testDataUtility, RAMService ramService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.tdl = testDataUtility;
        this.ramService = ramService;
        this.ramMapper = new RAMMapper(new ModelMapper());
    }

    /// create tests
    @Test
    public void testThatCreateRAMReturnsHttpStatus201Created()  throws Exception {
        RAMEntity testRAMEntity = tdl.createTestRAM();
        String ramString = objectMapper.writeValueAsString(testRAMEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/ram")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ramString)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateRAMReturnsSavedRAM() throws Exception {
        RAMEntity testRAMEntity = tdl.createTestRAM();
        String ramString = objectMapper.writeValueAsString(testRAMEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/ram")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ramString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.modelNumber").value("KF560C36BBE2K2-64")
        );
    }
}
