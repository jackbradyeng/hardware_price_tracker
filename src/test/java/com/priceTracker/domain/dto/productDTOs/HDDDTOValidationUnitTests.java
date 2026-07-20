package com.priceTracker.domain.dto.productDTOs;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static com.priceTracker.testingData.hddData.HDDTestingData.*;

public class HDDDTOValidationUnitTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private HDDDTO.HDDDTOBuilder validHDDBuilder() {
        return HDDDTO.builder()
                .modelNumber(TESTING_HDD_MODEL_NUMBER)
                .name(TESTING_HDD_NAME)
                .brand(TESTING_HDD_BRAND)
                .capacity(TESTING_HDD_CAPACITY)
                .sequentialRead(TESTING_HDD_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_HDD_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_HDD_MTBF)
                .rpm(TESTING_HDD_RPM)
                .cache(TESTING_HDD_CACHE)
                .storageInterface(TESTING_HDD_STORAGE_INTERFACE)
                .formFactor(TESTING_HDD_FORM_FACTOR)
                .isActive(true);
    }

    private boolean hasViolationFor(Set<ConstraintViolation<HDDDTO>> violations, String property) {
        return violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals(property));
    }

    // NAME SIZE TESTS
    @Test
    public void testThatNameAtMaxSizeIsValid() {
        HDDDTO hdd = validHDDBuilder().name("x".repeat(300)).build();
        assert !hasViolationFor(validator.validate(hdd), "name");
    }

    @Test
    public void testThatNameExceedingMaxSizeIsInvalid() {
        HDDDTO hdd = validHDDBuilder().name("x".repeat(301)).build();
        assert hasViolationFor(validator.validate(hdd), "name");
    }

    // BRAND SIZE TESTS
    @Test
    public void testThatBrandAtMaxSizeIsValid() {
        HDDDTO hdd = validHDDBuilder().brand("x".repeat(75)).build();
        assert !hasViolationFor(validator.validate(hdd), "brand");
    }

    @Test
    public void testThatBrandExceedingMaxSizeIsInvalid() {
        HDDDTO hdd = validHDDBuilder().brand("x".repeat(76)).build();
        assert hasViolationFor(validator.validate(hdd), "brand");
    }

    // STORAGE INTERFACE SIZE TESTS
    @Test
    public void testThatStorageInterfaceAtMaxSizeIsValid() {
        HDDDTO hdd = validHDDBuilder().storageInterface("x".repeat(50)).build();
        assert !hasViolationFor(validator.validate(hdd), "storageInterface");
    }

    @Test
    public void testThatStorageInterfaceExceedingMaxSizeIsInvalid() {
        HDDDTO hdd = validHDDBuilder().storageInterface("x".repeat(51)).build();
        assert hasViolationFor(validator.validate(hdd), "storageInterface");
    }

    // FORM FACTOR RANGE TESTS
    @Test
    public void testThatFormFactorAtLowerLimitIsValid() {
        HDDDTO hdd = validHDDBuilder().formFactor(2.50f).build();
        assert !hasViolationFor(validator.validate(hdd), "formFactor");
    }

    @Test
    public void testThatFormFactorBelowLowerLimitIsInvalid() {
        HDDDTO hdd = validHDDBuilder().formFactor(2.49f).build();
        assert hasViolationFor(validator.validate(hdd), "formFactor");
    }

    @Test
    public void testThatFormFactorAtUpperLimitIsValid() {
        HDDDTO hdd = validHDDBuilder().formFactor(5.00f).build();
        assert !hasViolationFor(validator.validate(hdd), "formFactor");
    }

    @Test
    public void testThatFormFactorAboveUpperLimitIsInvalid() {
        HDDDTO hdd = validHDDBuilder().formFactor(5.01f).build();
        assert hasViolationFor(validator.validate(hdd), "formFactor");
    }

    // NOT BLANK TESTS
    @Test
    public void testThatBlankModelNumberIsInvalid() {
        HDDDTO hdd = validHDDBuilder().modelNumber("").build();
        assert hasViolationFor(validator.validate(hdd), "modelNumber");
    }

    @Test
    public void testThatBlankNameIsInvalid() {
        HDDDTO hdd = validHDDBuilder().name("").build();
        assert hasViolationFor(validator.validate(hdd), "name");
    }

    @Test
    public void testThatBlankBrandIsInvalid() {
        HDDDTO hdd = validHDDBuilder().brand("").build();
        assert hasViolationFor(validator.validate(hdd), "brand");
    }

    @Test
    public void testThatBlankStorageInterfaceIsInvalid() {
        HDDDTO hdd = validHDDBuilder().storageInterface("").build();
        assert hasViolationFor(validator.validate(hdd), "storageInterface");
    }

    // NOT NULL TESTS
    @Test
    public void testThatNullCapacityIsInvalid() {
        HDDDTO hdd = validHDDBuilder().capacity(null).build();
        assert hasViolationFor(validator.validate(hdd), "capacity");
    }

    @Test
    public void testThatNullRpmIsInvalid() {
        HDDDTO hdd = validHDDBuilder().rpm(null).build();
        assert hasViolationFor(validator.validate(hdd), "rpm");
    }

    @Test
    public void testThatNullFormFactorIsInvalid() {
        HDDDTO hdd = validHDDBuilder().formFactor(null).build();
        assert hasViolationFor(validator.validate(hdd), "formFactor");
    }

    @Test
    public void testThatNullIsActiveIsInvalid() {
        HDDDTO hdd = validHDDBuilder().isActive(null).build();
        assert hasViolationFor(validator.validate(hdd), "isActive");
    }

    // NOT NEGATIVE TESTS
    @Test
    public void testThatNegativeCapacityIsInvalid() {
        HDDDTO hdd = validHDDBuilder().capacity(-1).build();
        assert hasViolationFor(validator.validate(hdd), "capacity");
    }

    @Test
    public void testThatNegativeSequentialReadIsInvalid() {
        HDDDTO hdd = validHDDBuilder().sequentialRead(-1).build();
        assert hasViolationFor(validator.validate(hdd), "sequentialRead");
    }

    @Test
    public void testThatNegativeSequentialWriteIsInvalid() {
        HDDDTO hdd = validHDDBuilder().sequentialWrite(-1).build();
        assert hasViolationFor(validator.validate(hdd), "sequentialWrite");
    }

    @Test
    public void testThatNegativeMeanTimeBetweenFailuresIsInvalid() {
        HDDDTO hdd = validHDDBuilder().meanTimeBetweenFailures(-1L).build();
        assert hasViolationFor(validator.validate(hdd), "meanTimeBetweenFailures");
    }

    @Test
    public void testThatNegativeRpmIsInvalid() {
        HDDDTO hdd = validHDDBuilder().rpm(-1).build();
        assert hasViolationFor(validator.validate(hdd), "rpm");
    }

    @Test
    public void testThatNegativeCacheIsInvalid() {
        HDDDTO hdd = validHDDBuilder().cache(-1).build();
        assert hasViolationFor(validator.validate(hdd), "cache");
    }
}