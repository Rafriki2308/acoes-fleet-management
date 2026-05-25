package com.acoes.fleetmanagement.workshop.execution.application.mapper;

import com.acoes.fleetmanagement.workshop.execution.domain.ExecutionJpaEntity;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.CreateExecutionRequest;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.ExecutionResponse;
import com.acoes.fleetmanagement.workshop.order.domain.OrderJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapea entre entidades de ejecucion y DTOs de entrada o salida.
 */
@Mapper(componentModel = "spring")
public interface ExecutionMapper {

    /**
     * Convierte una peticion de creacion y su orden en una ejecucion nueva.
     *
     * @param request datos recibidos para crear la ejecucion.
     * @param workshopOrder orden activa asociada a la ejecucion.
     * @return entidad de ejecucion sin persistir.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "executionNumber", ignore = true)
    @Mapping(target = "workshopOrder", source = "workshopOrder")
    @Mapping(target = "workshopOrderNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "finalSummary", ignore = true)
    @Mapping(target = "active", constant = "true")
    ExecutionJpaEntity toEntity(
            CreateExecutionRequest request,
            OrderJpaEntity workshopOrder
    );

    @Mapping(
            target = "workshopOrderId",
            source = "workshopOrder.id"
    )
    /**
     * Convierte una entidad de ejecucion en su DTO de salida.
     *
     * @param entity entidad de ejecucion.
     * @return DTO de respuesta de la ejecucion.
     */
    ExecutionResponse toResponse(
            ExecutionJpaEntity entity
    );
}
