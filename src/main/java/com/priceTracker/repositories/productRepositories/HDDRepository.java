package com.priceTracker.repositories.productRepositories;

import com.priceTracker.domain.entities.productEntities.HDDEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HDDRepository extends JpaRepository<HDDEntity, String> {
}