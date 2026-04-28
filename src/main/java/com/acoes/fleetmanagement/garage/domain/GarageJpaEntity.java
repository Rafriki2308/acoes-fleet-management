package com.acoes.fleetmanagement.garage.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(
        name = "garages",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_garage_name", columnNames = "name")
        }
)
public class GarageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @Embedded
    //Anotation to override to avoid conficts
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "address_street", length = 150)),
            @AttributeOverride(name = "city", column = @Column(name = "address_city", length = 80)),
            @AttributeOverride(name = "province", column = @Column(name = "address_province", length = 80)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "address_postal_code", length = 20)),
            @AttributeOverride(name = "country", column = @Column(name = "address_country", length = 80))
    })
    private AddressEmbeddable address;

    @Column(length = 80)
    private String contactName;

    @Column(length = 30)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private boolean active = true;
}
