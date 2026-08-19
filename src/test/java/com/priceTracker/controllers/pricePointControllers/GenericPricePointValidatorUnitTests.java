package com.priceTracker.controllers.pricePointControllers;

import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.services.pricePointServices.GenericPricePointValidator;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import static com.priceTracker.constants.VendorNames.SCORPTEC;
import static com.priceTracker.constants.VendorNames.UMART;
import static com.priceTracker.testingData.cpuData.CPUTestingData.TESTING_CPU_MODEL_NUMBER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenericPricePointValidatorUnitTests {

    private final GenericPricePointValidator validator = new GenericPricePointValidator() {};

    private GenericPricePointDTO createTestCPUPricePointDTO(String vendor, String productType) {
        return GenericPricePointDTO.builder()
                .id(1L)
                .productType(productType)
                .modelNumber(TESTING_CPU_MODEL_NUMBER)
                .vendor(vendor)
                .currency("AUD")
                .price(new BigDecimal("360.00"))
                .scrapedAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void testThatValidatePricePointDTOsWithMatchingVendorAndProductTypeReturnsTrue() {
        List<GenericPricePointDTO> pricePointDTOs = List.of(
                createTestCPUPricePointDTO(SCORPTEC, "CPU"),
                createTestCPUPricePointDTO(SCORPTEC, "CPU")
        );

        assertTrue(validator.validatePricePointDTOs(pricePointDTOs));
    }

    @Test
    public void testThatValidatePricePointDTOsWithSingleElementReturnsTrue() {
        List<GenericPricePointDTO> pricePointDTOs = List.of(createTestCPUPricePointDTO(SCORPTEC, "CPU"));

        assertTrue(validator.validatePricePointDTOs(pricePointDTOs));
    }

    @Test
    public void testThatValidatePricePointDTOsWithMismatchedVendorReturnsFalse() {
        List<GenericPricePointDTO> pricePointDTOs = List.of(
                createTestCPUPricePointDTO(SCORPTEC, "CPU"),
                createTestCPUPricePointDTO(UMART, "CPU")
        );

        assertFalse(validator.validatePricePointDTOs(pricePointDTOs));
    }

    @Test
    public void testThatValidatePricePointDTOsWithMismatchedProductTypeReturnsFalse() {
        List<GenericPricePointDTO> pricePointDTOs = List.of(
                createTestCPUPricePointDTO(SCORPTEC, "CPU"),
                createTestCPUPricePointDTO(SCORPTEC, "GPU")
        );

        assertFalse(validator.validatePricePointDTOs(pricePointDTOs));
    }

    @Test
    public void testThatValidatePricePointDTOsWithMismatchOnLastElementReturnsFalse() {
        List<GenericPricePointDTO> pricePointDTOs = List.of(
                createTestCPUPricePointDTO(SCORPTEC, "CPU"),
                createTestCPUPricePointDTO(SCORPTEC, "CPU"),
                createTestCPUPricePointDTO(UMART, "CPU")
        );

        assertFalse(validator.validatePricePointDTOs(pricePointDTOs));
    }

    @Test
    public void testThatValidatePricePointDTOsWithEmptyListThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class,
                () -> validator.validatePricePointDTOs(List.of()));
    }
}