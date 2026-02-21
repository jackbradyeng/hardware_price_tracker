package com.price_tracker.services.impl;

import com.price_tracker.domain.entities.UmartProductEntity;
import com.price_tracker.repositories.UmartProductRepository;
import com.price_tracker.services.UmartProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UmartProductServiceImpl implements UmartProductService {

    private final UmartProductRepository umartProductRepository;

    @Override
    public UmartProductEntity save(UmartProductEntity umartProductEntity) {
        return umartProductRepository.save(umartProductEntity);
    }

    @Override
    public List<UmartProductEntity> findAll() {
        return umartProductRepository.findAll().stream().toList();
    }

    @Override
    public Optional<UmartProductEntity> findOne(String id) {
        return umartProductRepository.findById(id);
    }

    @Override
    public boolean exists(String id) {
        return umartProductRepository.existsById(id);
    }

    @Override
    public UmartProductEntity partialUpdate(String id, UmartProductEntity umartProductEntity) {
        return umartProductRepository.findById(id).map(existingUmartProduct -> {
            Optional.ofNullable(umartProductEntity.getUrl()).ifPresent(existingUmartProduct::setUrl);
            return umartProductRepository.save(existingUmartProduct);
        }).orElseThrow(() -> new RuntimeException("Umart product does not exist."));
    }

    @Override
    public void delete(String id) {
        umartProductRepository.deleteById(id);
    }
}
