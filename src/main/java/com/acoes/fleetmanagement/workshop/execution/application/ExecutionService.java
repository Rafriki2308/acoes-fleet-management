package com.acoes.fleetmanagement.workshop.execution.application;

import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.CreateExecutionRequest;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.ExecutionResponse;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.UpdateExecutionStatusRequest;

import java.util.List;

/**
 * Define las operaciones de aplicacion disponibles para ejecuciones de taller.
 */
public interface ExecutionService {

    /**
     * Crea la ejecucion activa de una orden de taller.
     *
     * @param orderNumber numero funcional de la orden.
     * @param request datos necesarios para iniciar la ejecucion.
     * @return ejecucion creada.
     */
    ExecutionResponse create(
            String orderNumber,
            CreateExecutionRequest request
    );

    /**
     * Obtiene todas las ejecuciones activas.
     *
     * @return lista de ejecuciones activas.
     */
    List<ExecutionResponse> findAll();

    /**
     * Busca una ejecucion activa por su numero funcional.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @return ejecucion encontrada.
     */
    ExecutionResponse findByExecutionNumber(String executionNumber);

    /**
     * Busca una ejecucion activa por el numero funcional de su orden.
     *
     * @param orderNumber numero funcional de la orden.
     * @return ejecucion encontrada.
     */
    ExecutionResponse findByOrderNumber(String orderNumber);

    /**
     * Busca ejecuciones activas por matricula de vehiculo.
     *
     * @param plateNumber matricula del vehiculo.
     * @return lista de ejecuciones asociadas.
     */
    List<ExecutionResponse> findByVehiclePlateNumber(String plateNumber);

    /**
     * Busca ejecuciones activas por VIN de vehiculo.
     *
     * @param vin VIN del vehiculo.
     * @return lista de ejecuciones asociadas.
     */
    List<ExecutionResponse> findByVehicleVin(String vin);

    /**
     * Cambia el estado de una ejecucion activa.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param request nuevo estado de la ejecucion.
     * @return ejecucion actualizada.
     */
    ExecutionResponse updateStatus(
            String executionNumber,
            UpdateExecutionStatusRequest request
    );

    /**
     * Da de baja logicamente una ejecucion activa.
     *
     * @param executionNumber numero funcional de la ejecucion.
     */
    void deactivate(String executionNumber);
}
