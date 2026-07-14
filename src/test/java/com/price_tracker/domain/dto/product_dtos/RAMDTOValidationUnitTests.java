package com.price_tracker.domain.dto.product_dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static com.price_tracker.testing_data.ram_data.RAMTestingData.*;

public class RAMDTOValidationUnitTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private RAMDTO.RAMDTOBuilder validRAMBuilder() {
        return RAMDTO.builder()
                .modelNumber(TESTING_RAM_MODEL_NUMBER)
                .name(TESTING_RAM_NAME)
                .brand(TESTING_RAM_BRAND)
                .standard(TESTING_RAM_STANDARD)
                .volume(TESTING_RAM_VOLUME)
                .dimmCount(TESTING_RAM_DIMM_COUNT)
                .clockRate(TESTING_RAM_CLOCK_RATE)
                .latency(TESTING_RAM_LATENCY)
                .voltage(TESTING_RAM_VOLTAGE)
                .isActive(true);
    }

    private boolean hasViolationFor(Set<ConstraintViolation<RAMDTO>> violations, String property) {
        return violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals(property));
    }

    // NAME SIZE TESTS
    @Test
    public void testThatNameAtMaxSizeIsValid() {
        RAMDTO ram = validRAMBuilder().name("x".repeat(300)).build();
        assert !hasViolationFor(validator.validate(ram), "name");
    }

    @Test
    public void testThatNameExceedingMaxSizeIsInvalid() {
        RAMDTO ram = validRAMBuilder().name("x".repeat(301)).build();
        assert hasViolationFor(validator.validate(ram), "name");
    }

    // BRAND SIZE TESTS
    @Test
    public void testThatBrandAtMaxSizeIsValid() {
        RAMDTO ram = validRAMBuilder().brand("x".repeat(50)).build();
        assert !hasViolationFor(validator.validate(ram), "brand");
    }

    @Test
    public void testThatBrandExceedingMaxSizeIsInvalid() {
        RAMDTO ram = validRAMBuilder().brand("x".repeat(51)).build();
        assert hasViolationFor(validator.validate(ram), "brand");
    }

    // DIMM COUNT TESTS
    @Test
    public void testThatDimmCountAtLimitIsValid() {
        RAMDTO ram = validRAMBuilder().dimmCount(12).build();
        assert !hasViolationFor(validator.validate(ram), "dimmCount");
    }

    @Test
    public void testThatDimmCountExceedingLimitIsInvalid() {
        RAMDTO ram = validRAMBuilder().dimmCount(13).build();
        assert hasViolationFor(validator.validate(ram), "dimmCount");
    }

    // NOT BLANK TESTS
    @Test
    public void testThatBlankModelNumberIsInvalid() {
        RAMDTO ram = validRAMBuilder().modelNumber("").build();
        assert hasViolationFor(validator.validate(ram), "modelNumber");
    }

    @Test
    public void testThatBlankNameIsInvalid() {
        RAMDTO ram = validRAMBuilder().name("").build();
        assert hasViolationFor(validator.validate(ram), "name");
    }

    @Test
    public void testThatBlankBrandIsInvalid() {
        RAMDTO ram = validRAMBuilder().brand("").build();
        assert hasViolationFor(validator.validate(ram), "brand");
    }

    @Test
    public void testThatBlankStandardIsInvalid() {
        RAMDTO ram = validRAMBuilder().standard("").build();
        assert hasViolationFor(validator.validate(ram), "standard");
    }

    @Test
    public void testThatBlankLatencyIsInvalid() {
        RAMDTO ram = validRAMBuilder().latency("").build();
        assert hasViolationFor(validator.validate(ram), "latency");
    }

    // NOT NULL TESTS
    @Test
    public void testThatNullVolumeIsInvalid() {
        RAMDTO ram = validRAMBuilder().volume(null).build();
        assert hasViolationFor(validator.validate(ram), "volume");
    }

    @Test
    public void testThatNullDimmCountIsInvalid() {
        RAMDTO ram = validRAMBuilder().dimmCount(null).build();
        assert hasViolationFor(validator.validate(ram), "dimmCount");
    }

    @Test
    public void testThatNullClockRateIsInvalid() {
        RAMDTO ram = validRAMBuilder().clockRate(null).build();
        assert hasViolationFor(validator.validate(ram), "clockRate");
    }

    @Test
    public void testThatNullVoltageIsInvalid() {
        RAMDTO ram = validRAMBuilder().voltage(null).build();
        assert hasViolationFor(validator.validate(ram), "voltage");
    }

    @Test
    public void testThatNullIsActiveIsInvalid() {
        RAMDTO ram = validRAMBuilder().isActive(null).build();
        assert hasViolationFor(validator.validate(ram), "isActive");
    }

    // NOT NEGATIVE TESTS
    @Test
    public void testThatNegativeVolumeIsInvalid() {
        RAMDTO ram = validRAMBuilder().volume(-1).build();
        assert hasViolationFor(validator.validate(ram), "volume");
    }

    @Test
    public void testThatNegativeDimmCountIsInvalid() {
        RAMDTO ram = validRAMBuilder().dimmCount(-1).build();
        assert hasViolationFor(validator.validate(ram), "dimmCount");
    }

    @Test
    public void testThatNegativeClockRateIsInvalid() {
        RAMDTO ram = validRAMBuilder().clockRate(-1).build();
        assert hasViolationFor(validator.validate(ram), "clockRate");
    }

    @Test
    public void testThatNegativeVoltageIsInvalid() {
        RAMDTO ram = validRAMBuilder().voltage(-1.0).build();
        assert hasViolationFor(validator.validate(ram), "voltage");
    }
}