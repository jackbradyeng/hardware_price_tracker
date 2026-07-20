package com.priceTracker.repositories;

import com.priceTracker.domain.dto.productDTOs.GPUDTO;
import com.priceTracker.domain.entities.productEntities.GPUEntity;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.repositories.productRepositories.GPURepository;
import com.priceTracker.testingData.gpuData.GPUTestingUtility;
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
public class GPUEntityRepoIntegrationTests {

    private final GPURepository testInstance;
    private final GPUTestingUtility gpuTestingUtility;
    private final GenericMapper<GPUEntity, GPUDTO> gpuMapper;

    @Autowired
    public GPUEntityRepoIntegrationTests(GPURepository testInstance,
                                         GPUTestingUtility gpuTestingUtility,
                                         MapperFactory mapperFactory) {
        this.testInstance = testInstance;
        this.gpuTestingUtility = gpuTestingUtility;
        this.gpuMapper = mapperFactory.create(GPUEntity.class, GPUDTO.class);
    }

    @Test
    public void testCreateAndRecall() {
        GPUEntity gpuEntity = gpuMapper.mapFrom(gpuTestingUtility.createTestGPU());
        testInstance.save(gpuEntity);
        Optional<GPUEntity> result = testInstance.findById(gpuEntity.getModelNumber());
        assert (result).isPresent();
        assert (result.get()).equals(gpuEntity);
    }
}