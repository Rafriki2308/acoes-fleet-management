package com.acoes.fleetmanagement.workshop.execution.infrastructure.dto;

import com.acoes.fleetmanagement.workshop.execution.domain.model.ExecutionLineStatus;
import com.acoes.fleetmanagement.workshop.execution.domain.model.ExecutionLineType;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

import static com.acoes.fleetmanagement.shared.constants.ValidationConstants.QUANTITY_VALUE;
import static com.acoes.fleetmanagement.shared.constants.ValidationMessageConstants.EXECUTION_LINE_QUANTITY_MUST_BE_GREATER_ZERO;

/**
 * Peticion para actualizar parcialmente una linea de ejecucion de taller.
 *
 * @param description descripcion del trabajo realizado o elemento registrado.
 * @param type        tipo de linea de ejecucion.
 * @param quantity    cantidad asociada a la linea.
 * @param status      estado de la linea.
 */
public record PatchExecutionLineRequest(


        String description,

        ExecutionLineType type,

        @DecimalMin(value = QUANTITY_VALUE, message = EXECUTION_LINE_QUANTITY_MUST_BE_GREATER_ZERO)
        BigDecimal quantity,

        ExecutionLineStatus status

) {
}
