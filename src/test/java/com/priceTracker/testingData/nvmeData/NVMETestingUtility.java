package com.priceTracker.testingData.nvmeData;

import com.priceTracker.domain.dto.productDTOs.NVMEDTO;
import com.priceTracker.domain.entities.productEntities.NVMEEntity;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.mappers.MapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import static com.priceTracker.testingData.nvmeData.NVMETestingData.*;

@Component
public class NVMETestingUtility {

    private final GenericMapper<NVMEEntity, NVMEDTO> nvmeMapper;

    @Autowired
    public NVMETestingUtility(MapperFactory mapperFactory) {
        this.nvmeMapper = mapperFactory.create(NVMEEntity.class, NVMEDTO.class);
    }

    /// SAMPLE ENTITIES/DTOS
    public NVMEDTO createTestNVME() {
        return nvmeMapper.mapTo(NVMEEntity.builder()
                .modelNumber(TESTING_NVME_MODEL_NUMBER)
                .name(TESTING_NVME_NAME)
                .brand(TESTING_NVME_BRAND)
                .capacity(TESTING_NVME_CAPACITY)
                .sequentialRead(TESTING_NVME_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_NVME_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_NVME_MTBF)
                .storageInterface(TESTING_NVME_STORAGE_INTERFACE)
                .includesHeatSink(TESTING_NVME_INCLUDES_HEAT_SINK)
                .isActive(true)
                .build());
    }

    public NVMEDTO createSecondTestNVME() {
        return nvmeMapper.mapTo(NVMEEntity.builder()
                .modelNumber(SECOND_TESTING_NVME_MODEL_NUMBER)
                .name(SECOND_TESTING_NVME_NAME)
                .brand(TESTING_NVME_BRAND)
                .capacity(TESTING_NVME_CAPACITY)
                .sequentialRead(TESTING_NVME_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_NVME_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_NVME_MTBF)
                .storageInterface(TESTING_NVME_STORAGE_INTERFACE)
                .includesHeatSink(TESTING_NVME_INCLUDES_HEAT_SINK)
                .isActive(true)
                .build());
    }

    public List<NVMEDTO> createListOfNVMEs() {
        ArrayList<NVMEDTO> nvmeDTOs = new ArrayList<>();
        nvmeDTOs.add(createTestNVME());
        nvmeDTOs.add(createSecondTestNVME());
        return nvmeDTOs;
    }
}