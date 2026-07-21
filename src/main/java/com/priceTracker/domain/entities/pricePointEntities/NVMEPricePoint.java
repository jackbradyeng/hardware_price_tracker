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
@Table(name = NVME_PRICE_HISTORY)
public class NVMEPricePoint extends GenericPricePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = NVME_PRICE_SEQUENCE)
    @SequenceGenerator(
            name = NVME_PRICE_SEQUENCE,
            sequenceName = NVME_PRICE_SEQUENCE,
            allocationSize = 1
    )
    private Long id;
}