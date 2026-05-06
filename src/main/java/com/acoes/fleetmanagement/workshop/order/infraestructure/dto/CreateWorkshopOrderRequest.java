package com.acoes.fleetmanagement.workshop.order.infraestructure.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateWorkshopOrderRequest(

        @NotNull(message = "Vehicle id is required")
        Long vehicleId,

        @FutureOrPresent
        LocalDate openingDate

) {
}
