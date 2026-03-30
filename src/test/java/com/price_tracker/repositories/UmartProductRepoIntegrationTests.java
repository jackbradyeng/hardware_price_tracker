package com.price_tracker.repositories;

import com.price_tracker.TestDataUtility;
import com.price_tracker.domain.entities.product_entities.GPUEntity;
import com.price_tracker.domain.entities.product_entities.RAMEntity;
import com.price_tracker.domain.entities.vendor_entities.UmartProductEntity;
import com.price_tracker.repositories.vendor_repos.UmartProductRepository;
import com.price_tracker.services.product_services.GPUService;
import com.price_tracker.services.product_services.RAMService;
import com.price_tracker.services.vendor_services.UmartProductService;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode =  DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UmartProductRepoIntegrationTests {

    private final UmartProductRepository umartProductRepository;
    private final UmartProductService umartProductService;
    private final GPUService gpuService;
    private final RAMService ramService;
    private final GPUTestingUtility gpuTestingUtility;
    private final TestDataUtility tdl;

    @Autowired
    public UmartProductRepoIntegrationTests(UmartProductRepository umartProductRepository,
                                            UmartProductService umartProductService,
                                            GPUService gpuService,
                                            RAMService ramService,
                                            GPUTestingUtility gpuTestingUtility,
                                            TestDataUtility tdl) {
        this.umartProductRepository = umartProductRepository;
        this.umartProductService  = umartProductService;
        this.ramService = ramService;
        this.gpuService = gpuService;
        this.gpuTestingUtility = gpuTestingUtility;
        this.tdl = tdl;
    }

    @Test
    public void testThatActiveSavedGPUProductIsReturnedByGetURLs() {
        gpuService.save(gpuTestingUtility.createTestGPU());
        UmartProductEntity savedUmartGPU = umartProductService.save(gpuTestingUtility.createTestUmartGPU());
        assert umartProductRepository.findUrlsForActiveGPUs().getFirst().equals(savedUmartGPU.getUrl());
    }

    @Test
    public void testThatSavedInactiveGPUProductIsNotReturnedBGetURLs() {
        GPUEntity gpuEntity = gpuTestingUtility.createTestGPU();
        gpuEntity.setIsActive(false);
        gpuService.save(gpuEntity);
        umartProductService.save(gpuTestingUtility.createTestUmartGPU());
        assert umartProductRepository.findUrlsForActiveGPUs().isEmpty();
    }

    @Test
    public void testThatSavedActiveRAMProductIsReturnedByGetURLs() {
        ramService.save(tdl.createTestRAM());
        UmartProductEntity savedUmartRAM = umartProductRepository.save(tdl.createTestUmartRAM());
        assert umartProductRepository.findUrlsForActiveRAM().getFirst().equals(savedUmartRAM.getUrl());
    }

    @Test
    public void testThatSavedInactiveRAMProductIsNotReturnedByGetURLs() {
        RAMEntity ramEntity = tdl.createTestRAM();
        ramEntity.setIsActive(false);
        ramService.save(ramEntity);
        umartProductRepository.save(tdl.createTestUmartRAM());
        assert umartProductRepository.findUrlsForActiveRAM().isEmpty();
    }
}
