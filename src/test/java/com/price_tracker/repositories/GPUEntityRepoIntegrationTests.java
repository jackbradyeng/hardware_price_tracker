package com.price_tracker.repositories;

import com.price_tracker.TestDataUtility;
import com.price_tracker.domain.entities.GPUEntity;
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
    private final TestDataUtility data;

    @Autowired
    public GPUEntityRepoIntegrationTests(GPURepository testInstance, TestDataUtility tdl) {
        this.testInstance = testInstance;
        this.data = tdl;
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
