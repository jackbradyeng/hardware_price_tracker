package com.price_tracker.repositories;

import com.price_tracker.domain.entities.product_entities.RAMEntity;
import com.price_tracker.repositories.product_repos.RAMRepository;
import com.price_tracker.testing_data.ram_data.RAMTestingUtility;
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
    private final RAMTestingUtility ramTestingUtility;

    @Autowired
    public RAMEntityRepoIntegrationTests(RAMRepository testInstance, RAMTestingUtility ramTestingUtility) {
        this.testInstance = testInstance;
        this.ramTestingUtility = ramTestingUtility;
    }

    @Test
    public void testCreateANdRecall() {
        RAMEntity ramEntity = ramTestingUtility.createTestRAM();
        testInstance.save(ramEntity);
        Optional<RAMEntity> result = testInstance.findById(ramEntity.getModelNumber());
        assert (result).isPresent();
        assert (result.get()).equals(ramEntity);
    }
}
