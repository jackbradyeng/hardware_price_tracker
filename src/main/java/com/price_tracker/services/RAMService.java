package com.price_tracker.services;

import com.price_tracker.domain.entities.RAM;

import java.util.List;
import java.util.Optional;

public interface RAMService {
    RAM createRAM(RAM ram);

    List<RAM> findAll();

    Optional<RAM> findOne(String id);
}
