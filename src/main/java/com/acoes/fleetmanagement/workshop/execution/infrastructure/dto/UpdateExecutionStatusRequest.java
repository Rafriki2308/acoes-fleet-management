package com.acoes.fleetmanagement.workshop.execution.infrastructure.dto;

import com.acoes.fleetmanagement.workshop.execution.domain.model.ExecutionStatus;
import jakarta.validation.constraints.NotNull;

import static com.acoes.fleetmanagement.shared.constants.ValidationMessageConstants.EXECUTION_STATUS_IS_REQUIRED;

/**
 * DTO de entrada para cambiar el estado de una ejecucion de taller.
 */

public record UpdateExecutionStatusRequest(
        @NotNull(message = EXECUTION_STATUS_IS_REQUIRED)
        ExecutionStatus status
) {
}
