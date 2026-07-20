package com.priceTracker.domain.dto.productDTOs;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static com.priceTracker.testingData.ssdData.SSDTestingData.*;

public class SSDDTOValidationUnitTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private SSDDTO.SSDDTOBuilder validSSDBuilder() {
        return SSDDTO.builder()
                .modelNumber(TESTING_SSD_MODEL_NUMBER)
                .name(TESTING_SSD_NAME)
                .brand(TESTING_SSD_BRAND)
                .capacity(TESTING_SSD_CAPACITY)
                .sequentialRead(TESTING_SSD_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_SSD_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_SSD_MTBF)
                .storageInterface(TESTING_SSD_STORAGE_INTERFACE)
                .isActive(true);
    }

    private boolean hasViolationFor(Set<ConstraintViolation<SSDDTO>> violations, String property) {
        return violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals(property));
    }

    // NAME SIZE TESTS
    @Test
    public void testThatNameAtMaxSizeIsValid() {
        SSDDTO ssd = validSSDBuilder().name("x".repeat(300)).build();
        assert !hasViolationFor(validator.validate(ssd), "name");
    }

    @Test
    public void testThatNameExceedingMaxSizeIsInvalid() {
        SSDDTO ssd = validSSDBuilder().name("x".repeat(301)).build();
        assert hasViolationFor(validator.validate(ssd), "name");
    }

    // BRAND SIZE TESTS
    @Test
    public void testThatBrandAtMaxSizeIsValid() {
        SSDDTO ssd = validSSDBuilder().brand("x".repeat(70)).build();
        assert !hasViolationFor(validator.validate(ssd), "brand");
    }

    @Test
    public void testThatBrandExceedingMaxSizeIsInvalid() {
        SSDDTO ssd = validSSDBuilder().brand("x".repeat(71)).build();
        assert hasViolationFor(validator.validate(ssd), "brand");
    }

    // STORAGE INTERFACE SIZE TESTS
    @Test
    public void testThatStorageInterfaceAtMaxSizeIsValid() {
        SSDDTO ssd = validSSDBuilder().storageInterface("x".repeat(50)).build();
        assert !hasViolationFor(validator.validate(ssd), "storageInterface");
    }

    @Test
    public void testThatStorageInterfaceExceedingMaxSizeIsInvalid() {
        SSDDTO ssd = validSSDBuilder().storageInterface("x".repeat(51)).build();
        assert hasViolationFor(validator.validate(ssd), "storageInterface");
    }

    // NOT BLANK TESTS
    @Test
    public void testThatBlankModelNumberIsInvalid() {
        SSDDTO ssd = validSSDBuilder().modelNumber("").build();
        assert hasViolationFor(validator.validate(ssd), "modelNumber");
    }

    @Test
    public void testThatBlankNameIsInvalid() {
        SSDDTO ssd = validSSDBuilder().name("").build();
        assert hasViolationFor(validator.validate(ssd), "name");
    }

    @Test
    public void testThatBlankBrandIsInvalid() {
        SSDDTO ssd = validSSDBuilder().brand("").build();
        assert hasViolationFor(validator.validate(ssd), "brand");
    }

    @Test
    public void testThatBlankStorageInterfaceIsInvalid() {
        SSDDTO ssd = validSSDBuilder().storageInterface("").build();
        assert hasViolationFor(validator.validate(ssd), "storageInterface");
    }

    // NOT NULL TESTS
    @Test
    public void testThatNullCapacityIsInvalid() {
        SSDDTO ssd = validSSDBuilder().capacity(null).build();
        assert hasViolationFor(validator.validate(ssd), "capacity");
    }

    @Test
    public void testThatNullIsActiveIsInvalid() {
        SSDDTO ssd = validSSDBuilder().isActive(null).build();
        assert hasViolationFor(validator.validate(ssd), "isActive");
    }

    // NOT NEGATIVE TESTS
    @Test
    public void testThatNegativeCapacityIsInvalid() {
        SSDDTO ssd = validSSDBuilder().capacity(-1).build();
        assert hasViolationFor(validator.validate(ssd), "capacity");
    }

    @Test
    public void testThatNegativeSequentialReadIsInvalid() {
        SSDDTO ssd = validSSDBuilder().sequentialRead(-1).build();
        assert hasViolationFor(validator.validate(ssd), "sequentialRead");
    }

    @Test
    public void testThatNegativeSequentialWriteIsInvalid() {
        SSDDTO ssd = validSSDBuilder().sequentialWrite(-1).build();
        assert hasViolationFor(validator.validate(ssd), "sequentialWrite");
    }

    @Test
    public void testThatNegativeMeanTimeBetweenFailuresIsInvalid() {
        SSDDTO ssd = validSSDBuilder().meanTimeBetweenFailures(-1L).build();
        assert hasViolationFor(validator.validate(ssd), "meanTimeBetweenFailures");
    }
}