package com.acoes.fleetmanagement.workshop.execution.infrastructure.dto;

import com.acoes.fleetmanagement.workshop.execution.domain.model.ExecutionStatus;

import java.time.LocalDate;
/**
 * DTO de salida con la informacion publica de una ejecucion de taller.
 */

public record ExecutionResponse(

        Long id,
        String executionNumber,
        String workshopOrderNumber,
        Long workshopOrderId,
        ExecutionStatus status,
        LocalDate startDate,
        LocalDate endDate,
        boolean active

) {
}
