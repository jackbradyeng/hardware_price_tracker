package com.priceTracker.domain.entities.pricePointEntities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import static com.priceTracker.constants.otherConstants.DatabaseConstants.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = CPU_PRICE_HISTORY)
public class CPUPricePoint extends GenericPricePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = CPU_PRICE_SEQUENCE)
    @SequenceGenerator(
            name = CPU_PRICE_SEQUENCE,
            sequenceName = CPU_PRICE_SEQUENCE,
            allocationSize = 1
    )
    private Long id;
}
