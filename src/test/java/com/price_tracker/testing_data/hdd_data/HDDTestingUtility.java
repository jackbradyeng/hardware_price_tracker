package com.price_tracker.testing_data.hdd_data;

import com.price_tracker.domain.dto.product_dtos.HDDDTO;
import com.price_tracker.domain.entities.product_entities.HDDEntity;
import com.price_tracker.mappers.product_mappers.HDDMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import static com.price_tracker.testing_data.hdd_data.HDDTestingData.*;

@Component
@AllArgsConstructor
public class HDDTestingUtility {

    private final HDDMapper hddMapper;

    public HDDDTO createTestHDD() {
        return hddMapper.mapTo(HDDEntity.builder()
                .modelNumber(TESTING_HDD_MODEL_NUMBER)
                .name(TESTING_HDD_NAME)
                .brand(TESTING_HDD_BRAND)
                .capacity(TESTING_HDD_CAPACITY)
                .sequentialRead(TESTING_HDD_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_HDD_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_HDD_MTBF)
                .storageInterface(TESTING_HDD_STORAGE_INTERFACE)
                .formFactor(TESTING_HDD_FORM_FACTOR)
                .build());
    }

    public HDDDTO createSecondTestHDD() {
        return hddMapper.mapTo(HDDEntity.builder()
                .modelNumber(SECOND_TESTING_HDD_MODEL_NUMBER)
                .name(SECOND_TESTING_HDD_NAME)
                .brand(TESTING_HDD_BRAND)
                .capacity(TESTING_HDD_CAPACITY)
                .sequentialRead(TESTING_HDD_SEQUENTIAL_READ)
                .sequentialWrite(TESTING_HDD_SEQUENTIAL_WRITE)
                .meanTimeBetweenFailures(TESTING_HDD_MTBF)
                .storageInterface(TESTING_HDD_STORAGE_INTERFACE)
                .formFactor(TESTING_HDD_FORM_FACTOR)
                .build());
    }

    public List<HDDDTO> createListOfHDDs() {
        ArrayList<HDDDTO> hddDTOs = new ArrayList<>();
        hddDTOs.add(createTestHDD());
        hddDTOs.add(createSecondTestHDD());
        return hddDTOs;
    }
}