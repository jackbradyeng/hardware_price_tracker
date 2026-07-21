package com.priceTracker.domain.dto.productDTOs;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static com.priceTracker.testingData.nvmeData.NVMETestingData.*;

public class NVMEDTOValidationUnitTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private NVMEDTO.NVMEDTOBuilder validNVMEBuilder() {
        return NVMEDTO.builder()
                .modelNumber(TESTING_NVME_MODEL_NUMBER)
                .name(TESTING_NVME_NAME)
                .brand(TESTING_NVME_BRAND)
                .capacity(TESTING_NVME_CAPACITY)
                .sequentialRead(TESTING_NVME_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_NVME_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_NVME_MTBF)
                .storageInterface(TESTING_NVME_STORAGE_INTERFACE)
                .includesHeatSink(TESTING_NVME_INCLUDES_HEAT_SINK)
                .isActive(true);
    }

    private boolean hasViolationFor(Set<ConstraintViolation<NVMEDTO>> violations, String property) {
        return violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals(property));
    }

    // NAME SIZE TESTS
    @Test
    public void testThatNameAtMaxSizeIsValid() {
        NVMEDTO nvme = validNVMEBuilder().name("x".repeat(300)).build();
        assert !hasViolationFor(validator.validate(nvme), "name");
    }

    @Test
    public void testThatNameExceedingMaxSizeIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().name("x".repeat(301)).build();
        assert hasViolationFor(validator.validate(nvme), "name");
    }

    // BRAND SIZE TESTS
    @Test
    public void testThatBrandAtMaxSizeIsValid() {
        NVMEDTO nvme = validNVMEBuilder().brand("x".repeat(50)).build();
        assert !hasViolationFor(validator.validate(nvme), "brand");
    }

    @Test
    public void testThatBrandExceedingMaxSizeIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().brand("x".repeat(51)).build();
        assert hasViolationFor(validator.validate(nvme), "brand");
    }

    // STORAGE INTERFACE SIZE TESTS
    @Test
    public void testThatStorageInterfaceAtMaxSizeIsValid() {
        NVMEDTO nvme = validNVMEBuilder().storageInterface("x".repeat(50)).build();
        assert !hasViolationFor(validator.validate(nvme), "storageInterface");
    }

    @Test
    public void testThatStorageInterfaceExceedingMaxSizeIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().storageInterface("x".repeat(51)).build();
        assert hasViolationFor(validator.validate(nvme), "storageInterface");
    }

    // NOT BLANK TESTS
    @Test
    public void testThatBlankModelNumberIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().modelNumber("").build();
        assert hasViolationFor(validator.validate(nvme), "modelNumber");
    }

    @Test
    public void testThatBlankNameIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().name("").build();
        assert hasViolationFor(validator.validate(nvme), "name");
    }

    @Test
    public void testThatBlankBrandIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().brand("").build();
        assert hasViolationFor(validator.validate(nvme), "brand");
    }

    @Test
    public void testThatBlankStorageInterfaceIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().storageInterface("").build();
        assert hasViolationFor(validator.validate(nvme), "storageInterface");
    }

    // NOT NULL TESTS
    @Test
    public void testThatNullCapacityIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().capacity(null).build();
        assert hasViolationFor(validator.validate(nvme), "capacity");
    }

    @Test
    public void testThatNullIncludesHeatSinkIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().includesHeatSink(null).build();
        assert hasViolationFor(validator.validate(nvme), "includesHeatSink");
    }

    @Test
    public void testThatNullIsActiveIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().isActive(null).build();
        assert hasViolationFor(validator.validate(nvme), "isActive");
    }

    // NOT NEGATIVE TESTS
    @Test
    public void testThatNegativeCapacityIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().capacity(-1).build();
        assert hasViolationFor(validator.validate(nvme), "capacity");
    }

    @Test
    public void testThatNegativeSequentialReadIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().sequentialRead(-1).build();
        assert hasViolationFor(validator.validate(nvme), "sequentialRead");
    }

    @Test
    public void testThatNegativeSequentialWriteIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().sequentialWrite(-1).build();
        assert hasViolationFor(validator.validate(nvme), "sequentialWrite");
    }

    @Test
    public void testThatNegativeMeanTimeBetweenFailuresIsInvalid() {
        NVMEDTO nvme = validNVMEBuilder().meanTimeBetweenFailures(-1L).build();
        assert hasViolationFor(validator.validate(nvme), "meanTimeBetweenFailures");
    }
}