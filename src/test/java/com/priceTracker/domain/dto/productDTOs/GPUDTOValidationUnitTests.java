package com.priceTracker.domain.dto.productDTOs;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static com.priceTracker.testingData.gpuData.GPUTestingData.*;

public class GPUDTOValidationUnitTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private GPUDTO.GPUDTOBuilder validGPUBuilder() {
        return GPUDTO.builder()
                .modelNumber(TESTING_GPU_MODEL_NUMBER)
                .chip(TESTING_GPU_CHIP)
                .chipManufacturer(TESTING_GPU_CHIP_MANUFACTURER)
                .boardManufacturer(TESTING_GPU_BOARD_MANUFACTURER)
                .name(TESTING_GPU_NAME)
                .isActive(true);
    }

    private boolean hasViolationFor(Set<ConstraintViolation<GPUDTO>> violations, String property) {
        return violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals(property));
    }

    // CHIP SIZE TESTS
    @Test
    public void testThatChipAtMaxSizeIsValid() {
        GPUDTO gpu = validGPUBuilder().chip("x".repeat(300)).build();
        assert !hasViolationFor(validator.validate(gpu), "chip");
    }

    @Test
    public void testThatChipExceedingMaxSizeIsInvalid() {
        GPUDTO gpu = validGPUBuilder().chip("x".repeat(301)).build();
        assert hasViolationFor(validator.validate(gpu), "chip");
    }

    // CHIP MANUFACTURER SIZE TESTS
    @Test
    public void testThatChipManufacturerAtMaxSizeIsValid() {
        GPUDTO gpu = validGPUBuilder().chipManufacturer("x".repeat(75)).build();
        assert !hasViolationFor(validator.validate(gpu), "chipManufacturer");
    }

    @Test
    public void testThatChipManufacturerExceedingMaxSizeIsInvalid() {
        GPUDTO gpu = validGPUBuilder().chipManufacturer("x".repeat(76)).build();
        assert hasViolationFor(validator.validate(gpu), "chipManufacturer");
    }

    // BOARD MANUFACTURER SIZE TESTS
    @Test
    public void testThatBoardManufacturerAtMaxSizeIsValid() {
        GPUDTO gpu = validGPUBuilder().boardManufacturer("x".repeat(75)).build();
        assert !hasViolationFor(validator.validate(gpu), "boardManufacturer");
    }

    @Test
    public void testThatBoardManufacturerExceedingMaxSizeIsInvalid() {
        GPUDTO gpu = validGPUBuilder().boardManufacturer("x".repeat(76)).build();
        assert hasViolationFor(validator.validate(gpu), "boardManufacturer");
    }

    // NAME SIZE TESTS
    @Test
    public void testThatNameAtMaxSizeIsValid() {
        GPUDTO gpu = validGPUBuilder().name("x".repeat(350)).build();
        assert !hasViolationFor(validator.validate(gpu), "name");
    }

    @Test
    public void testThatNameExceedingMaxSizeIsInvalid() {
        GPUDTO gpu = validGPUBuilder().name("x".repeat(351)).build();
        assert hasViolationFor(validator.validate(gpu), "name");
    }

    // NOT BLANK TESTS
    @Test
    public void testThatBlankModelNumberIsInvalid() {
        GPUDTO gpu = validGPUBuilder().modelNumber("").build();
        assert hasViolationFor(validator.validate(gpu), "modelNumber");
    }

    @Test
    public void testThatBlankChipIsInvalid() {
        GPUDTO gpu = validGPUBuilder().chip("").build();
        assert hasViolationFor(validator.validate(gpu), "chip");
    }

    @Test
    public void testThatBlankChipManufacturerIsInvalid() {
        GPUDTO gpu = validGPUBuilder().chipManufacturer("").build();
        assert hasViolationFor(validator.validate(gpu), "chipManufacturer");
    }

    @Test
    public void testThatBlankBoardManufacturerIsInvalid() {
        GPUDTO gpu = validGPUBuilder().boardManufacturer("").build();
        assert hasViolationFor(validator.validate(gpu), "boardManufacturer");
    }

    @Test
    public void testThatBlankNameIsInvalid() {
        GPUDTO gpu = validGPUBuilder().name("").build();
        assert hasViolationFor(validator.validate(gpu), "name");
    }

    // NOT NULL TESTS
    @Test
    public void testThatNullIsActiveIsInvalid() {
        GPUDTO gpu = validGPUBuilder().isActive(null).build();
        assert hasViolationFor(validator.validate(gpu), "isActive");
    }
}