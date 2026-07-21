package com.priceTracker.repositories;

import com.priceTracker.domain.dto.productDTOs.CPUDTO;
import com.priceTracker.domain.dto.productDTOs.GPUDTO;
import com.priceTracker.domain.dto.vendorDTOs.VendorProductDTO;
import com.priceTracker.repositories.vendorRepositories.ScorptecProductRepository;
import com.priceTracker.services.productServices.GenericProductService;
import com.priceTracker.services.vendorServices.impl.ScorptecProductServiceImpl;
import com.priceTracker.testingData.cpuData.CPUTestingUtility;
import com.priceTracker.testingData.gpuData.GPUTestingUtility;
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
public class ScorptecProductRepoIntegrationTests {

    private final ScorptecProductRepository scorptecProductRepository;
    private final ScorptecProductServiceImpl scorptecProductService;
    private final GenericProductService<GPUDTO> gpuService;
    private final GenericProductService<CPUDTO> cpuService;
    private final GPUTestingUtility gpuTestingUtility;
    private final CPUTestingUtility cpuTestingUtility;

    @Autowired
    public ScorptecProductRepoIntegrationTests(ScorptecProductRepository scorptecProductRepository,
                                               ScorptecProductServiceImpl ScorptecProductServiceImpl,
                                               GenericProductService<GPUDTO> gpuService,
                                               GenericProductService<CPUDTO> cpuService,
                                               GPUTestingUtility gpuTestingUtility,
                                               CPUTestingUtility cpuTestingUtility) {
        this.scorptecProductRepository = scorptecProductRepository;
        this.scorptecProductService = ScorptecProductServiceImpl;
        this.cpuService = cpuService;
        this.gpuService = gpuService;
        this.gpuTestingUtility = gpuTestingUtility;
        this.cpuTestingUtility = cpuTestingUtility;
    }

    @Test
    public void testThatActiveSavedGPUProductIsReturnedByGetURLs() {
        gpuService.save(gpuTestingUtility.createTestGPU());
        VendorProductDTO savedScorptecGPU = scorptecProductService.save(gpuTestingUtility.createTestScorptecGPU());
        assert scorptecProductRepository.findUrlsForActiveGPUs().getFirst().equals(savedScorptecGPU.getUrl());
    }

    @Test
    public void testThatSavedInactiveGPUProductIsNotReturnedBGetURLs() {
        GPUDTO gpuEntity = gpuTestingUtility.createTestGPU();
        gpuEntity.setIsActive(false);
        gpuService.save(gpuEntity);
        scorptecProductService.save(gpuTestingUtility.createTestScorptecGPU());
        assert scorptecProductRepository.findUrlsForActiveGPUs().isEmpty();
    }

    @Test
    public void testThatSavedActiveCPUProductIsReturnedByGetURLs() {
        cpuService.save(cpuTestingUtility.createTestCPU());
        VendorProductDTO scorptecVendorProduct = cpuTestingUtility.createTestScorptecCPU();
        VendorProductDTO savedScorptecCPU = scorptecProductService.save(scorptecVendorProduct);
        assert scorptecProductRepository.findUrlsForActiveCPU().getFirst().equals(savedScorptecCPU.getUrl());
    }

    @Test
    public void testThatSavedInactiveCPUProductIsNotReturnedByGetURLs() {
        CPUDTO cpuEntity = cpuTestingUtility.createTestCPU();
        cpuEntity.setIsActive(false);
        cpuService.save(cpuEntity);
        scorptecProductService.save(cpuTestingUtility.createTestScorptecCPU());
        assert scorptecProductRepository.findUrlsForActiveCPU().isEmpty();
    }
}