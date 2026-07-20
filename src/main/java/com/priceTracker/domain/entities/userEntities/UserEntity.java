package com.priceTracker.domain.entities.userEntities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static com.priceTracker.constants.otherConstants.DatabaseConstants.USER_SEQUENCE;
import static com.priceTracker.constants.otherConstants.DatabaseConstants.USER_TABLE_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = USER_TABLE_NAME)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = USER_SEQUENCE)
    @SequenceGenerator(
            name = USER_SEQUENCE,
            sequenceName = USER_SEQUENCE,
            allocationSize = 1
    )
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}