package com.acoes.fleetmanagement.workshop.order.application;

import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.CreateWorkshopOrderRequest;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.WorkshopOrderDetailResponse;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.WorkshopOrderResponse;

import java.util.List;

public interface WorkshopOrderService {

    WorkshopOrderResponse create(CreateWorkshopOrderRequest request);

    List<WorkshopOrderResponse> findAll();

    WorkshopOrderResponse findById(Long id);

    WorkshopOrderResponse findByOrderNumber(String orderNumber);

    List<WorkshopOrderResponse> findByVehicleId(Long vehicleId);

    List<WorkshopOrderResponse> findByVehiclePlateNumber(String plateNumber);

    List<WorkshopOrderResponse> findByVehicleVin(String vin);

    WorkshopOrderDetailResponse findOrderWithLinesByOrderNumber(String orderNumber);

    void deactivate(Long id);
}
