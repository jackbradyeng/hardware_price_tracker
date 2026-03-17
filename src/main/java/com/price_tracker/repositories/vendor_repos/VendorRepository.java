package com.price_tracker.repositories.vendor_repos;

import com.price_tracker.domain.entities.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<VendorEntity, String> {
}
