package com.acoes.fleetmanagement.vehicle.infraestructure.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssignGarageRequest(
        @NotNull(message = "Garage id is required")
        @Positive
        Long garageId) {
}
