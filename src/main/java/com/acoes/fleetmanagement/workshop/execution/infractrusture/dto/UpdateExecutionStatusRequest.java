package com.acoes.fleetmanagement.workshop.execution.infractrusture.dto;

import com.acoes.fleetmanagement.workshop.execution.domain.model.WorkshopExecutionStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateExecutionStatusRequest(
        @NotNull(message = "Status is required")
        WorkshopExecutionStatus status
) {
}
