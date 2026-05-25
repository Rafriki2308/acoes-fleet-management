package com.acoes.fleetmanagement.workshop.execution.infrastructure;

import com.acoes.fleetmanagement.workshop.execution.application.ExecutionService;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.CreateExecutionRequest;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.ExecutionResponse;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.UpdateExecutionStatusRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.EndpointConstants.*;

/**
 * Expone los endpoints REST para gestionar ejecuciones de taller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(EXECUTIONS)
public class ExecutionController {

    private final ExecutionService executionService;

    /**
     * Crea la ejecucion de una orden de taller.
     *
     * @param orderNumber numero funcional de la orden.
     * @param request datos de la ejecucion a crear.
     * @return ejecucion creada.
     */
    @PostMapping(CREATE_EXEC_BY_ORDER)
    @ResponseStatus(HttpStatus.CREATED)
    public ExecutionResponse create(
            @PathVariable String orderNumber,
            @Valid @RequestBody CreateExecutionRequest request
    ) {
        return executionService.create(orderNumber, request);
    }

    /**
     * Lista todas las ejecuciones activas.
     *
     * @return ejecuciones activas.
     */
    @GetMapping
    public List<ExecutionResponse> findAll() {
        return executionService.findAll();
    }

    /**
     * Obtiene una ejecucion activa por numero funcional.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @return ejecucion encontrada.
     */
    @GetMapping(GET_EXEC_BY_NUMBER)
    public ExecutionResponse findByExecutionNumber(
            @PathVariable String executionNumber
    ) {
        return executionService.findByExecutionNumber(executionNumber);
    }

    /**
     * Obtiene una ejecucion activa por numero funcional de orden.
     *
     * @param orderNumber numero funcional de la orden.
     * @return ejecucion encontrada.
     */
    @GetMapping(GET_EXEC_BY_ORDER)
    public ExecutionResponse findByOrderNumber(
            @PathVariable String orderNumber
    ) {
        return executionService.findByOrderNumber(orderNumber);
    }

    /**
     * Lista ejecuciones activas por matricula de vehiculo.
     *
     * @param plateNumber matricula del vehiculo.
     * @return ejecuciones asociadas.
     */
    @GetMapping(GET_EXEC_BY_PLATE)
    public List<ExecutionResponse> findByVehiclePlateNumber(
            @PathVariable String plateNumber
    ) {
        return executionService.findByVehiclePlateNumber(plateNumber);
    }

    /**
     * Lista ejecuciones activas por VIN de vehiculo.
     *
     * @param vin VIN del vehiculo.
     * @return ejecuciones asociadas.
     */
    @GetMapping(GET_EXEC_BY_VIN)
    public List<ExecutionResponse> findByVehicleVin(
            @PathVariable String vin
    ) {
        return executionService.findByVehicleVin(vin);
    }

    /**
     * Cambia el estado de una ejecucion activa.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param request nuevo estado.
     * @return ejecucion actualizada.
     */
    @PatchMapping(PATCH_EXEC_STATUS_BY_NUMBER)
    public ExecutionResponse updateStatus(
            @PathVariable String executionNumber,
            @Valid @RequestBody UpdateExecutionStatusRequest request
    ) {
        return executionService.updateStatus(
                executionNumber,
                request
        );
    }

    /**
     * Da de baja logicamente una ejecucion activa.
     *
     * @param executionNumber numero funcional de la ejecucion.
     */
    @PatchMapping(PATCH_EXEC_OFF_BY_NUMBER)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable String executionNumber) {
        executionService.deactivate(executionNumber);
    }
}
