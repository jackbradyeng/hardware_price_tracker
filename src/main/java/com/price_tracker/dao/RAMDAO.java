package com.price_tracker.dao;

import java.util.Optional;
import com.price_tracker.domain.RAM;

public interface RAMDAO {

    void create(RAM ram);

    Optional<RAM> find(String id);
}
