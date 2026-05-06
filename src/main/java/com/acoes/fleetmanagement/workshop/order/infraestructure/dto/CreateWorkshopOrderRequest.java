package com.acoes.fleetmanagement.workshop.order.infraestructure.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import static com.acoes.fleetmanagement.shared.constants.ValidationMessageConstants.VEHICLE_ID_IS_REQUIRED_MESSAGE;

public record CreateWorkshopOrderRequest(

        @NotNull(message = VEHICLE_ID_IS_REQUIRED_MESSAGE)
        Long vehicleId,

        @FutureOrPresent
        LocalDate openingDate

) {
}
