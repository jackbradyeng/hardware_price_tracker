package com.price_tracker.repositories;

import com.price_tracker.domain.entities.product_entities.GPUEntity;
import com.price_tracker.repositories.product_repos.GPURepository;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
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

    @Autowired
    public GPUEntityRepoIntegrationTests(GPURepository testInstance, GPUTestingUtility gpuTestingUtility) {
        this.testInstance = testInstance;
        this.gpuTestingUtility = gpuTestingUtility;
    }

    @Test
    public void testCreateAndRecall() {
        GPUEntity gpuEntity = gpuTestingUtility.createTestGPU();
        testInstance.save(gpuEntity);
        Optional<GPUEntity> result = testInstance.findById(gpuEntity.getModelNumber());
        assert (result).isPresent();
        assert (result.get()).equals(gpuEntity);
    }
}
