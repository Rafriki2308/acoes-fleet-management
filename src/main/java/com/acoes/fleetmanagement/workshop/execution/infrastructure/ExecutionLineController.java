package com.acoes.fleetmanagement.workshop.execution.infrastructure;

import com.acoes.fleetmanagement.workshop.execution.application.ExecutionLineService;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.CreateExecutionLineRequest;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.ExecutionLineResponse;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.PatchExecutionLineRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.EndpointConstants.*;

/**
 * Expone operaciones REST para gestionar lineas de ejecucion de taller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(EXEC_LINES)
public class ExecutionLineController {

    private final ExecutionLineService executionLineService;

    /**
     * Crea una linea dentro de una ejecucion existente.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param request         datos necesarios para crear la linea.
     * @return linea de ejecucion creada.
     */
    @PostMapping(CREATE_EXEC_LINE_BY_EXEC)
    @ResponseStatus(HttpStatus.CREATED)
    public ExecutionLineResponse create(
            @PathVariable String executionNumber,
            @Valid @RequestBody CreateExecutionLineRequest request
    ) {
        return executionLineService.create(executionNumber, request);
    }

    /**
     * Lista las lineas activas de una ejecucion.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @return lineas activas de la ejecucion.
     */
    @GetMapping(GET_EXEC_LINES_BY_EXEC)
    public List<ExecutionLineResponse> findByExecutionNumber(
            @PathVariable String executionNumber
    ) {
        return executionLineService.findByExecutionNumber(executionNumber);
    }

    /**
     * Actualiza parcialmente una linea de ejecucion.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param lineNumber      numero funcional de la linea.
     * @param request         datos opcionales que se aplican sobre la linea.
     * @return linea de ejecucion actualizada.
     */
    @PatchMapping(PATCH_EXEC_LINE_BY_EXEC)
    public ExecutionLineResponse patch(
            @PathVariable String executionNumber,
            @PathVariable Integer lineNumber,
            @Valid @RequestBody PatchExecutionLineRequest request
    ) {
        return executionLineService.patch(
                executionNumber,
                lineNumber,
                request
        );
    }

    /**
     * Da de baja logicamente una linea de ejecucion.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param lineNumber      numero funcional de la linea.
     */
    @PatchMapping(PATCH_EXEC_LINE_OFF_BY_EXEC)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(
            @PathVariable String executionNumber,
            @PathVariable Integer lineNumber
    ) {
        executionLineService.deactivate(executionNumber, lineNumber);
    }
}
