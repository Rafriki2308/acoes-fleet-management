package com.acoes.fleetmanagement.workshop.order.infraestructure;

import com.acoes.fleetmanagement.workshop.order.application.WorkshopOrderLineService;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.PatchWorkshopOrderLineRequest;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.WorkshopOrderLineResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.EndpointConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(MAIN_WORKSHOP_ORDER_LINE)
public class WorkshopOrderLineController {

    private final WorkshopOrderLineService workshopOrderLineService;

    @GetMapping(FIND_LINES_BY_WORKSHOP_ORDER_ID)
    public List<WorkshopOrderLineResponse> findByWorkshopOrder(
            @PathVariable Long workshopOrderId
    ) {
        return workshopOrderLineService.findByWorkshopOrder(workshopOrderId);
    }

    @GetMapping(FIND_LINES_BY_WORKSHOP_ORDER_NUMBER)
    public List<WorkshopOrderLineResponse> findByWorkshopOrderNumber(
            @PathVariable String orderNumber
    ) {
        return workshopOrderLineService.findByWorkshopOrderNumber(orderNumber);
    }

    @PatchMapping(BY_ID)
    public WorkshopOrderLineResponse patch(
            @PathVariable Long id,
            @Valid @RequestBody PatchWorkshopOrderLineRequest request
    ) {
        return workshopOrderLineService.patch(id, request);
    }

    @PatchMapping(PATCH_LINE_BY_WORKSHOP_ORDER_NUMBER)
    public WorkshopOrderLineResponse patchByOrderNumberAndLineNumber(
            @PathVariable String orderNumber,
            @PathVariable Integer lineNumber,
            @RequestBody PatchWorkshopOrderLineRequest request
    ) {
        return workshopOrderLineService.patchByOrderNumberAndLineNumber(
                orderNumber,
                lineNumber,
                request
        );
    }

    @PatchMapping(DEACTIVATE_BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        workshopOrderLineService.deactivate(id);
    }
}
