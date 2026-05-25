package com.acoes.fleetmanagement.workshop.execution.application.mapper;

import com.acoes.fleetmanagement.workshop.execution.domain.ExecutionJpaEntity;
import com.acoes.fleetmanagement.workshop.execution.domain.ExecutionLineJpaEntity;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.CreateExecutionLineRequest;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.ExecutionLineResponse;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.PatchExecutionLineRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapea entre entidades de linea de ejecucion y DTOs de entrada o salida.
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE
)
public interface ExecutionLineMapper {

    /**
     * Convierte una peticion de creacion en una entidad nueva asociada a una ejecucion.
     *
     * @param request   datos recibidos para crear la linea.
     * @param execution ejecucion a la que pertenece la linea.
     * @return entidad de linea de ejecucion sin persistir.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "execution", source = "execution")
    @Mapping(target = "status", source = "request.status")
    @Mapping(target = "active", constant = "true")
    ExecutionLineJpaEntity toEntity(
            CreateExecutionLineRequest request,
            ExecutionJpaEntity execution
    );

    /**
     * Convierte una entidad de linea de ejecucion en su DTO de salida.
     *
     * @param entity entidad de linea de ejecucion.
     * @return DTO de respuesta de la linea de ejecucion.
     */
    @Mapping(
            target = "executionNumber",
            source = "execution.executionNumber"
    )
    ExecutionLineResponse toResponse(
            ExecutionLineJpaEntity entity
    );

    /**
     * Copia los campos presentes de una peticion parcial sobre una entidad existente.
     *
     * @param request datos opcionales a aplicar.
     * @param entity  entidad gestionada que debe actualizarse.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "execution", ignore = true)
    @Mapping(target = "lineNumber", ignore = true)
    @Mapping(target = "active", ignore = true)
    void patchEntityFromRequest(
            PatchExecutionLineRequest request,
            @MappingTarget ExecutionLineJpaEntity entity
    );
}
