package com.price_tracker.services.vendor_services.impl;

import com.price_tracker.domain.entities.vendor_entities.ScorptecProductEntity;
import com.price_tracker.repositories.vendor_repos.ScorptecProductRepository;
import com.price_tracker.services.vendor_services.ScorptecProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScorptecProductServiceImpl implements ScorptecProductService {

    private final ScorptecProductRepository scorptecProductRepository;

    @Override
    public ScorptecProductEntity save(ScorptecProductEntity scorptecProductEntity) {
        return scorptecProductRepository.save(scorptecProductEntity);
    }

    @Override
    public List<ScorptecProductEntity> findAll() {
        return scorptecProductRepository.findAll().stream().toList();
    }

    @Override
    public Optional<ScorptecProductEntity> findOne(String id) {
        return scorptecProductRepository.findById(id);
    }

    @Override
    public boolean exists(String id) {
        return scorptecProductRepository.existsById(id);
    }

    @Override
    public ScorptecProductEntity partialUpdate(String id, ScorptecProductEntity scorptecProductEntity) {
        return scorptecProductRepository.findById(id).map(existingScorptecProduct -> {
            Optional.ofNullable(scorptecProductEntity.getUrl()).ifPresent(existingScorptecProduct::setUrl);
            return scorptecProductRepository.save(existingScorptecProduct);
        }).orElseThrow(() -> new RuntimeException("Scorptec product does not exist."));
    }

    @Override
    public void delete(String id) {
        scorptecProductRepository.deleteById(id);
    }
}