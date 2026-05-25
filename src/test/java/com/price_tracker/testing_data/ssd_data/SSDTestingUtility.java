package com.price_tracker.testing_data.ssd_data;

import com.price_tracker.domain.dto.product_dtos.SSDDTO;
import com.price_tracker.domain.entities.product_entities.SSDEntity;
import com.price_tracker.mappers.product_mappers.SSDMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import static com.price_tracker.testing_data.ssd_data.SSDTestingData.*;

@Component
@AllArgsConstructor
public class SSDTestingUtility {

    private final SSDMapper ssdMapper;

    public SSDDTO createTestSSD() {
        return ssdMapper.mapTo(SSDEntity.builder()
                .modelNumber(TESTING_SSD_MODEL_NUMBER)
                .name(TESTING_SSD_NAME)
                .brand(TESTING_SSD_BRAND)
                .capacity(TESTING_SSD_CAPACITY)
                .sequentialRead(TESTING_SSD_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_SSD_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_SSD_MTBF)
                .storageInterface(TESTING_SSD_STORAGE_INTERFACE)
                .build());
    }

    public SSDDTO createSecondTestSSD() {
        return ssdMapper.mapTo(SSDEntity.builder()
                .modelNumber(SECOND_TESTING_SSD_MODEL_NUMBER)
                .name(SECOND_TESTING_SSD_NAME)
                .brand(TESTING_SSD_BRAND)
                .capacity(TESTING_SSD_CAPACITY)
                .sequentialRead(TESTING_SSD_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_SSD_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_SSD_MTBF)
                .storageInterface(TESTING_SSD_STORAGE_INTERFACE)
                .build());
    }

    public List<SSDDTO> createListOfSSDs() {
        ArrayList<SSDDTO> ssdDTOs = new ArrayList<>();
        ssdDTOs.add(createTestSSD());
        ssdDTOs.add(createSecondTestSSD());
        return ssdDTOs;
    }
}