package com.price_tracker.repositories;

import com.price_tracker.TestDataUtility;
import com.price_tracker.domain.entities.GPUEntity;
import com.price_tracker.domain.entities.UmartProductEntity;
import com.price_tracker.services.GPUService;
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

    @Autowired
    public UmartProductRepoIntegrationTests(UmartProductRepository umartProductRepository,
                                            UmartProductService umartProductService,
                                            GPUService gpuService,
                                            TestDataUtility tdl) {
        this.umartProductRepository = umartProductRepository;
        this.umartProductService  = umartProductService;
        this.gpuService = gpuService;
        this.tdl = tdl;
    }

    @Test
    public void testThatSavedGPUProductIsReturnedByGetURLs() {
        GPUEntity gpuEntity = tdl.createTestGPU();
        GPUEntity savedGPU = gpuService.save(gpuEntity);
        UmartProductEntity testUmartGPU = tdl.createTestUmartGPU();
        UmartProductEntity savedUmartGPU = umartProductService.save(testUmartGPU);
        assert umartProductRepository.findUrlsForActiveGPUs().getFirst().equals(savedUmartGPU.getUrl());
    }

    public void testThatSavedRAMProductIsReturnedByGetURLs() {

    }
}
