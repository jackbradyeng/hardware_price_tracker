package com.price_tracker.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RAMDTO {

    private String modelNumber;
    private String name;
    private String brand;
    private int volume;
    private int dimmCount;
    private int clockRate;
    private String latency;
    private Double voltage;
    private boolean isActive = true;
}
