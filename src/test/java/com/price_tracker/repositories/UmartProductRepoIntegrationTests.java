package com.price_tracker.repositories;

import com.price_tracker.TestDataUtility;
import com.price_tracker.domain.entities.GPUEntity;
import com.price_tracker.domain.entities.RAMEntity;
import com.price_tracker.domain.entities.UmartProductEntity;
import com.price_tracker.services.GPUService;
import com.price_tracker.services.RAMService;
import com.price_tracker.services.UmartProductService;
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
    private final TestDataUtility tdl;
    private final RAMService ramService;

    @Autowired
    public UmartProductRepoIntegrationTests(UmartProductRepository umartProductRepository,
                                            UmartProductService umartProductService,
                                            GPUService gpuService,
                                            RAMService ramService,
                                            TestDataUtility tdl) {
        this.umartProductRepository = umartProductRepository;
        this.umartProductService  = umartProductService;
        this.ramService = ramService;
        this.gpuService = gpuService;
        this.tdl = tdl;
    }

    @Test
    public void testThatActiveSavedGPUProductIsReturnedByGetURLs() {
        GPUEntity gpuEntity = tdl.createTestGPU();
        GPUEntity savedGPU = gpuService.save(gpuEntity);
        UmartProductEntity testUmartGPU = tdl.createTestUmartGPU();
        UmartProductEntity savedUmartGPU = umartProductService.save(testUmartGPU);
        assert umartProductRepository.findUrlsForActiveGPUs().getFirst().equals(savedUmartGPU.getUrl());
    }

    @Test
    public void testThatSavedInactiveGPUProductIsNotReturnedBGetURLs() {
        GPUEntity gpuEntity = tdl.createTestGPU();
        gpuEntity.setIsActive(false);
        GPUEntity savedGPU = gpuService.save(gpuEntity);
        UmartProductEntity testUmartGPU = tdl.createTestUmartGPU();
        UmartProductEntity savedUmartGPU = umartProductService.save(testUmartGPU);
        assert umartProductRepository.findUrlsForActiveGPUs().isEmpty();
    }

    @Test
    public void testThatSavedActiveRAMProductIsReturnedByGetURLs() {
        RAMEntity ramEntity = tdl.createTestRAM();
        RAMEntity savedRAM = ramService.save(ramEntity);
        UmartProductEntity testUmartRAM = tdl.createTestUmartRAM();
        UmartProductEntity savedUmartRAM = umartProductRepository.save(testUmartRAM);
        assert umartProductRepository.findUrlsForActiveRAM().getFirst().equals(savedUmartRAM.getUrl());
    }

    @Test
    public void testThatSavedInactiveRAMProductIsNotReturnedByGetURLs() {
        RAMEntity ramEntity = tdl.createTestRAM();
        ramEntity.setIsActive(false);
        RAMEntity savedRAM = ramService.save(ramEntity);
        UmartProductEntity testUmartRAM = tdl.createTestUmartRAM();
        UmartProductEntity savedUmartRAM = umartProductRepository.save(testUmartRAM);
        assert umartProductRepository.findUrlsForActiveRAM().isEmpty();
    }
}
