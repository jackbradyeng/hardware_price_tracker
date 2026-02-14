package com.price_tracker.repositories;

import com.price_tracker.domain.entities.GPUEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GPUEntityRepoIntegrationTests {

    private final GPURepository testInstance;
    private final TestDataUtility data = new TestDataUtility();

    @Autowired
    public GPUEntityRepoIntegrationTests(GPURepository testInstance) {
        this.testInstance = testInstance;
    }

    @Test
    public void testCreateAndRecall() {
        GPUEntity gpuEntity = data.createTestGPU();
        testInstance.save(gpuEntity);
        Optional<GPUEntity> result = testInstance.findById(gpuEntity.getModelNumber());
        assert (result).isPresent();
        assert (result.get()).equals(gpuEntity);
    }
}
