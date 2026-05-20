package com.acoes.fleetmanagement.workshop.execution.application.mapper;

import com.acoes.fleetmanagement.workshop.execution.domain.WorkshopExecutionJpaEntity;
import com.acoes.fleetmanagement.workshop.execution.infractrusture.dto.CreateExecutionRequest;
import com.acoes.fleetmanagement.workshop.execution.infractrusture.dto.ExecutionResponse;
import com.acoes.fleetmanagement.workshop.order.domain.WorkshopOrderJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkshopExecutionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "executionNumber", ignore = true)
    @Mapping(target = "workshopOrder", source = "workshopOrder")
    @Mapping(target = "workshopOrderNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "active", constant = "true")
    WorkshopExecutionJpaEntity toEntity(
            CreateExecutionRequest request,
            WorkshopOrderJpaEntity workshopOrder
    );

    @Mapping(
            target = "workshopOrderId",
            source = "workshopOrder.id"
    )
    ExecutionResponse toResponse(
            WorkshopExecutionJpaEntity entity
    );
}
