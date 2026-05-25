package com.price_tracker.testing_data.nvme_data;

import com.price_tracker.domain.dto.product_dtos.NVMEDTO;
import com.price_tracker.domain.entities.product_entities.NVMEEntity;
import com.price_tracker.mappers.product_mappers.NVMEMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import static com.price_tracker.testing_data.nvme_data.NVMETestingData.*;

@Component
@AllArgsConstructor
public class NVMETestingUtility {

    private final NVMEMapper nvmeMapper;

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
                .build());
    }

    public List<NVMEDTO> createListOfNVMEs() {
        ArrayList<NVMEDTO> nvmeDTOs = new ArrayList<>();
        nvmeDTOs.add(createTestNVME());
        nvmeDTOs.add(createSecondTestNVME());
        return nvmeDTOs;
    }
}