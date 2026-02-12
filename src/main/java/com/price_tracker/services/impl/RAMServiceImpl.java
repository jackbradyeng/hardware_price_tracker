package com.price_tracker.services.impl;

import com.price_tracker.domain.entities.RAM;
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
    public RAM save(RAM ram) {
        return ramRepository.save(ram);
    }

    @Override
    public List<RAM> findAll() {
        return StreamSupport
                .stream(ramRepository.findAll().spliterator(), false)
                .toList();
    }

    @Override
    public Optional<RAM> findOne(String id) {
        return ramRepository.findById(id);
    }

    @Override
    public boolean exists(String id) {
        return ramRepository.existsById(id);
    }

    @Override
    public RAM partialUpdate(String id, RAM ram) {
        ram.setId(id);

        return ramRepository.findById(id).map(existingRAM -> {
            Optional.ofNullable(ram.getPrice()).ifPresent(existingRAM::setPrice);
            return ramRepository.save(existingRAM);
        }).orElseThrow(() -> new RuntimeException("RAM does not exist"));
    }

    @Override
    public void delete(String id) {
        ramRepository.deleteById(id);
    }
}
