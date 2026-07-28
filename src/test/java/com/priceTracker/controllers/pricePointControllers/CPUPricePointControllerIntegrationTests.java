package com.priceTracker.controllers.pricePointControllers;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.repositories.pricePointRepositories.CPUPricePointRepository;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static com.priceTracker.constants.VendorNames.SCORPTEC;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CPUPricePointControllerIntegrationTests {

    private static final String TESTING_CPU_MODEL_NUMBER = "CPU_PRICE_POINT_CONTROLLER_TEST_MODEL_NUMBER";
    private static final String TESTING_CPU_PRICE_POINT_CURRENCY = "AUD";
    private static final BigDecimal TESTING_CPU_PRICE_POINT_PRICE = new BigDecimal("360.00");
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final CPUPricePointRepository cpuPricePointRepository;

    @Autowired
    public CPUPricePointControllerIntegrationTests(MockMvc mockMvc,
                                                   CPUPricePointRepository cpuPricePointRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.cpuPricePointRepository = cpuPricePointRepository;
    }

    private GenericPricePointDTO createTestCPUPricePointDTO() {
        return GenericPricePointDTO.builder()
                .id(1L)
                .modelNumber(TESTING_CPU_MODEL_NUMBER)
                .vendor(SCORPTEC)
                .currency(TESTING_CPU_PRICE_POINT_CURRENCY)
                .price(TESTING_CPU_PRICE_POINT_PRICE)
                .scrapedAt(LocalDateTime.now())
                .build();
    }

    // CREATE TESTS (ADMIN ONLY)
    @Test
    public void testThatCreatePricePointsAsAdminReturnsHttpStatus200Ok() throws Exception {
        List<GenericPricePointDTO> testPricePoints = List.of(createTestCPUPricePointDTO());
        String testPricePointsString = objectMapper.writeValueAsString(testPricePoints);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPricePointsString)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatCreatePricePointsReturnsSavedPricePoints() throws Exception {
        List<GenericPricePointDTO> testPricePoints = List.of(createTestCPUPricePointDTO());
        String testPricePointsString = objectMapper.writeValueAsString(testPricePoints);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPricePointsString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].modelNumber").value(TESTING_CPU_MODEL_NUMBER)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].vendor").value(SCORPTEC)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].currency").value(TESTING_CPU_PRICE_POINT_CURRENCY)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].price").value(TESTING_CPU_PRICE_POINT_PRICE.doubleValue())
        );
    }

    @Test
    public void testThatCreatePricePointsPersistsPricePointsToTheDatabase() throws Exception {
        List<GenericPricePointDTO> testPricePoints = List.of(createTestCPUPricePointDTO());
        String testPricePointsString = objectMapper.writeValueAsString(testPricePoints);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPricePointsString)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

        boolean persisted = cpuPricePointRepository.findAll().stream()
                .anyMatch(pricePoint -> pricePoint.getModelNumber().equals(TESTING_CPU_MODEL_NUMBER)
                        && TESTING_CPU_PRICE_POINT_PRICE.compareTo(pricePoint.getPrice()) == 0);

        assertTrue(persisted);
    }

    @Test
    public void testThatCreateMultiplePricePointsReturnsAllSavedPricePoints() throws Exception {
        GenericPricePointDTO secondPricePoint = GenericPricePointDTO.builder()
                .id(2L)
                .modelNumber(TESTING_CPU_MODEL_NUMBER)
                .vendor(SCORPTEC)
                .currency(TESTING_CPU_PRICE_POINT_CURRENCY)
                .price(new BigDecimal("399.99"))
                .scrapedAt(LocalDateTime.now())
                .build();
        List<GenericPricePointDTO> testPricePoints = List.of(
                createTestCPUPricePointDTO(),
                secondPricePoint
        );
        String testPricePointsString = objectMapper.writeValueAsString(testPricePoints);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPricePointsString)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.length()").value(2)
        );
    }

    @Test
    public void testThatCreatePricePointsWithEmptyListReturnsHttpStatus204NoContent() throws Exception {
        String emptyListString = objectMapper.writeValueAsString(List.of());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyListString)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    // VALIDATION WIRING TESTS
    private GenericPricePointDTO createInvalidTestCPUPricePointDTO() {
        return GenericPricePointDTO.builder()
                .id(1L)
                .modelNumber("")
                .vendor(SCORPTEC)
                .currency(TESTING_CPU_PRICE_POINT_CURRENCY)
                .price(null)
                .scrapedAt(LocalDateTime.now())
                .build();
    }

    private GenericPricePointDTO createTestCPUPricePointDTOWithCurrency(String currency) {
        return GenericPricePointDTO.builder()
                .id(1L)
                .modelNumber(TESTING_CPU_MODEL_NUMBER)
                .vendor(SCORPTEC)
                .currency(currency)
                .price(TESTING_CPU_PRICE_POINT_PRICE)
                .scrapedAt(LocalDateTime.now())
                .build();
    }

    private GenericPricePointDTO createTestCPUPricePointDTOWithPrice(BigDecimal price) {
        return GenericPricePointDTO.builder()
                .id(1L)
                .modelNumber(TESTING_CPU_MODEL_NUMBER)
                .vendor(SCORPTEC)
                .currency(TESTING_CPU_PRICE_POINT_CURRENCY)
                .price(price)
                .scrapedAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void testThatCreatePricePointsWithInvalidFieldsReturnsHttpStatus400BadRequest() throws Exception {
        GenericPricePointDTO invalidPricePoint = createInvalidTestCPUPricePointDTO();
        String invalidPricePointString = objectMapper.writeValueAsString(List.of(invalidPricePoint));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPricePointString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreatePricePointsWithInvalidFieldsReturnsExpectedValidationErrors() throws Exception {
        GenericPricePointDTO invalidPricePoint = createInvalidTestCPUPricePointDTO();
        String invalidPricePointString = objectMapper.writeValueAsString(List.of(invalidPricePoint));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPricePointString)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors['createPricePoints.pricePointDTOs[0].modelNumber']").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors['createPricePoints.pricePointDTOs[0].price']").exists()
        );
    }

    @Test
    public void testThatCreatePricePointsWithCurrencyExceedingMaxLengthReturnsHttpStatus400BadRequest() throws Exception {
        GenericPricePointDTO invalidPricePoint = createTestCPUPricePointDTOWithCurrency("TOOLONGCURRENCY");
        String invalidPricePointString = objectMapper.writeValueAsString(List.of(invalidPricePoint));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPricePointString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors['createPricePoints.pricePointDTOs[0].currency']").exists()
        );
    }

    @Test
    public void testThatCreatePricePointsWithBlankCurrencyReturnsHttpStatus400BadRequest() throws Exception {
        GenericPricePointDTO invalidPricePoint = createTestCPUPricePointDTOWithCurrency("");
        String invalidPricePointString = objectMapper.writeValueAsString(List.of(invalidPricePoint));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPricePointString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors['createPricePoints.pricePointDTOs[0].currency']").exists()
        );
    }

    @Test
    public void testThatCreatePricePointsWithNegativePriceReturnsHttpStatus400BadRequest() throws Exception {
        GenericPricePointDTO invalidPricePoint = createTestCPUPricePointDTOWithPrice(new BigDecimal("-360.00"));
        String invalidPricePointString = objectMapper.writeValueAsString(List.of(invalidPricePoint));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPricePointString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors['createPricePoints.pricePointDTOs[0].price']").exists()
        );
    }

    @Test
    public void testThatCreatePricePointsWithZeroPriceReturnsHttpStatus400BadRequest() throws Exception {
        GenericPricePointDTO invalidPricePoint = createTestCPUPricePointDTOWithPrice(BigDecimal.ZERO);
        String invalidPricePointString = objectMapper.writeValueAsString(List.of(invalidPricePoint));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPricePointString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors['createPricePoints.pricePointDTOs[0].price']").exists()
        );
    }

    @Test
    public void testThatCreatePricePointsWithNullIdReturnsHttpStatus200_Ok() throws Exception {
        GenericPricePointDTO invalidPricePoint = GenericPricePointDTO.builder()
                .id(null)
                .modelNumber(TESTING_CPU_MODEL_NUMBER)
                .vendor(SCORPTEC)
                .currency(TESTING_CPU_PRICE_POINT_CURRENCY)
                .price(TESTING_CPU_PRICE_POINT_PRICE)
                .scrapedAt(LocalDateTime.now())
                .build();
        String invalidPricePointString = objectMapper.writeValueAsString(List.of(invalidPricePoint));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPricePointString)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatCreatePricePointsWithBlankVendorReturnsHttpStatus400BadRequest() throws Exception {
        GenericPricePointDTO invalidPricePoint = GenericPricePointDTO.builder()
                .id(1L)
                .modelNumber(TESTING_CPU_MODEL_NUMBER)
                .vendor("")
                .currency(TESTING_CPU_PRICE_POINT_CURRENCY)
                .price(TESTING_CPU_PRICE_POINT_PRICE)
                .scrapedAt(LocalDateTime.now())
                .build();
        String invalidPricePointString = objectMapper.writeValueAsString(List.of(invalidPricePoint));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPricePointString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors['createPricePoints.pricePointDTOs[0].vendor']").exists()
        );
    }

    @Test
    public void testThatCreatePricePointsWithNullScrapedAtReturnsHttpStatus400BadRequest() throws Exception {
        GenericPricePointDTO invalidPricePoint = GenericPricePointDTO.builder()
                .id(1L)
                .modelNumber(TESTING_CPU_MODEL_NUMBER)
                .vendor(SCORPTEC)
                .currency(TESTING_CPU_PRICE_POINT_CURRENCY)
                .price(TESTING_CPU_PRICE_POINT_PRICE)
                .scrapedAt(null)
                .build();
        String invalidPricePointString = objectMapper.writeValueAsString(List.of(invalidPricePoint));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPricePointString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors['createPricePoints.pricePointDTOs[0].scrapedAt']").exists()
        );
    }

    @Test
    public void testThatCreatePricePointsWithOneInvalidPricePointInAMixedListReturnsErrorAtCorrectIndex() throws Exception {
        GenericPricePointDTO validPricePoint = createTestCPUPricePointDTO();
        GenericPricePointDTO invalidPricePoint = createTestCPUPricePointDTOWithPrice(new BigDecimal("-1.00"));
        String testPricePointsString = objectMapper.writeValueAsString(List.of(validPricePoint, invalidPricePoint));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cpu-pricepoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPricePointsString)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors['createPricePoints.pricePointDTOs[1].price']").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors['createPricePoints.pricePointDTOs[0].price']").doesNotExist()
        );
    }
}