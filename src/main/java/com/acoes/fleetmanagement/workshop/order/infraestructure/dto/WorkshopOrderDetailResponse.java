package com.acoes.fleetmanagement.workshop.order.infraestructure.dto;

import com.acoes.fleetmanagement.workshop.order.domain.model.WorkshopOrderStatus;

import java.time.LocalDate;
import java.util.List;

public record WorkshopOrderDetailResponse(
        Long id,
        String orderNumber,
        Long vehicleId,
        String vehiclePlateNumber,
        WorkshopOrderStatus status,
        LocalDate openingDate,
        LocalDate closingDate,
        boolean active,
        List<WorkshopOrderLineResponse> lines
) {
}
