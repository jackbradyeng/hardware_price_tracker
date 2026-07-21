package com.priceTracker.repositories.vendorRepositories;

import com.priceTracker.domain.entities.vendorEntities.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<VendorEntity, String> {
}
