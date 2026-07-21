package com.priceTracker.domain.entities.vendorEntities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static com.priceTracker.constants.otherConstants.DatabaseConstants.VENDOR_TABLE_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = VENDOR_TABLE_NAME)
public class VendorEntity {

    @Id
    private String vendor;
    private String primaryLocation;
    private String primaryCurrency;
    private String homeURL;
    @Column(nullable = false)
    private Boolean activeStatus;
}