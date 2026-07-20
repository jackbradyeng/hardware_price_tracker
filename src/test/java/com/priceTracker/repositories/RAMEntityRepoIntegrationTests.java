package com.priceTracker.repositories;

import com.priceTracker.domain.dto.productDTOs.RAMDTO;
import com.priceTracker.domain.entities.productEntities.RAMEntity;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.repositories.productRepositories.RAMRepository;
import com.priceTracker.testingData.ramData.RAMTestingUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class RAMEntityRepoIntegrationTests {

    private final RAMRepository testInstance;
    private final RAMTestingUtility ramTestingUtility;
    private final GenericMapper<RAMEntity, RAMDTO> ramMapper;

    @Autowired
    public RAMEntityRepoIntegrationTests(RAMRepository testInstance,
                                         RAMTestingUtility ramTestingUtility,
                                         MapperFactory mapperFactory) {
        this.testInstance = testInstance;
        this.ramTestingUtility = ramTestingUtility;
        this.ramMapper = mapperFactory.create(RAMEntity.class, RAMDTO.class);
    }

    @Test
    public void testCreateAndRecall() {
        RAMEntity ramEntity = ramMapper.mapFrom(ramTestingUtility.createTestRAM());
        testInstance.save(ramEntity);
        Optional<RAMEntity> result = testInstance.findById(ramEntity.getModelNumber());
        assert (result).isPresent();
        assert (result.get()).equals(ramEntity);
    }
}