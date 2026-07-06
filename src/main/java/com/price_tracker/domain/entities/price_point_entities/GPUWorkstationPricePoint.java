package com.price_tracker.domain.entities.price_point_entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import static com.price_tracker.constants.other_constants.DatabaseConstants.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = WORKSTATION_GPU_PRICE_HISTORY)
public class GPUWorkstationPricePoint extends GenericPricePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = WS_GPU_PRICE_SEQUENCE)
    @SequenceGenerator(
            name = WS_GPU_PRICE_SEQUENCE,
            sequenceName = WS_GPU_PRICE_SEQUENCE,
            allocationSize = 1
    )
    private Long id;
}
