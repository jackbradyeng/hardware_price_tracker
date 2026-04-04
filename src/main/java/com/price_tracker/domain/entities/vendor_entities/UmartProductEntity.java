package com.price_tracker.domain.entities.vendor_entities;

import jakarta.persistence.*;
import lombok.*;
import static com.price_tracker.constants.other_constants.DatabaseConstants.UMART_PRODUCT_NAME;

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
