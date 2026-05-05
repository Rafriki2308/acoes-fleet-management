package com.acoes.fleetmanagement.garage.infrastructure.dto;

public record GarageResponse(Long id,
                             String name,

                             String street,
                             String city,
                             String province,
                             String postalCode,
                             String country,

                             String contactName,
                             String phone,
                             String email,
                             String notes,

                             boolean active) {
}
