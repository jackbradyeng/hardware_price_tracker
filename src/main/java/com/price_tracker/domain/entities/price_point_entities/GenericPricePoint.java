package com.price_tracker.domain.entities.price_point_entities;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class GenericPricePoint {

    /* each subclass honours this contract via their respective Lombok annotations. The abstract class
    * cannot declare an ID field itself as Spring Data JPA prohibits child classes from overriding 
    * @GeneratedValue & @SequenceGenerator annotations. */
    public abstract Long getId();
    public abstract void setId(Long id);

    private String modelNumber;
    private String vendor;
    private String currency;
    private BigDecimal price;
    private LocalDateTime scrapedAt;
}