package com.price_tracker.repositories;

import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.domain.entities.product_entities.CPUEntity;
import com.price_tracker.mappers.GenericMapper;
import com.price_tracker.mappers.MapperFactory;
import com.price_tracker.repositories.product_repos.CPURepository;
import com.price_tracker.testing_data.cpu_data.CPUTestingUtility;
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
public class CPUEntityRepoIntegrationTests {

    private final CPURepository testInstance;
    private final CPUTestingUtility cpuTestingUtility;
    private final GenericMapper<CPUEntity, CPUDTO> cpuMapper;

    @Autowired
    public CPUEntityRepoIntegrationTests(CPURepository testInstance,
                                         CPUTestingUtility cpuTestingUtility,
                                         MapperFactory mapperFactory) {
        this.testInstance = testInstance;
        this.cpuTestingUtility = cpuTestingUtility;
        this.cpuMapper = mapperFactory.create(CPUEntity.class, CPUDTO.class);
    }

    @Test
    public void testCreateAndRecall() {
        CPUEntity cpuEntity = cpuMapper.mapFrom(cpuTestingUtility.createTestCPU());
        testInstance.save(cpuEntity);
        Optional<CPUEntity> result = testInstance.findById(cpuEntity.getModelNumber());
        assert (result).isPresent();
        assert (result.get()).equals(cpuEntity);
    }
}
