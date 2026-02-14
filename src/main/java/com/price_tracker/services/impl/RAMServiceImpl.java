package com.price_tracker.services.impl;

import com.price_tracker.domain.entities.RAMEntity;
import com.price_tracker.repositories.RAMRepository;
import com.price_tracker.services.RAMService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class RAMServiceImpl implements RAMService {

    private final RAMRepository ramRepository;

    public RAMServiceImpl(RAMRepository ramRepository) {
        this.ramRepository = ramRepository;
    }

    @Override
    public RAMEntity save(RAMEntity ramEntity) {
        return ramRepository.save(ramEntity);
    }

    @Override
    public List<RAMEntity> findAll() {
        return StreamSupport
                .stream(ramRepository.findAll().spliterator(), false)
                .toList();
    }

    @Override
    public Optional<RAMEntity> findOne(String id) {
        return ramRepository.findById(id);
    }

    @Override
    public boolean exists(String id) {
        return ramRepository.existsById(id);
    }

    @Override
    public RAMEntity partialUpdate(String id, RAMEntity ramEntity) {
        ramEntity.setModelNumber(id);

        return ramRepository.findById(id).map(existingRAM -> {
            Optional.ofNullable(ramEntity.getName()).ifPresent(existingRAM::setName);
            return ramRepository.save(existingRAM);
        }).orElseThrow(() -> new RuntimeException("RAM does not exist"));
    }

    @Override
    public void delete(String id) {
        ramRepository.deleteById(id);
    }
}
