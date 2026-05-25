package com.acoes.fleetmanagement.workshop.order.infrastructure.dto;

import com.acoes.fleetmanagement.workshop.order.domain.model.OrderStatus;

import java.time.LocalDate;
import java.util.List;
/**
 * DTO de salida con la informacion de una orden y sus lineas asociadas.
 */

public record OrderDetailResponse(
        Long id,
        String orderNumber,
        Long vehicleId,
        String vehiclePlateNumber,
        OrderStatus status,
        LocalDate openingDate,
        LocalDate closingDate,
        boolean active,
        List<OrderLineResponse> lines
) {
}
