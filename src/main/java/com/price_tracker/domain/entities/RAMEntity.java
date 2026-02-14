package com.price_tracker.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.price_tracker.constants.DBTableNames.RAM_TABLE_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = RAM_TABLE_NAME)
public class RAMEntity {

    @Id
    private String modelNumber;
    private String name;
    private String brand;
    private String standard;
    private String latency;
    private int volume;
    private int dimmCount;
    private int clockRate; // measured in MHZ
    private Double voltage; // measured in volts
    private boolean isActive = true; // active status flag, tells the scraper whether to look for the given model
}
