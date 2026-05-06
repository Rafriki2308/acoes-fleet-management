package com.acoes.fleetmanagement.workshop.order.application.mapper;

import com.acoes.fleetmanagement.vehicle.domain.VehicleJpaEntity;
import com.acoes.fleetmanagement.workshop.order.domain.WorkshopOrderJpaEntity;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.CreateWorkshopOrderRequest;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.WorkshopOrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface WorkshopOrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "status", constant = "OPEN")
    @Mapping(target = "vehicle", source = "vehicle")
    @Mapping(target = "vehiclePlateNumber", source = "vehicle.plateNumber")
    @Mapping(target = "openingDate", expression = "java(resolveOpeningDate(request.openingDate()))")
    @Mapping(target = "closingDate", ignore = true)
    WorkshopOrderJpaEntity toEntity(CreateWorkshopOrderRequest request, VehicleJpaEntity vehicle);

    @Mapping(target = "vehicleId", source = "vehicle.id")
    WorkshopOrderResponse toResponse(WorkshopOrderJpaEntity entity);

    default LocalDate resolveOpeningDate(LocalDate openingDate) {
        return openingDate != null ? openingDate : LocalDate.now();
    }
}
