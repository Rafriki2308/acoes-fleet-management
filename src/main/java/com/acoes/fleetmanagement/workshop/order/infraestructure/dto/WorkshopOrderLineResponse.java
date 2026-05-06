package com.acoes.fleetmanagement.workshop.order.infraestructure.dto;

import com.acoes.fleetmanagement.workshop.order.domain.model.WorkshopOrderLinePriority;

public record WorkshopOrderLineResponse(

        Long id,
        String workshopOrderNumber,
        Integer lineNumber,
        String workDescription,
        WorkshopOrderLinePriority priority,
        boolean active

) {
}
