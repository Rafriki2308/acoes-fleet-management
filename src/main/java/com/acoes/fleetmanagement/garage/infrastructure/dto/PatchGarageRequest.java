package com.acoes.fleetmanagement.garage.infrastructure.dto;
/**
 * DTO de entrada para actualizar parcialmente los datos de un garaje.
 */

public record PatchGarageRequest(
                                 String name,
                                 String street,
                                 String city,
                                 String province,
                                 String postalCode,
                                 String country,
                                 String contactName,
                                 String phone,
                                 String email,
                                 String notes
) {
}
