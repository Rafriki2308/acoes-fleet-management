package com.acoes.fleetmanagement.workshop.order.infraestructure.dto;

import com.acoes.fleetmanagement.workshop.order.domain.model.WorkshopOrderLinePriority;
import jakarta.validation.constraints.Positive;

public record PatchWorkshopOrderLineRequest(

        @Positive(message = "Line number must be greater than zero")
        Integer lineNumber,

        String workDescription,

        WorkshopOrderLinePriority priority

) {
}
