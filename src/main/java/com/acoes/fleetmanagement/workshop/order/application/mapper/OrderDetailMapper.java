package com.acoes.fleetmanagement.workshop.order.application.mapper;

import com.acoes.fleetmanagement.workshop.order.domain.OrderJpaEntity;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.OrderDetailResponse;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.OrderLineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Construye la respuesta de detalle de una orden junto con sus lineas.
 */
@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    /**
     * Combina una orden y sus lineas en una respuesta de detalle.
     *
     * @param order orden activa encontrada.
     * @param lines lineas activas de la orden.
     * @return DTO de detalle de la orden.
     */
    @Mapping(target = "vehicleId", source = "order.vehicle.id")
    @Mapping(target = "lines", source = "lines")
    @Mapping(target = "id", ignore = true)
    OrderDetailResponse toResponse(
            OrderJpaEntity order,
            List<OrderLineResponse> lines
    );
}
