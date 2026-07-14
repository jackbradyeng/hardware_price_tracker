package com.price_tracker.domain.dto.product_dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static com.price_tracker.testing_data.wsgpu_data.WorkstationGPUTestingData.*;

public class GPUWorkstationDTOValidationUnitTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private GPUWorkstationDTO.GPUWorkstationDTOBuilder validGPUWorkstationBuilder() {
        return GPUWorkstationDTO.builder()
                .modelNumber(TESTING_WS_GPU_MODEL_NUMBER)
                .name(TESTING_WS_GPU_NAME)
                .chipManufacturer(TESTING_WS_GPU_CHIP_MANUFACTURER)
                .gpuMemory(TESTING_WS_GPU_MEMORY)
                .memoryInterface(TESTING_WS_GPU_MEMORY_INTERFACE)
                .memoryBandwidth(TESTING_WS_GPU_MEMORY_BANDWIDTH)
                .cudaCores(TESTING_WS_GPU_CUDA_CORES)
                .tensorCores(TESTING_WS_GPU_TENSOR_CORES)
                .raytracingCores(TESTING_WS_GPU_RT_CORES)
                .maxPower(TESTING_WS_GPU_MAX_POWER)
                .systemInterface(TESTING_WS_GPU_SYS_INTERFACE)
                .isActive(true);
    }

    private boolean hasViolationFor(Set<ConstraintViolation<GPUWorkstationDTO>> violations, String property) {
        return violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals(property));
    }

    // NAME SIZE TESTS
    @Test
    public void testThatNameAtMaxSizeIsValid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().name("x".repeat(300)).build();
        assert !hasViolationFor(validator.validate(gpu), "name");
    }

    @Test
    public void testThatNameExceedingMaxSizeIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().name("x".repeat(301)).build();
        assert hasViolationFor(validator.validate(gpu), "name");
    }

    // CHIP MANUFACTURER SIZE TESTS
    @Test
    public void testThatChipManufacturerAtMaxSizeIsValid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().chipManufacturer("x".repeat(75)).build();
        assert !hasViolationFor(validator.validate(gpu), "chipManufacturer");
    }

    @Test
    public void testThatChipManufacturerExceedingMaxSizeIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().chipManufacturer("x".repeat(76)).build();
        assert hasViolationFor(validator.validate(gpu), "chipManufacturer");
    }

    // SYSTEM INTERFACE SIZE TESTS
    @Test
    public void testThatSystemInterfaceAtMaxSizeIsValid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().systemInterface("x".repeat(50)).build();
        assert !hasViolationFor(validator.validate(gpu), "systemInterface");
    }

    @Test
    public void testThatSystemInterfaceExceedingMaxSizeIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().systemInterface("x".repeat(51)).build();
        assert hasViolationFor(validator.validate(gpu), "systemInterface");
    }

    // MAX POWER TESTS
    @Test
    public void testThatMaxPowerAtLimitIsValid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().maxPower(10000).build();
        assert !hasViolationFor(validator.validate(gpu), "maxPower");
    }

    @Test
    public void testThatMaxPowerExceedingLimitIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().maxPower(10001).build();
        assert hasViolationFor(validator.validate(gpu), "maxPower");
    }

    // NOT BLANK TESTS
    @Test
    public void testThatBlankModelNumberIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().modelNumber("").build();
        assert hasViolationFor(validator.validate(gpu), "modelNumber");
    }

    @Test
    public void testThatBlankNameIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().name("").build();
        assert hasViolationFor(validator.validate(gpu), "name");
    }

    @Test
    public void testThatBlankChipManufacturerIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().chipManufacturer("").build();
        assert hasViolationFor(validator.validate(gpu), "chipManufacturer");
    }

    @Test
    public void testThatBlankSystemInterfaceIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().systemInterface("").build();
        assert hasViolationFor(validator.validate(gpu), "systemInterface");
    }

    // NOT NULL TESTS
    @Test
    public void testThatNullGpuMemoryIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().gpuMemory(null).build();
        assert hasViolationFor(validator.validate(gpu), "gpuMemory");
    }

    @Test
    public void testThatNullMemoryInterfaceIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().memoryInterface(null).build();
        assert hasViolationFor(validator.validate(gpu), "memoryInterface");
    }

    @Test
    public void testThatNullMemoryBandwidthIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().memoryBandwidth(null).build();
        assert hasViolationFor(validator.validate(gpu), "memoryBandwidth");
    }

    @Test
    public void testThatNullCudaCoresIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().cudaCores(null).build();
        assert hasViolationFor(validator.validate(gpu), "cudaCores");
    }

    @Test
    public void testThatNullTensorCoresIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().tensorCores(null).build();
        assert hasViolationFor(validator.validate(gpu), "tensorCores");
    }

    @Test
    public void testThatNullRaytracingCoresIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().raytracingCores(null).build();
        assert hasViolationFor(validator.validate(gpu), "raytracingCores");
    }

    @Test
    public void testThatNullMaxPowerIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().maxPower(null).build();
        assert hasViolationFor(validator.validate(gpu), "maxPower");
    }

    @Test
    public void testThatNullIsActiveIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().isActive(null).build();
        assert hasViolationFor(validator.validate(gpu), "isActive");
    }

    // NOT NEGATIVE TESTS
    @Test
    public void testThatNegativeGpuMemoryIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().gpuMemory(-1).build();
        assert hasViolationFor(validator.validate(gpu), "gpuMemory");
    }

    @Test
    public void testThatNegativeMemoryInterfaceIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().memoryInterface(-1).build();
        assert hasViolationFor(validator.validate(gpu), "memoryInterface");
    }

    @Test
    public void testThatNegativeMemoryBandwidthIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().memoryBandwidth(-1).build();
        assert hasViolationFor(validator.validate(gpu), "memoryBandwidth");
    }

    @Test
    public void testThatNegativeCudaCoresIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().cudaCores(-1).build();
        assert hasViolationFor(validator.validate(gpu), "cudaCores");
    }

    @Test
    public void testThatNegativeTensorCoresIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().tensorCores(-1).build();
        assert hasViolationFor(validator.validate(gpu), "tensorCores");
    }

    @Test
    public void testThatNegativeRaytracingCoresIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().raytracingCores(-1).build();
        assert hasViolationFor(validator.validate(gpu), "raytracingCores");
    }

    @Test
    public void testThatNegativeMaxPowerIsInvalid() {
        GPUWorkstationDTO gpu = validGPUWorkstationBuilder().maxPower(-1).build();
        assert hasViolationFor(validator.validate(gpu), "maxPower");
    }
}