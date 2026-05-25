package com.acoes.fleetmanagement.workshop.order.infrastructure.dto;

import com.acoes.fleetmanagement.workshop.order.domain.model.OrderLinePriority;
/**
 * DTO de salida con la informacion de una linea de orden de taller.
 */

public record OrderLineResponse(

        Long id,
        String workshopOrderNumber,
        Integer lineNumber,
        String workDescription,
        OrderLinePriority priority,
        boolean active

) {
}
