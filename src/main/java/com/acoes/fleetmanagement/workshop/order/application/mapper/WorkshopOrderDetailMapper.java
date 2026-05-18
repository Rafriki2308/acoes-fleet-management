package com.acoes.fleetmanagement.workshop.order.application.mapper;

import com.acoes.fleetmanagement.workshop.order.domain.WorkshopOrderJpaEntity;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.WorkshopOrderDetailResponse;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.WorkshopOrderLineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkshopOrderDetailMapper {

    @Mapping(target = "vehicleId", source = "order.vehicle.id")
    @Mapping(target = "lines", source = "lines")
    WorkshopOrderDetailResponse toResponse(
            WorkshopOrderJpaEntity order,
            List<WorkshopOrderLineResponse> lines
    );
}