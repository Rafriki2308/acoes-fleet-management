package com.acoes.fleetmanagement.workshop.order.infrastructure.dto;

import com.acoes.fleetmanagement.workshop.order.domain.model.OrderLinePriority;
import jakarta.validation.constraints.Positive;
/**
 * DTO de entrada para actualizar parcialmente una linea de orden de taller.
 */

public record PatchOrderLineRequest(

        @Positive(message = "Line number must be greater than zero")
        Integer lineNumber,

        String workDescription,

        OrderLinePriority priority

) {
}
