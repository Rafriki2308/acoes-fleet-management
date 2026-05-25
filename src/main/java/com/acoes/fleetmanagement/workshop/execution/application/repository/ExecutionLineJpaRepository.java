package com.acoes.fleetmanagement.workshop.execution.application.repository;

import com.acoes.fleetmanagement.workshop.execution.domain.ExecutionLineJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para consultar y persistir lineas de ejecucion de taller.
 */
@Repository
public interface ExecutionLineJpaRepository
        extends JpaRepository<ExecutionLineJpaEntity, Long> {

    /**
     * Busca lineas activas de una ejecucion por su numero funcional.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @return lineas activas ordenadas por numero de linea.
     */
    List<ExecutionLineJpaEntity>
    findByExecutionExecutionNumberAndActiveTrueOrderByLineNumberAsc(
            String executionNumber
    );

    /**
     * Busca una linea activa por numero de ejecucion y numero de linea.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param lineNumber numero funcional de la linea.
     * @return linea activa encontrada, si existe.
     */
    Optional<ExecutionLineJpaEntity>
    findByExecutionExecutionNumberAndLineNumberAndActiveTrue(
            String executionNumber,
            Integer lineNumber
    );

    /**
     * Comprueba si ya existe una linea con ese numero dentro de una ejecucion.
     *
     * @param executionId identificador interno de la ejecucion.
     * @param lineNumber numero funcional de la linea.
     * @return {@code true} si ya existe una linea con ese numero.
     */
    boolean existsByExecutionIdAndLineNumber(
            Long executionId,
            Integer lineNumber
    );
}
