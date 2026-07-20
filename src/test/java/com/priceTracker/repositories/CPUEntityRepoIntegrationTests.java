package com.priceTracker.repositories;

import com.priceTracker.domain.dto.productDTOs.CPUDTO;
import com.priceTracker.domain.entities.productEntities.CPUEntity;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.mappers.MapperFactory;
import com.priceTracker.repositories.productRepositories.CPURepository;
import com.priceTracker.testingData.cpuData.CPUTestingUtility;
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