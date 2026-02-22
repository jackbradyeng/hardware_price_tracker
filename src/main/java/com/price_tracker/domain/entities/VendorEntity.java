package com.price_tracker.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static com.price_tracker.constants.DatabaseTableNames.VENDOR_TABLE_NAME;

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
    private Boolean activeStatus;
}
