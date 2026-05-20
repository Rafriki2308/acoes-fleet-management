package com.acoes.fleetmanagement.workshop.order.infraestructure.dto;

import com.acoes.fleetmanagement.workshop.order.domain.model.WorkshopOrderLinePriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.acoes.fleetmanagement.shared.constants.ValidationMessageConstants.*;

public record CreateWorkshopOrderLineRequest(
        

        @Positive(message = WORKSHOP_ORDER_LINE_MUST_POSITIVE)
        Integer lineNumber,

        @NotBlank(message = WORKSHOP_ORDER_LINE_DESCRIPTION_REQUIRED)
        String workDescription,

        @NotNull(message = WORKSHOP_ORDER_LINE_PRIORITY_REQUIRED)
        WorkshopOrderLinePriority priority

) {
}
