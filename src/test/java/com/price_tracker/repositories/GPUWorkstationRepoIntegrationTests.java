package com.price_tracker.repositories;

import com.price_tracker.domain.entities.product_entities.GPUWorkstationEntity;
import com.price_tracker.repositories.product_repos.GPUWorkstationRepository;
import com.price_tracker.testing_data.wsgpu_data.WorkstationGPUTestingUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode =  DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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