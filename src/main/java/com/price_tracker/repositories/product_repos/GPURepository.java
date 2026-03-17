package com.price_tracker.repositories.product_repos;

import com.price_tracker.domain.entities.GPUEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GPURepository extends JpaRepository<GPUEntity, String> {
}
