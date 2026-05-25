package com.acoes.fleetmanagement.workshop.order.application;

import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.CreateOrderLineRequest;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.OrderLineResponse;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.PatchOrderLineRequest;

import java.util.List;

/**
 * Define las operaciones de aplicacion disponibles para lineas de orden de taller.
 */
public interface OrderLineService {

    /**
     * Crea una linea dentro de una orden de taller activa.
     *
     * @param orderNumber numero funcional de la orden.
     * @param request datos necesarios para crear la linea.
     * @return linea creada.
     */
    OrderLineResponse create(
            String orderNumber,
            CreateOrderLineRequest request
    );

    /**
     * Obtiene las lineas activas de una orden por su identificador interno.
     *
     * @param workshopOrderId identificador interno de la orden.
     * @return lista de lineas activas.
     */
    List<OrderLineResponse> findByWorkshopOrder(Long workshopOrderId);

    /**
     * Obtiene las lineas activas de una orden por su numero funcional.
     *
     * @param orderNumber numero funcional de la orden.
     * @return lista de lineas activas.
     */
    List<OrderLineResponse> findByWorkshopOrderNumber(String orderNumber);

    /**
     * Actualiza parcialmente una linea activa.
     *
     * @param id identificador interno de la linea.
     * @param request datos opcionales a modificar.
     * @return linea actualizada.
     */
    OrderLineResponse patch(Long id, PatchOrderLineRequest request);

    /**
     * Actualiza parcialmente una linea activa usando la orden y numero de linea.
     *
     * @param orderNumber numero funcional de la orden.
     * @param lineNumber numero de linea dentro de la orden.
     * @param request datos opcionales a modificar.
     * @return linea actualizada.
     */
    OrderLineResponse patchByOrderNumberAndLineNumber(
            String orderNumber,
            Integer lineNumber,
            PatchOrderLineRequest request
    );

    /**
     * Da de baja logicamente una linea activa.
     *
     * @param id identificador interno de la linea.
     */
    void deactivate(Long id);
}
