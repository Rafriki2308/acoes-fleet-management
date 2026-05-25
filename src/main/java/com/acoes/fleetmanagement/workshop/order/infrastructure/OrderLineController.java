package com.acoes.fleetmanagement.workshop.order.infrastructure;

import com.acoes.fleetmanagement.workshop.order.application.OrderLineService;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.OrderLineResponse;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.PatchOrderLineRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.EndpointConstants.*;

/**
 * Expone los endpoints REST para consultar y actualizar lineas de orden de taller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(ORDER_LINES)
public class OrderLineController {

    private final OrderLineService orderLineService;

    /**
     * Lista lineas activas por id de orden.
     *
     * @param workshopOrderId identificador interno de la orden.
     * @return lineas activas de la orden.
     */
    @GetMapping(GET_LINES_BY_ORDER_ID)
    public List<OrderLineResponse> findByWorkshopOrder(
            @PathVariable Long workshopOrderId
    ) {
        return orderLineService.findByWorkshopOrder(workshopOrderId);
    }

    /**
     * Lista lineas activas por numero funcional de orden.
     *
     * @param orderNumber numero funcional de la orden.
     * @return lineas activas de la orden.
     */
    @GetMapping(GET_LINES_BY_ORDER_NUMBER)
    public List<OrderLineResponse> findByWorkshopOrderNumber(
            @PathVariable String orderNumber
    ) {
        return orderLineService.findByWorkshopOrderNumber(orderNumber);
    }

    /**
     * Actualiza parcialmente una linea activa por id.
     *
     * @param id      identificador interno de la linea.
     * @param request datos opcionales a modificar.
     * @return linea actualizada.
     */
    @PatchMapping(PATCH_BY_ID)
    public OrderLineResponse patch(
            @PathVariable Long id,
            @Valid @RequestBody PatchOrderLineRequest request
    ) {
        return orderLineService.patch(id, request);
    }

    /**
     * Actualiza parcialmente una linea activa por numero de orden y linea.
     *
     * @param orderNumber numero funcional de la orden.
     * @param lineNumber  numero de linea dentro de la orden.
     * @param request     datos opcionales a modificar.
     * @return linea actualizada.
     */
    @PatchMapping(PATCH_LINE_BY_ORDER_NUMBER)
    public OrderLineResponse patchByOrderNumberAndLineNumber(
            @PathVariable String orderNumber,
            @PathVariable Integer lineNumber,
            @RequestBody PatchOrderLineRequest request
    ) {
        return orderLineService.patchByOrderNumberAndLineNumber(
                orderNumber,
                lineNumber,
                request
        );
    }

    /**
     * Da de baja logicamente una linea activa.
     *
     * @param id identificador interno de la linea.
     */
    @PatchMapping(PATCH_OFF_BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        orderLineService.deactivate(id);
    }
}
