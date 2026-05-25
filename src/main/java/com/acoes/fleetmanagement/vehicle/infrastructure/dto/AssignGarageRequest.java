package com.acoes.fleetmanagement.vehicle.infrastructure.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO de entrada para asignar un garaje actual a un vehiculo.
 */
public record AssignGarageRequest(
        @NotNull(message = "Garage id is required")
        @Positive
        Long garageId
) {
}
