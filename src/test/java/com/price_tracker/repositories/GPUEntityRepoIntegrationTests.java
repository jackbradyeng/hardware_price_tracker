package com.price_tracker.repositories;

import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.domain.entities.product_entities.GPUEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.product_repos.GPURepository;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode =  DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
