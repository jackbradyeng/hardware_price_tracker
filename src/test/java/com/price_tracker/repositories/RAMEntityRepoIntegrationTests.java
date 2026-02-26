package com.price_tracker.repositories;

import com.price_tracker.TestDataUtility;
import com.price_tracker.domain.entities.RAMEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode =  DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RAMEntityRepoIntegrationTests {

    private final RAMRepository testInstance;
    private final TestDataUtility data;

    @Autowired
    public RAMEntityRepoIntegrationTests(RAMRepository testInstance, TestDataUtility data) {
        this.testInstance = testInstance;
        this.data = data;
    }

    @Test
    public void testCreateANdRecall() {
        RAMEntity ramEntity = data.createTestRAM();
        testInstance.save(ramEntity);
        Optional<RAMEntity> result = testInstance.findById(ramEntity.getModelNumber());
        assert (result).isPresent();
        assert (result.get()).equals(ramEntity);
    }
}
