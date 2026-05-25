package com.acoes.fleetmanagement.workshop.execution.application;

import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.CreateExecutionLineRequest;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.ExecutionLineResponse;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.PatchExecutionLineRequest;

import java.util.List;

/**
 * Define las operaciones disponibles para gestionar lineas de ejecucion de taller.
 */
public interface ExecutionLineService {

    /**
     * Crea una linea de ejecucion asociada a una ejecucion existente.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param request datos necesarios para crear la linea.
     * @return linea de ejecucion creada.
     */
    ExecutionLineResponse create(
            String executionNumber,
            CreateExecutionLineRequest request
    );

    /**
     * Obtiene las lineas activas de una ejecucion ordenadas por numero de linea.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @return lista de lineas activas asociadas a la ejecucion.
     */
    List<ExecutionLineResponse> findByExecutionNumber(
            String executionNumber
    );

    /**
     * Actualiza parcialmente una linea activa de una ejecucion.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param lineNumber numero funcional de la linea.
     * @param request datos opcionales que se aplican sobre la linea.
     * @return linea de ejecucion actualizada.
     */
    ExecutionLineResponse patch(
            String executionNumber,
            Integer lineNumber,
            PatchExecutionLineRequest request
    );

    /**
     * Da de baja logicamente una linea activa de una ejecucion.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param lineNumber numero funcional de la linea.
     */
    void deactivate(
            String executionNumber,
            Integer lineNumber
    );
}
