package com.acoes.fleetmanagement.workshop.execution.infractrusture.dto;

import com.acoes.fleetmanagement.workshop.execution.domain.model.WorkshopExecutionStatus;

import java.time.LocalDate;

public record ExecutionResponse(

        Long id,
        String executionNumber,
        String workshopOrderNumber,
        Long workshopOrderId,
        WorkshopExecutionStatus status,
        LocalDate startDate,
        LocalDate endDate,
        boolean active

) {
}
