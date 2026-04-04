package com.price_tracker.repositories;

import com.price_tracker.domain.entities.product_entities.CPUEntity;
import com.price_tracker.repositories.product_repos.CPURepository;
import com.price_tracker.testing_data.cpu_data.CPUTestingUtility;
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
public class CPUEntityRepoIntegrationTests {

    private final CPURepository testInstance;
    private final CPUTestingUtility cpuTestingUtility;

    @Autowired
    public CPUEntityRepoIntegrationTests(CPURepository testInstance, CPUTestingUtility cpuTestingUtility) {
        this.testInstance = testInstance;
        this.cpuTestingUtility = cpuTestingUtility;
    }

    @Test
    public void testCreateAndRecall() {
        CPUEntity cpuEntity = cpuTestingUtility.createTestCPU();
        testInstance.save(cpuEntity);
        Optional<CPUEntity> result = testInstance.findById(cpuEntity.getModelNumber());
        assert (result).isPresent();
        assert (result.get()).equals(cpuEntity);
    }
}
