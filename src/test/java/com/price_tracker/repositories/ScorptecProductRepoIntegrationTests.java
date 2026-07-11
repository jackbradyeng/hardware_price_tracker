package com.price_tracker.repositories;

import com.price_tracker.domain.dto.product_dtos.CPUDTO;
import com.price_tracker.domain.dto.product_dtos.GPUDTO;
import com.price_tracker.domain.entities.vendor_entities.ScorptecProductEntity;
import com.price_tracker.repositories.vendor_repos.ScorptecProductRepository;
import com.price_tracker.services.product_services.GenericProductService;
import com.price_tracker.services.vendor_services.ScorptecProductService;
import com.price_tracker.testing_data.cpu_data.CPUTestingUtility;
import com.price_tracker.testing_data.gpu_data.GPUTestingUtility;
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
    private final ScorptecProductService scorptecProductService;
    private final GenericProductService<GPUDTO> gpuService;
    private final GenericProductService<CPUDTO> cpuService;
    private final GPUTestingUtility gpuTestingUtility;
    private final CPUTestingUtility cpuTestingUtility;

    @Autowired
    public ScorptecProductRepoIntegrationTests(ScorptecProductRepository scorptecProductRepository,
                                               ScorptecProductService scorptecProductService,
                                               GenericProductService<GPUDTO> gpuService,
                                               GenericProductService<CPUDTO> cpuService,
                                               GPUTestingUtility gpuTestingUtility,
                                               CPUTestingUtility cpuTestingUtility) {
        this.scorptecProductRepository = scorptecProductRepository;
        this.scorptecProductService = scorptecProductService;
        this.cpuService = cpuService;
        this.gpuService = gpuService;
        this.gpuTestingUtility = gpuTestingUtility;
        this.cpuTestingUtility = cpuTestingUtility;
    }

    @Test
    public void testThatActiveSavedGPUProductIsReturnedByGetURLs() {
        gpuService.save(gpuTestingUtility.createTestGPU());
        ScorptecProductEntity savedScorptecGPU = scorptecProductService.save(gpuTestingUtility.createTestScorptecGPU());
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
        ScorptecProductEntity savedScorptecCPU = scorptecProductRepository.save(cpuTestingUtility.createTestScorptecCPU());
        assert scorptecProductRepository.findUrlsForActiveCPU().getFirst().equals(savedScorptecCPU.getUrl());
    }

    @Test
    public void testThatSavedInactiveCPUProductIsNotReturnedByGetURLs() {
        CPUDTO cpuEntity = cpuTestingUtility.createTestCPU();
        cpuEntity.setIsActive(false);
        cpuService.save(cpuEntity);
        scorptecProductRepository.save(cpuTestingUtility.createTestScorptecCPU());
        assert scorptecProductRepository.findUrlsForActiveCPU().isEmpty();
    }
}