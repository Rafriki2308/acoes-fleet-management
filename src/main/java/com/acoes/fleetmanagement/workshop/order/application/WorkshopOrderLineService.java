package com.acoes.fleetmanagement.workshop.order.application;

import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.CreateWorkshopOrderLineRequest;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.WorkshopOrderLineResponse;

import java.util.List;

public interface WorkshopOrderLineService {

    WorkshopOrderLineResponse create(CreateWorkshopOrderLineRequest request);

    List<WorkshopOrderLineResponse> findByWorkshopOrder(Long workshopOrderId);

    List<WorkshopOrderLineResponse> findByWorkshopOrderNumber(String orderNumber);

    void deactivate(Long id);
}
