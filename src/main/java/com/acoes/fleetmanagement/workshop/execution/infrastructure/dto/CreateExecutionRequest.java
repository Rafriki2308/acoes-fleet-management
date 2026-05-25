package com.acoes.fleetmanagement.workshop.execution.infrastructure.dto;

import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

import static com.acoes.fleetmanagement.shared.constants.ValidationMessageConstants.EXECUTION_MUST_BE_PAST_OR_PRESENT;

/**
 * DTO de entrada para crear la ejecucion de una orden de taller.
 */

public record CreateExecutionRequest(

        @PastOrPresent(message = EXECUTION_MUST_BE_PAST_OR_PRESENT)
        LocalDate startDate

) {
}
