package com.price_tracker.repositories;

import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import com.price_tracker.domain.entities.product_entities.RAMEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.product_repos.RAMRepository;
import com.price_tracker.testing_data.ram_data.RAMTestingUtility;
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