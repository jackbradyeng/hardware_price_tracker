package com.priceTracker.repositories.productRepositories;

import com.priceTracker.domain.entities.productEntities.RAMEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RAMRepository extends JpaRepository<RAMEntity, String> {
}