package com.price_tracker.services.impl;

import com.price_tracker.domain.entities.RAM;
import com.price_tracker.repositories.RAMRepository;
import com.price_tracker.services.RAMService;
import org.springframework.stereotype.Service;

@Service
public class RAMServiceImpl implements RAMService {

    private final RAMRepository ramRepository;

    public RAMServiceImpl(RAMRepository ramRepository) {
        this.ramRepository = ramRepository;
    }

    @Override
    public RAM createRAM(RAM ram) {
        return ramRepository.save(ram);
    }
}
