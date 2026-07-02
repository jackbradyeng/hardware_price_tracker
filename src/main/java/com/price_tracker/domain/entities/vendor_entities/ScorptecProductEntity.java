package com.price_tracker.domain.entities.vendor_entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static com.price_tracker.constants.other_constants.DatabaseConstants.SCORPTEC_PRODUCT_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = SCORPTEC_PRODUCT_NAME)
public class ScorptecProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vendor;
    private String productType;
    private String modelNumber;
    private String url;
}
