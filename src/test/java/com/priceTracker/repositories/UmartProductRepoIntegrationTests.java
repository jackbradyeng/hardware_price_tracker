package com.priceTracker.repositories;

import com.priceTracker.domain.dto.productDTOs.GPUDTO;
import com.priceTracker.domain.dto.productDTOs.RAMDTO;
import com.priceTracker.domain.dto.vendorDTOs.VendorProductDTO;
import com.priceTracker.repositories.vendorRepositories.UmartProductRepository;
import com.priceTracker.services.productServices.GenericProductService;
import com.priceTracker.services.vendorServices.impl.UmartProductServiceImpl;
import com.priceTracker.testingData.gpuData.GPUTestingUtility;
import com.priceTracker.testingData.ramData.RAMTestingUtility;
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
    private final UmartProductServiceImpl umartProductService;
    private final GenericProductService<GPUDTO> gpuService;
    private final GenericProductService<RAMDTO> ramService;
    private final GPUTestingUtility gpuTestingUtility;
    private final RAMTestingUtility ramTestingUtility;

    @Autowired
    public UmartProductRepoIntegrationTests(UmartProductRepository umartProductRepository,
                                            UmartProductServiceImpl umartProductService,
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
        VendorProductDTO savedUmartGPU = umartProductService.save(gpuTestingUtility.createTestUmartGPU());
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
        VendorProductDTO savedUmartRAM = umartProductService.save(ramTestingUtility.createTestUmartRAM());
        assert umartProductRepository.findUrlsForActiveRAM().getFirst().equals(savedUmartRAM.getUrl());
    }

    @Test
    public void testThatSavedInactiveRAMProductIsNotReturnedByGetURLs() {
        RAMDTO ramEntity = ramTestingUtility.createTestRAM();
        ramEntity.setIsActive(false);
        ramService.save(ramEntity);
        umartProductService.save(ramTestingUtility.createTestUmartRAM());
        assert umartProductRepository.findUrlsForActiveRAM().isEmpty();
    }
}