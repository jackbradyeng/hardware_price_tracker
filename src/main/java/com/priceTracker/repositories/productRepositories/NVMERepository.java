package com.priceTracker.repositories.productRepositories;

import com.priceTracker.domain.entities.productEntities.NVMEEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NVMERepository extends JpaRepository<NVMEEntity, String> {
}