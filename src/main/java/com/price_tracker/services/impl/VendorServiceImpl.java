package com.price_tracker.services.impl;

import com.price_tracker.domain.entities.VendorEntity;
import com.price_tracker.repositories.VendorRepository;
import com.price_tracker.services.VendorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public VendorEntity save(VendorEntity vendorEntity) {
        return vendorRepository.save(vendorEntity);
    }

    @Override
    public List<VendorEntity> findAll() {
        return vendorRepository.findAll().stream().toList();
    }

    @Override
    public Optional<VendorEntity> findOne(String id) {
        return vendorRepository.findById(id);
    }

    @Override
    public boolean exists(String id) {
        return vendorRepository.existsById(id);
    }

    @Override
    public VendorEntity partialUpdate(String id, VendorEntity vendorEntity) {
        vendorEntity.setVendor(id);

        return vendorRepository.findById(id).map(existingVendor -> {
            Optional.ofNullable(vendorEntity.getHomeURL()).ifPresent(existingVendor::setHomeURL);
            return vendorRepository.save(existingVendor);
        }).orElseThrow(() -> new RuntimeException("Vendor does not exist"));
    }

    @Override
    public void delete(String id) {
        vendorRepository.deleteById(id);
    }
}
