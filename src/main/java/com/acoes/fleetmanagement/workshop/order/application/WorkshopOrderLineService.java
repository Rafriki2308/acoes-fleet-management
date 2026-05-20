package com.acoes.fleetmanagement.workshop.order.application;

import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.CreateWorkshopOrderLineRequest;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.PatchWorkshopOrderLineRequest;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.WorkshopOrderLineResponse;

import java.util.List;

public interface WorkshopOrderLineService {

    WorkshopOrderLineResponse create(
            String orderNumber,
            CreateWorkshopOrderLineRequest request
    );

    List<WorkshopOrderLineResponse> findByWorkshopOrder(Long workshopOrderId);

    List<WorkshopOrderLineResponse> findByWorkshopOrderNumber(String orderNumber);

    WorkshopOrderLineResponse patch(Long id, PatchWorkshopOrderLineRequest request);

    WorkshopOrderLineResponse patchByOrderNumberAndLineNumber(
            String orderNumber,
            Integer lineNumber,
            PatchWorkshopOrderLineRequest request
    );

    void deactivate(Long id);
}
