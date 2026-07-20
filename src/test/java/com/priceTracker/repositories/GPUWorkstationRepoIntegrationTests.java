package com.priceTracker.repositories;

import com.priceTracker.domain.entities.productEntities.GPUWorkstationEntity;
import com.priceTracker.repositories.productRepositories.GPUWorkstationRepository;
import com.priceTracker.testingData.wsgpuData.WorkstationGPUTestingUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GPUWorkstationRepoIntegrationTests {

    private final GPUWorkstationRepository testInstance;
    private final WorkstationGPUTestingUtility workstationGPUTestingUtility;

    @Autowired
    public GPUWorkstationRepoIntegrationTests(GPUWorkstationRepository testInstance,
                                              WorkstationGPUTestingUtility workstationGPUTestingUtility) {
        this.testInstance = testInstance;
        this.workstationGPUTestingUtility = workstationGPUTestingUtility;
    }

    @Test
    public void testCreateAndRecall() {
        GPUWorkstationEntity workstationGPUEntity = workstationGPUTestingUtility.createTestWorkstationGPU();
        testInstance.save(workstationGPUEntity);
        Optional<GPUWorkstationEntity> result = testInstance.findById(workstationGPUEntity.getModelNumber());
        assert (result).isPresent();
        assert (result.get()).equals(workstationGPUEntity);
    }
}