package com.acoes.fleetmanagement.workshop.order.application;

import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.CreateOrderRequest;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.OrderDetailResponse;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.OrderResponse;

import java.util.List;

/**
 * Define las operaciones de aplicacion disponibles para ordenes de taller.
 */
public interface OrderService {

    /**
     * Crea una orden de taller para un vehiculo activo.
     *
     * @param request datos necesarios para abrir la orden.
     * @return orden creada.
     */
    OrderResponse create(CreateOrderRequest request);

    /**
     * Obtiene todas las ordenes de taller activas.
     *
     * @return lista de ordenes activas.
     */
    List<OrderResponse> findAll();

    /**
     * Busca una orden activa por su identificador interno.
     *
     * @param id identificador interno de la orden.
     * @return orden encontrada.
     */
    OrderResponse findById(Long id);

    /**
     * Busca una orden activa por su numero funcional.
     *
     * @param orderNumber numero funcional de la orden.
     * @return orden encontrada.
     */
    OrderResponse findByOrderNumber(String orderNumber);

    /**
     * Busca ordenes activas asociadas a un vehiculo.
     *
     * @param vehicleId identificador interno del vehiculo.
     * @return lista de ordenes del vehiculo.
     */
    List<OrderResponse> findByVehicleId(Long vehicleId);

    /**
     * Busca ordenes activas por matricula de vehiculo.
     *
     * @param plateNumber matricula del vehiculo.
     * @return lista de ordenes asociadas.
     */
    List<OrderResponse> findByVehiclePlateNumber(String plateNumber);

    /**
     * Busca ordenes activas por VIN de vehiculo.
     *
     * @param vin VIN del vehiculo.
     * @return lista de ordenes asociadas.
     */
    List<OrderResponse> findByVehicleVin(String vin);

    /**
     * Recupera una orden activa junto con sus lineas activas.
     *
     * @param orderNumber numero funcional de la orden.
     * @return detalle de la orden con sus lineas.
     */
    OrderDetailResponse findOrderWithLinesByOrderNumber(String orderNumber);

    /**
     * Da de baja logicamente una orden activa.
     *
     * @param id identificador interno de la orden.
     */
    void deactivate(Long id);
}
