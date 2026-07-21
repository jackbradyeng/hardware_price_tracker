package com.priceTracker.domain.dto.productDTOs;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static com.priceTracker.testingData.cpuData.CPUTestingData.*;

public class CPUDTOValidationUnitTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private CPUDTO.CPUDTOBuilder validCPUBuilder() {
        return CPUDTO.builder()
                .modelNumber(TESTING_CPU_MODEL_NUMBER)
                .name(TESTING_CPU_NAME)
                .chipManufacturer(TESTING_CPU_CHIP_MANUFACTURER)
                .series(TESTING_CPU_CHIP_SERIES)
                .cores(TESTING_CPU_CORES)
                .threads(TESTING_CPU_THREADS)
                .baseClock(TESTING_CPU_BASE_CLOCK)
                .boostClock(TESTING_CPU_BOOST_CLOCK)
                .l1Cache(TESTING_CPU_L1_CACHE)
                .l2Cache(TESTING_CPU_L2_CACHE)
                .l3Cache(TESTING_CPU_L3_CACHE)
                .thermalDesignPower(TESTING_CPU_TDP)
                .maxTemperature(TESTING_CPU_MAX_TEMPERATURE)
                .maxMemory(TESTING_CPU_MAX_MEMORY)
                .memorySupported(TESTING_CPU_MEMORY_SUPPORTED)
                .hasIntegratedGPU(TESTING_CPU_INTEGRATED_GPU)
                .isActive(true);
    }

    private boolean hasViolationFor(Set<ConstraintViolation<CPUDTO>> violations, String property) {
        return violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals(property));
    }

    // NAME SIZE TESTS
    @Test
    public void testThatNameAtMaxSizeIsValid() {
        CPUDTO cpu = validCPUBuilder().name("x".repeat(300)).build();
        Set<ConstraintViolation<CPUDTO>> violations = validator.validate(cpu);

        assert !hasViolationFor(violations, "name");
    }

    @Test
    public void testThatNameExceedingMaxSizeIsInvalid() {
        CPUDTO cpu = validCPUBuilder().name("x".repeat(301)).build();
        Set<ConstraintViolation<CPUDTO>> violations = validator.validate(cpu);

        assert hasViolationFor(violations, "name");
    }

    // MAX TEMPERATURE TESTS
    @Test
    public void testThatMaxTemperatureAtLimitIsValid() {
        CPUDTO cpu = validCPUBuilder().maxTemperature(150).build();
        Set<ConstraintViolation<CPUDTO>> violations = validator.validate(cpu);

        assert !hasViolationFor(violations, "maxTemperature");
    }

    @Test
    public void testThatMaxTemperatureExceedingLimitIsInvalid() {
        CPUDTO cpu = validCPUBuilder().maxTemperature(151).build();
        Set<ConstraintViolation<CPUDTO>> violations = validator.validate(cpu);

        assert hasViolationFor(violations, "maxTemperature");
    }

    // MEMORY SUPPORTED SIZE TESTS
    @Test
    public void testThatMemorySupportedAtMaxSizeIsValid() {
        CPUDTO cpu = validCPUBuilder().memorySupported("x".repeat(50)).build();
        Set<ConstraintViolation<CPUDTO>> violations = validator.validate(cpu);

        assert !hasViolationFor(violations, "memorySupported");
    }

    @Test
    public void testThatMemorySupportedExceedingMaxSizeIsInvalid() {
        CPUDTO cpu = validCPUBuilder().memorySupported("x".repeat(51)).build();
        Set<ConstraintViolation<CPUDTO>> violations = validator.validate(cpu);

        assert hasViolationFor(violations, "memorySupported");
    }

    // NOT BLANK TESTS
    @Test
    public void testThatBlankModelNumberIsInvalid() {
        CPUDTO cpu = validCPUBuilder().modelNumber("").build();
        assert hasViolationFor(validator.validate(cpu), "modelNumber");
    }

    @Test
    public void testThatBlankNameIsInvalid() {
        CPUDTO cpu = validCPUBuilder().name("").build();
        assert hasViolationFor(validator.validate(cpu), "name");
    }

    @Test
    public void testThatBlankChipManufacturerIsInvalid() {
        CPUDTO cpu = validCPUBuilder().chipManufacturer("").build();
        assert hasViolationFor(validator.validate(cpu), "chipManufacturer");
    }

    @Test
    public void testThatBlankSeriesIsInvalid() {
        CPUDTO cpu = validCPUBuilder().series("").build();
        assert hasViolationFor(validator.validate(cpu), "series");
    }

    @Test
    public void testThatBlankMemorySupportedIsInvalid() {
        CPUDTO cpu = validCPUBuilder().memorySupported("").build();
        assert hasViolationFor(validator.validate(cpu), "memorySupported");
    }

    // NOT NULL TESTS
    @Test
    public void testThatNullCoresIsInvalid() {
        CPUDTO cpu = validCPUBuilder().cores(null).build();
        assert hasViolationFor(validator.validate(cpu), "cores");
    }

    @Test
    public void testThatNullThreadsIsInvalid() {
        CPUDTO cpu = validCPUBuilder().threads(null).build();
        assert hasViolationFor(validator.validate(cpu), "threads");
    }

    @Test
    public void testThatNullBaseClockIsInvalid() {
        CPUDTO cpu = validCPUBuilder().baseClock(null).build();
        assert hasViolationFor(validator.validate(cpu), "baseClock");
    }

    @Test
    public void testThatNullThermalDesignPowerIsInvalid() {
        CPUDTO cpu = validCPUBuilder().thermalDesignPower(null).build();
        assert hasViolationFor(validator.validate(cpu), "thermalDesignPower");
    }

    @Test
    public void testThatNullHasIntegratedGPUIsInvalid() {
        CPUDTO cpu = validCPUBuilder().hasIntegratedGPU(null).build();
        assert hasViolationFor(validator.validate(cpu), "hasIntegratedGPU");
    }

    @Test
    public void testThatNullIsActiveIsInvalid() {
        CPUDTO cpu = validCPUBuilder().isActive(null).build();
        assert hasViolationFor(validator.validate(cpu), "isActive");
    }

    // NOT NEGATIVE TESTS
    @Test
    public void testThatNegativeCoresIsInvalid() {
        CPUDTO cpu = validCPUBuilder().cores(-1).build();
        assert hasViolationFor(validator.validate(cpu), "cores");
    }

    @Test
    public void testThatNegativeThreadsIsInvalid() {
        CPUDTO cpu = validCPUBuilder().threads(-1).build();
        assert hasViolationFor(validator.validate(cpu), "threads");
    }

    @Test
    public void testThatNegativeBaseClockIsInvalid() {
        CPUDTO cpu = validCPUBuilder().baseClock(-1.0).build();
        assert hasViolationFor(validator.validate(cpu), "baseClock");
    }

    @Test
    public void testThatNegativeBoostClockIsInvalid() {
        CPUDTO cpu = validCPUBuilder().boostClock(-1.0).build();
        assert hasViolationFor(validator.validate(cpu), "boostClock");
    }

    @Test
    public void testThatNegativeL1CacheIsInvalid() {
        CPUDTO cpu = validCPUBuilder().l1Cache(-1.0).build();
        assert hasViolationFor(validator.validate(cpu), "l1Cache");
    }

    @Test
    public void testThatNegativeL2CacheIsInvalid() {
        CPUDTO cpu = validCPUBuilder().l2Cache(-1.0).build();
        assert hasViolationFor(validator.validate(cpu), "l2Cache");
    }

    @Test
    public void testThatNegativeL3CacheIsInvalid() {
        CPUDTO cpu = validCPUBuilder().l3Cache(-1.0).build();
        assert hasViolationFor(validator.validate(cpu), "l3Cache");
    }

    @Test
    public void testThatNegativeThermalDesignPowerIsInvalid() {
        CPUDTO cpu = validCPUBuilder().thermalDesignPower(-1).build();
        assert hasViolationFor(validator.validate(cpu), "thermalDesignPower");
    }

    @Test
    public void testThatNegativeMaxTemperatureIsInvalid() {
        CPUDTO cpu = validCPUBuilder().maxTemperature(-1).build();
        assert hasViolationFor(validator.validate(cpu), "maxTemperature");
    }

    @Test
    public void testThatNegativeMaxMemoryIsInvalid() {
        CPUDTO cpu = validCPUBuilder().maxMemory(-1).build();
        assert hasViolationFor(validator.validate(cpu), "maxMemory");
    }
}