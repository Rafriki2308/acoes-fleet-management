package com.acoes.fleetmanagement.shared.businessnumber.domain;

import com.acoes.fleetmanagement.shared.businessnumber.domain.model.BusinessSequenceKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA que almacena el ultimo valor usado por una secuencia de negocio.
 */
@Getter
@Setter
@Entity
@Table(
        name = "business_sequences",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_business_sequence_key_year_month",
                        columnNames = {"sequence_key", "sequence_year", "sequence_month"}
                )
        }
)
public class BusinessSequenceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sequence_key", nullable = false, length = 50)
    private BusinessSequenceKey sequenceKey;

    @Column(name = "sequence_year", nullable = false)
    private Integer year;

    @Column(name = "sequence_month", nullable = false)
    private Integer month;

    @Column(name = "current_value", nullable = false)
    private Long currentValue;
}
