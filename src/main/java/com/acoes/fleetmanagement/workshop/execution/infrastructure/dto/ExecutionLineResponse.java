package com.acoes.fleetmanagement.workshop.execution.infrastructure.dto;

import com.acoes.fleetmanagement.workshop.execution.domain.model.ExecutionLineStatus;
import com.acoes.fleetmanagement.workshop.execution.domain.model.ExecutionLineType;

import java.math.BigDecimal;

/**
 * Respuesta con la informacion publica de una linea de ejecucion de taller.
 *
 * @param id identificador interno de la linea.
 * @param executionNumber numero funcional de la ejecucion asociada.
 * @param lineNumber numero funcional de la linea.
 * @param description descripcion del trabajo realizado o elemento registrado.
 * @param type tipo de linea de ejecucion.
 * @param quantity cantidad asociada a la linea.
 * @param status estado de la linea.
 * @param active indica si la linea sigue activa.
 */
public record ExecutionLineResponse(

        Long id,
        String executionNumber,
        Integer lineNumber,
        String description,
        ExecutionLineType type,
        BigDecimal quantity,
        ExecutionLineStatus status,
        boolean active

) {
}
