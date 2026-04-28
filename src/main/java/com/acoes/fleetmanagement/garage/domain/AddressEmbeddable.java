package com.acoes.fleetmanagement.garage.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Embeddable
public class AddressEmbeddable {

    @Column(name = "street", length = 150)
    private String street;

    @Column(name = "city", length = 80)
    private String city;

    @Column(name = "province", length = 80)
    private String province;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "country", length = 80)
    private String country;
}
