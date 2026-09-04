package com.priceTracker.controllers.scrapingJobControllers;

import com.priceTracker.domain.dto.scrapingJobDTOs.ScrapingJobResultDTO;
import com.priceTracker.domain.entities.scrapingJobEntities.ScrapingJobResult;
import com.priceTracker.repositories.scrapingJobRepositories.ScrapingJobResultRepository;
import com.priceTracker.services.pricePointServices.ScrapingJobService;
import com.priceTracker.testingData.scrapingJobData.ScrapingJobTestingUtility;
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
import java.util.List;
import java.util.Optional;
import static com.priceTracker.testingData.scrapingJobData.ScrapingJobTestingData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ScrapingJobControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ScrapingJobResultRepository scrapingJobResultRepository;
    private final ScrapingJobTestingUtility scrapingJobTestingUtility;
    private final ScrapingJobService scrapingJobService;

    @Autowired
    public ScrapingJobControllerIntegrationTests(MockMvc mockMvc,
                                                 ScrapingJobResultRepository scrapingJobResultRepository,
                                                 ScrapingJobTestingUtility scrapingJobTestingUtility,
                                                 ScrapingJobService scrapingJobService) {
        this.mockMvc = mockMvc;
        this.scrapingJobResultRepository = scrapingJobResultRepository;
        this.scrapingJobTestingUtility = scrapingJobTestingUtility;
        this.scrapingJobService = scrapingJobService;
    }

    /// FIND ALL TESTS
    @Test
    public void testThatFindAllReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindAllReturnsSavedScrapingJobResult() throws Exception {
        ScrapingJobResult savedResult = scrapingJobResultRepository.save(
                scrapingJobTestingUtility.createTestScrapingJobResult());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[?(@.id == " + savedResult.getId() + ")]").exists()
        );
    }

    /// FIND ONE TESTS
    @Test
    public void testThatFindOneReturnsHttpStatus200OkWhenResultExists() throws Exception {
        ScrapingJobResult savedResult = scrapingJobResultRepository.save(
                scrapingJobTestingUtility.createTestScrapingJobResult());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/" + savedResult.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindOneReturnsExpectedScrapingJobResult() throws Exception {
        ScrapingJobResult savedResult = scrapingJobResultRepository.save(
                scrapingJobTestingUtility.createTestScrapingJobResult());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/" + savedResult.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedResult.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.vendor").value(TESTING_SCRAPING_JOB_VENDOR)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productType").value(TESTING_SCRAPING_JOB_PRODUCT_TYPE)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.recordsReceived").value(TESTING_SCRAPING_JOB_RECORDS_RECEIVED)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.recordsSaved").value(TESTING_SCRAPING_JOB_RECORDS_SAVED)
        );
    }

    @Test
    public void testThatFindOneReturnsHttpStatus404NotFoundWhenResultDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/999999999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFindOneWithNonNumericIdReturnsHttpStatus400BadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/notANumber")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    /// FIND BY PRODUCT TESTS
    @Test
    public void testThatFindByProductReturnsHttpStatus200OkWhenResultsExist() throws Exception {
        scrapingJobResultRepository.save(scrapingJobTestingUtility.createTestScrapingJobResult());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/products/" + TESTING_SCRAPING_JOB_PRODUCT_TYPE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindByProductOnlyReturnsResultsMatchingProductType() throws Exception {
        scrapingJobResultRepository.save(scrapingJobTestingUtility.createTestScrapingJobResult(
                TESTING_SCRAPING_JOB_VENDOR, TESTING_SCRAPING_JOB_PRODUCT_TYPE));
        scrapingJobResultRepository.save(scrapingJobTestingUtility.createTestScrapingJobResult(
                TESTING_SCRAPING_JOB_VENDOR, TESTING_SCRAPING_JOB_OTHER_PRODUCT_TYPE));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/products/" + TESTING_SCRAPING_JOB_PRODUCT_TYPE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[?(@.productType == '" + TESTING_SCRAPING_JOB_OTHER_PRODUCT_TYPE + "')]").doesNotExist()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[?(@.productType == '" + TESTING_SCRAPING_JOB_PRODUCT_TYPE + "')]").exists()
        );
    }

    @Test
    public void testThatFindByProductWithNoMatchingResultsReturnsHttpStatus200OkWithEmptyList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/products/PRODUCT_TYPE_THAT_DOES_NOT_EXIST")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.length()").value(0)
        );
    }

    @Test
    public void testThatFindByProductWithBlankProductTypeReturnsHttpStatus400BadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/products/{productType}", " ")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatFindByProductWithBlankProductTypeReturnsExpectedValidationError() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/products/{productType}", " ")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors['findByProduct.productType']").exists()
        );
    }

    /// FIND BY VENDOR TESTS
    @Test
    public void testThatFindByVendorReturnsHttpStatus200OkWhenResultsExist() throws Exception {
        scrapingJobResultRepository.save(scrapingJobTestingUtility.createTestScrapingJobResult());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/vendors/" + TESTING_SCRAPING_JOB_VENDOR)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindByVendorOnlyReturnsResultsMatchingVendor() throws Exception {
        scrapingJobResultRepository.save(scrapingJobTestingUtility.createTestScrapingJobResult(
                TESTING_SCRAPING_JOB_VENDOR, TESTING_SCRAPING_JOB_PRODUCT_TYPE));
        scrapingJobResultRepository.save(scrapingJobTestingUtility.createTestScrapingJobResult(
                TESTING_SCRAPING_JOB_OTHER_VENDOR, TESTING_SCRAPING_JOB_PRODUCT_TYPE));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/vendors/" + TESTING_SCRAPING_JOB_VENDOR)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[?(@.vendor == '" + TESTING_SCRAPING_JOB_OTHER_VENDOR + "')]").doesNotExist()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[?(@.vendor == '" + TESTING_SCRAPING_JOB_VENDOR + "')]").exists()
        );
    }

    @Test
    public void testThatFindByVendorWithNoMatchingResultsReturnsHttpStatus200OkWithEmptyList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/vendors/VENDOR_THAT_DOES_NOT_EXIST")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.length()").value(0)
        );
    }

    @Test
    public void testThatFindByVendorWithBlankVendorReturnsHttpStatus400BadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/vendors/{vendorType}", " ")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatFindByVendorWithBlankVendorReturnsExpectedValidationError() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/scraping-job-results/vendors/{vendor}", " ")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.errors['findByVendor.vendor']").exists()
        );
    }

    /// SERVICE TESTS - saveResults
    @Test
    public void testThatSaveResultsPersistsAndReturnsScrapingJobResultDTO() {
        ScrapingJobResultDTO savedResult = scrapingJobService.saveResults(
                TESTING_SCRAPING_JOB_VENDOR, TESTING_SCRAPING_JOB_PRODUCT_TYPE,
                TESTING_SCRAPING_JOB_RECORDS_RECEIVED, TESTING_SCRAPING_JOB_RECORDS_SAVED);

        assertEquals(TESTING_SCRAPING_JOB_VENDOR, savedResult.getVendor());
        assertEquals(TESTING_SCRAPING_JOB_PRODUCT_TYPE, savedResult.getProductType());
        assertEquals(TESTING_SCRAPING_JOB_RECORDS_RECEIVED, savedResult.getRecordsReceived());
        assertEquals(TESTING_SCRAPING_JOB_RECORDS_SAVED, savedResult.getRecordsSaved());
        assertTrue(scrapingJobResultRepository.findById(savedResult.getId()).isPresent());
    }

    /// SERVICE TESTS - findResult
    @Test
    public void testThatFindResultReturnsPresentOptionalWhenResultExists() {
        ScrapingJobResult savedResult = scrapingJobResultRepository.save(
                scrapingJobTestingUtility.createTestScrapingJobResult());

        Optional<ScrapingJobResultDTO> foundResult = scrapingJobService.findResult(savedResult.getId());

        assertTrue(foundResult.isPresent());
        assertEquals(TESTING_SCRAPING_JOB_VENDOR, foundResult.get().getVendor());
        assertEquals(TESTING_SCRAPING_JOB_PRODUCT_TYPE, foundResult.get().getProductType());
    }

    @Test
    public void testThatFindResultReturnsEmptyOptionalWhenResultDoesNotExist() {
        Optional<ScrapingJobResultDTO> foundResult = scrapingJobService.findResult(999999999L);

        assertFalse(foundResult.isPresent());
    }

    /// SERVICE TESTS - findAll
    @Test
    public void testThatFindAllReturnsPresentOptionalContainingSavedResult() {
        ScrapingJobResult savedResult = scrapingJobResultRepository.save(
                scrapingJobTestingUtility.createTestScrapingJobResult());

        Optional<List<ScrapingJobResultDTO>> foundResults = scrapingJobService.findAll();

        assertTrue(foundResults.isPresent());
        assertTrue(foundResults.get().stream().anyMatch(result -> result.getId().equals(savedResult.getId())));
    }

    /// SERVICE TESTS - findByProductType
    @Test
    public void testThatFindByProductTypeReturnsOnlyResultsMatchingProductType() {
        scrapingJobResultRepository.save(scrapingJobTestingUtility.createTestScrapingJobResult(
                TESTING_SCRAPING_JOB_VENDOR, TESTING_SCRAPING_JOB_PRODUCT_TYPE));
        scrapingJobResultRepository.save(scrapingJobTestingUtility.createTestScrapingJobResult(
                TESTING_SCRAPING_JOB_VENDOR, TESTING_SCRAPING_JOB_OTHER_PRODUCT_TYPE));

        Optional<List<ScrapingJobResultDTO>> foundResults =
                scrapingJobService.findByProductType(TESTING_SCRAPING_JOB_PRODUCT_TYPE);

        assertTrue(foundResults.isPresent());
        assertTrue(foundResults.get().stream()
                .allMatch(result -> result.getProductType().equals(TESTING_SCRAPING_JOB_PRODUCT_TYPE)));
        assertTrue(foundResults.get().stream()
                .noneMatch(result -> result.getProductType().equals(TESTING_SCRAPING_JOB_OTHER_PRODUCT_TYPE)));
    }

    @Test
    public void testThatFindByProductTypeWithNoMatchesReturnsPresentOptionalWithEmptyList() {
        Optional<List<ScrapingJobResultDTO>> foundResults =
                scrapingJobService.findByProductType("PRODUCT_TYPE_THAT_DOES_NOT_EXIST");

        assertTrue(foundResults.isPresent());
        assertTrue(foundResults.get().isEmpty());
    }

    /// SERVICE TESTS - findByVendor
    @Test
    public void testThatFindByVendorReturnsOnlyResultsMatchingVendor() {
        scrapingJobResultRepository.save(scrapingJobTestingUtility.createTestScrapingJobResult(
                TESTING_SCRAPING_JOB_VENDOR, TESTING_SCRAPING_JOB_PRODUCT_TYPE));
        scrapingJobResultRepository.save(scrapingJobTestingUtility.createTestScrapingJobResult(
                TESTING_SCRAPING_JOB_OTHER_VENDOR, TESTING_SCRAPING_JOB_PRODUCT_TYPE));

        Optional<List<ScrapingJobResultDTO>> foundResults =
                scrapingJobService.findByVendor(TESTING_SCRAPING_JOB_VENDOR);

        assertTrue(foundResults.isPresent());
        assertTrue(foundResults.get().stream()
                .allMatch(result -> result.getVendor().equals(TESTING_SCRAPING_JOB_VENDOR)));
        assertTrue(foundResults.get().stream()
                .noneMatch(result -> result.getVendor().equals(TESTING_SCRAPING_JOB_OTHER_VENDOR)));
    }

    @Test
    public void testThatFindByVendorWithNoMatchesReturnsPresentOptionalWithEmptyList() {
        Optional<List<ScrapingJobResultDTO>> foundResults =
                scrapingJobService.findByVendor("VENDOR_THAT_DOES_NOT_EXIST");

        assertTrue(foundResults.isPresent());
        assertTrue(foundResults.get().isEmpty());
    }
}