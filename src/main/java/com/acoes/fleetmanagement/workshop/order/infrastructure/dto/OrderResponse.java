package com.acoes.fleetmanagement.workshop.order.infrastructure.dto;

import com.acoes.fleetmanagement.workshop.order.domain.model.OrderStatus;

import java.time.LocalDate;
/**
 * DTO de salida con la informacion resumida de una orden de taller.
 */

public record OrderResponse(
        Long id,
        String orderNumber,
        Long vehicleId,
        String vehiclePlateNumber,
        OrderStatus status,
        LocalDate openingDate,
        LocalDate closingDate,
        boolean active
) {
}
