package com.priceTracker.domain.entities.vendorEntities;

import jakarta.persistence.*;
import lombok.*;
import static com.priceTracker.constants.otherConstants.DatabaseConstants.UMART_PRODUCT_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = UMART_PRODUCT_NAME)
public class UmartProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vendor;
    private String productType;
    private String modelNumber;
    private String url;
}
