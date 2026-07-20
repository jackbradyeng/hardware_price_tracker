package com.priceTracker.repositories.vendorRepositories;

import com.priceTracker.domain.entities.vendorEntities.UmartProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UmartProductRepository extends JpaRepository<UmartProductEntity, String> {

    // JPA mapping query used to extract GPU product URLs from the DB
    @Query("select u.url from UmartProductEntity u " +
            "join GPUEntity g on u.modelNumber = g.modelNumber " +
            "where u.productType='GPU' and g.isActive = true")
    List<String> findUrlsForActiveGPUs();

    @Query("select u.url from UmartProductEntity u " +
            "join RAMEntity r on u.modelNumber = r.modelNumber " +
            "where u.productType='RAM' and r.isActive = true")
    List<String> findUrlsForActiveRAM();

    @Query("select u.url from UmartProductEntity u " +
            "join CPUEntity c on u.modelNumber = c.modelNumber " +
            "where u.productType='CPU' and c.isActive = true")
    List<String> findUrlsForActiveCPU();

    @Query("select u.url from UmartProductEntity u " +
            "join GPUWorkstationEntity g on u.modelNumber = g.modelNumber " +
            "where u.productType='WORKSTATION GPU' and g.isActive = true")
    List<String> findUrlsForActiveWorkstationGPUs();

    @Query("select u.url from UmartProductEntity u " +
            "join HDDEntity h on u.modelNumber = h.modelNumber " +
            "where u.productType='HDD' and h.isActive = true")
    List<String> findUrlsForActiveHDDs();

    @Query("select u.url from UmartProductEntity u " +
            "join SSDEntity s on u.modelNumber = s.modelNumber " +
            "where u.productType='SSD' and s.isActive = true")
    List<String> findUrlsForActiveSSDs();

    @Query("select u.url from UmartProductEntity u " +
            "join NVMEEntity n on u.modelNumber = n.modelNumber " +
            "where u.productType='NVME' and n.isActive = true")
    List<String> findUrlsForActiveNVMEs();
}
