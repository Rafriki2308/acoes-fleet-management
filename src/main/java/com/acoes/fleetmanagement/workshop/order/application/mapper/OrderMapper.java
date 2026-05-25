package com.acoes.fleetmanagement.workshop.order.application.mapper;

import com.acoes.fleetmanagement.vehicle.domain.VehicleJpaEntity;
import com.acoes.fleetmanagement.workshop.order.domain.OrderJpaEntity;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.CreateOrderRequest;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

/**
 * Mapea entre entidades de orden de taller y DTOs de entrada o salida.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    /**
     * Convierte una peticion de creacion y su vehiculo en una orden nueva.
     *
     * @param request datos recibidos para crear la orden.
     * @param vehicle vehiculo activo asociado a la orden.
     * @return entidad de orden sin persistir.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "status", constant = "OPEN")
    @Mapping(target = "vehicle", source = "vehicle")
    @Mapping(target = "vehiclePlateNumber", source = "vehicle.plateNumber")
    @Mapping(target = "openingDate", expression = "java(resolveOpeningDate(request.openingDate()))")
    @Mapping(target = "closingDate", ignore = true)
    OrderJpaEntity toEntity(CreateOrderRequest request, VehicleJpaEntity vehicle);

    /**
     * Convierte una entidad de orden en su DTO de salida.
     *
     * @param entity entidad de orden.
     * @return DTO de respuesta de la orden.
     */
    @Mapping(target = "vehicleId", source = "vehicle.id")
    OrderResponse toResponse(OrderJpaEntity entity);

    /**
     * Resuelve la fecha de apertura que debe aplicarse a una orden nueva.
     *
     * @param openingDate fecha solicitada por el cliente.
     * @return fecha solicitada o la fecha actual si no se informo.
     */
    default LocalDate resolveOpeningDate(LocalDate openingDate) {
        return openingDate != null ? openingDate : LocalDate.now();
    }
}
