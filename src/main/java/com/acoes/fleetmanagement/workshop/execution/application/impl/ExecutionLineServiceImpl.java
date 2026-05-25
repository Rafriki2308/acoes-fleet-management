package com.acoes.fleetmanagement.workshop.execution.application.impl;

import com.acoes.fleetmanagement.shared.exception.DuplicateResourceException;
import com.acoes.fleetmanagement.shared.exception.ResourceNotFoundException;
import com.acoes.fleetmanagement.workshop.execution.application.ExecutionLineService;
import com.acoes.fleetmanagement.workshop.execution.application.mapper.ExecutionLineMapper;
import com.acoes.fleetmanagement.workshop.execution.application.repository.ExecutionJpaRepository;
import com.acoes.fleetmanagement.workshop.execution.application.repository.ExecutionLineJpaRepository;
import com.acoes.fleetmanagement.workshop.execution.domain.ExecutionJpaEntity;
import com.acoes.fleetmanagement.workshop.execution.domain.ExecutionLineJpaEntity;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.CreateExecutionLineRequest;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.ExecutionLineResponse;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.PatchExecutionLineRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.ExceptionMessageConstans.*;

/**
 * Implementa las reglas de negocio y persistencia para lineas de ejecucion de taller.
 */
@Service
@RequiredArgsConstructor
public class ExecutionLineServiceImpl implements ExecutionLineService {

    private final ExecutionLineJpaRepository executionLineRepository;
    private final ExecutionJpaRepository executionRepository;
    private final ExecutionLineMapper executionLineMapper;

    /**
     * Crea una linea de ejecucion tras resolver la ejecucion activa y validar que el numero de linea sea unico.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param request datos necesarios para crear la linea.
     * @return linea de ejecucion creada.
     * @throws DuplicateResourceException si ya existe una linea con el mismo numero en la ejecucion.
     * @throws ResourceNotFoundException si no existe una ejecucion activa con el numero indicado.
     */
    @Override
    @Transactional
    public ExecutionLineResponse create(
            String executionNumber,
            CreateExecutionLineRequest request
    ) {
        ExecutionJpaEntity execution =
                findActiveExecutionByNumber(executionNumber);

        validateLineNumberUniqueness(
                execution.getId(),
                request.lineNumber()
        );

        ExecutionLineJpaEntity line =
                executionLineMapper.toEntity(request, execution);

        return executionLineMapper.toResponse(
                executionLineRepository.save(line)
        );
    }

    /**
     * Obtiene las lineas activas de una ejecucion activa.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @return lista de lineas activas ordenadas por numero de linea.
     * @throws ResourceNotFoundException si no existe una ejecucion activa con el numero indicado.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExecutionLineResponse> findByExecutionNumber(
            String executionNumber
    ) {
        findActiveExecutionByNumber(executionNumber);

        return executionLineRepository
                .findByExecutionExecutionNumberAndActiveTrueOrderByLineNumberAsc(
                        executionNumber
                )
                .stream()
                .map(executionLineMapper::toResponse)
                .toList();
    }

    /**
     * Actualiza parcialmente una linea activa de una ejecucion.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param lineNumber numero funcional de la linea.
     * @param request datos opcionales que se aplican sobre la linea.
     * @return linea de ejecucion actualizada.
     * @throws ResourceNotFoundException si no existe una linea activa con los identificadores indicados.
     */
    @Override
    @Transactional
    public ExecutionLineResponse patch(
            String executionNumber,
            Integer lineNumber,
            PatchExecutionLineRequest request
    ) {
        ExecutionLineJpaEntity line =
                findActiveLineByExecutionNumberAndLineNumber(
                        executionNumber,
                        lineNumber
                );

        executionLineMapper.patchEntityFromRequest(request, line);

        return executionLineMapper.toResponse(line);
    }

    /**
     * Da de baja logicamente una linea activa de una ejecucion.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param lineNumber numero funcional de la linea.
     * @throws ResourceNotFoundException si no existe una linea activa con los identificadores indicados.
     */
    @Override
    @Transactional
    public void deactivate(
            String executionNumber,
            Integer lineNumber
    ) {
        ExecutionLineJpaEntity line =
                findActiveLineByExecutionNumberAndLineNumber(
                        executionNumber,
                        lineNumber
                );

        // Hibernate persistira este cambio por dirty checking.
        line.setActive(false);
    }

    /**
     * Recupera una ejecucion activa por su numero funcional.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @return ejecucion activa encontrada.
     */
    private ExecutionJpaEntity findActiveExecutionByNumber(
            String executionNumber
    ) {
        return executionRepository
                .findByExecutionNumberAndActiveTrue(executionNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                EXECUTION_NOT_FOUND + executionNumber
                        )
                );
    }

    /**
     * Recupera una linea activa por numero de ejecucion y numero de linea.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param lineNumber numero funcional de la linea.
     * @return linea activa encontrada.
     */
    private ExecutionLineJpaEntity findActiveLineByExecutionNumberAndLineNumber(
            String executionNumber,
            Integer lineNumber
    ) {
        return executionLineRepository
                .findByExecutionExecutionNumberAndLineNumberAndActiveTrue(
                        executionNumber,
                        lineNumber
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                EXECUTION_LINE_NOT_FOUND
                                        + executionNumber
                                        + ", line: "
                                        + lineNumber
                        )
                );
    }

    /**
     * Valida que el numero de linea no exista previamente dentro de la ejecucion.
     *
     * @param executionId identificador interno de la ejecucion.
     * @param lineNumber numero funcional de la linea.
     */
    private void validateLineNumberUniqueness(
            Long executionId,
            Integer lineNumber
    ) {
        if (executionLineRepository.existsByExecutionIdAndLineNumber(
                executionId,
                lineNumber
        )) {
            throw new DuplicateResourceException(LINE_ALREADY_EXISTS);
        }
    }
}
