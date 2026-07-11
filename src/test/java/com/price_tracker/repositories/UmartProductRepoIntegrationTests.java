package com.price_tracker.repositories;

import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.domain.dto.product_dtos.RAMDTO;
import com.price_tracker.domain.entities.vendor_entities.UmartProductEntity;
import com.price_tracker.repositories.vendor_repos.UmartProductRepository;
import com.price_tracker.services.product_services.GenericProductService;
import com.price_tracker.services.vendor_services.UmartProductService;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
import com.price_tracker.testing_data.ram_data.RAMTestingUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UmartProductRepoIntegrationTests {

    private final UmartProductRepository umartProductRepository;
    private final UmartProductService umartProductService;
    private final GenericProductService<GPUDTO> gpuService;
    private final GenericProductService<RAMDTO> ramService;
    private final GPUTestingUtility gpuTestingUtility;
    private final RAMTestingUtility ramTestingUtility;

    @Autowired
    public UmartProductRepoIntegrationTests(UmartProductRepository umartProductRepository,
                                            UmartProductService umartProductService,
                                            GenericProductService<GPUDTO> gpuService,
                                            GenericProductService<RAMDTO> ramService,
                                            GPUTestingUtility gpuTestingUtility,
                                            RAMTestingUtility ramTestingUtility) {
        this.umartProductRepository = umartProductRepository;
        this.umartProductService  = umartProductService;
        this.ramService = ramService;
        this.gpuService = gpuService;
        this.gpuTestingUtility = gpuTestingUtility;
        this.ramTestingUtility = ramTestingUtility;
    }

    @Test
    public void testThatActiveSavedGPUProductIsReturnedByGetURLs() {
        gpuService.save(gpuTestingUtility.createTestGPU());
        UmartProductEntity savedUmartGPU = umartProductService.save(gpuTestingUtility.createTestUmartGPU());
        assert umartProductRepository.findUrlsForActiveGPUs().getFirst().equals(savedUmartGPU.getUrl());
    }

    @Test
    public void testThatSavedInactiveGPUProductIsNotReturnedBGetURLs() {
        GPUDTO gpuEntity = gpuTestingUtility.createTestGPU();
        gpuEntity.setIsActive(false);
        gpuService.save(gpuEntity);
        umartProductService.save(gpuTestingUtility.createTestUmartGPU());
        assert umartProductRepository.findUrlsForActiveGPUs().isEmpty();
    }

    @Test
    public void testThatSavedActiveRAMProductIsReturnedByGetURLs() {
        ramService.save(ramTestingUtility.createTestRAM());
        UmartProductEntity savedUmartRAM = umartProductRepository.save(ramTestingUtility.createTestUmartRAM());
        assert umartProductRepository.findUrlsForActiveRAM().getFirst().equals(savedUmartRAM.getUrl());
    }

    @Test
    public void testThatSavedInactiveRAMProductIsNotReturnedByGetURLs() {
        RAMDTO ramEntity = ramTestingUtility.createTestRAM();
        ramEntity.setIsActive(false);
        ramService.save(ramEntity);
        umartProductRepository.save(ramTestingUtility.createTestUmartRAM());
        assert umartProductRepository.findUrlsForActiveRAM().isEmpty();
    }
}
