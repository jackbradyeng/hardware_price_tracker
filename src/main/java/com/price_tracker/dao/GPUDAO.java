package com.price_tracker.dao;

import java.util.Optional;
import com.price_tracker.domain.GPU;

public interface GPUDAO {

    void create(GPU gpu);

    Optional<GPU> find(String modelNumber);
}
