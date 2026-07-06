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
@Table(name = HDD_PRICE_HISTORY)
public class HDDPricePoint extends GenericPricePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HDD_PRICE_SEQUENCE)
    @SequenceGenerator(
            name = HDD_PRICE_SEQUENCE,
            sequenceName = HDD_PRICE_SEQUENCE,
            allocationSize = 1
    )
    private Long id;
}