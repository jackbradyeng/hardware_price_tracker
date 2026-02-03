package com.price_tracker.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RAM {

    private int id;
    private String name;
    private String brand;
    private int volume;
    private int clockRate;
}
