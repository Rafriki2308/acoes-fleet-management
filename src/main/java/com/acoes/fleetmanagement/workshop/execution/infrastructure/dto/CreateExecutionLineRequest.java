package com.acoes.fleetmanagement.workshop.execution.infrastructure.dto;

import com.acoes.fleetmanagement.workshop.execution.domain.model.ExecutionLineStatus;
import com.acoes.fleetmanagement.workshop.execution.domain.model.ExecutionLineType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

import static com.acoes.fleetmanagement.shared.constants.ValidationConstants.QUANTITY_VALUE;
import static com.acoes.fleetmanagement.shared.constants.ValidationMessageConstants.*;

/**
 * Peticion para crear una linea de ejecucion de taller.
 *
 * @param lineNumber numero funcional de la linea dentro de la ejecucion.
 * @param description descripcion del trabajo realizado o elemento registrado.
 * @param type tipo de linea de ejecucion.
 * @param quantity cantidad asociada a la linea.
 * @param status estado inicial de la linea.
 */
public record CreateExecutionLineRequest(

        @NotNull(message = EXECUTION_LINE_REQUIRED)
        Integer lineNumber,

        @NotBlank(message = EXECUTION_LINE_DESCRIPTION_REQUIRED)
        String description,

        @NotNull(message = EXECUTION_LINE_TYPE_REQUIRED)
        ExecutionLineType type,

        @NotNull(message = EXECUTION_LINE_QUANTITY_REQUIRED)
        @DecimalMin(
                value = QUANTITY_VALUE,
                message = EXECUTION_LINE_QUANTITY_MUST_BE_GREATER_ZERO
        )
        BigDecimal quantity,

        @NotNull(message = EXECUTION_LINE_STATUS_REQUIRED)
        ExecutionLineStatus status

) {
}
