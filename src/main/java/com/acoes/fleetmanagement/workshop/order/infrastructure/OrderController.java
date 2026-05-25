package com.acoes.fleetmanagement.workshop.order.infrastructure;

import com.acoes.fleetmanagement.workshop.order.application.OrderLineService;
import com.acoes.fleetmanagement.workshop.order.application.OrderService;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.EndpointConstants.*;

/**
 * Expone los endpoints REST para gestionar ordenes de taller y sus lineas.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(ORDERS)
public class OrderController {

    private final OrderService orderService;

    private final OrderLineService orderLineService;

    /**
     * Crea una orden de taller.
     *
     * @param request datos de la orden a crear.
     * @return orden creada.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.create(request);
    }

    /**
     * Crea una linea dentro de una orden de taller.
     *
     * @param orderNumber numero funcional de la orden.
     * @param request datos de la linea a crear.
     * @return linea creada.
     */
    @PostMapping(CREATE_LINE_BY_ORDER)
    @ResponseStatus(HttpStatus.CREATED)
    public OrderLineResponse createLine(
            @PathVariable String orderNumber,
            @Valid @RequestBody CreateOrderLineRequest request
    ) {
        return orderLineService.create(orderNumber, request);
    }

    /**
     * Lista todas las ordenes activas.
     *
     * @return ordenes activas.
     */
    @GetMapping
    public List<OrderResponse> findAll() {
        return orderService.findAll();
    }

    /**
     * Obtiene una orden activa por id.
     *
     * @param id identificador interno de la orden.
     * @return orden encontrada.
     */
    @GetMapping(GET_BY_ID)
    public OrderResponse findById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    /**
     * Obtiene una orden activa por numero funcional.
     *
     * @param orderNumber numero funcional de la orden.
     * @return orden encontrada.
     */
    @GetMapping(GET_ORDER_BY_NUMBER)
    public OrderResponse findByOrderNumber(@PathVariable String orderNumber) {
        return orderService.findByOrderNumber(orderNumber);
    }

    /**
     * Lista ordenes activas de un vehiculo.
     *
     * @param vehicleId identificador interno del vehiculo.
     * @return ordenes asociadas al vehiculo.
     */
    @GetMapping(GET_ORDER_BY_VEHICLE)
    public List<OrderResponse> findByVehicleId(@PathVariable Long vehicleId) {
        return orderService.findByVehicleId(vehicleId);
    }

    /**
     * Lista ordenes activas por matricula de vehiculo.
     *
     * @param plateNumber matricula del vehiculo.
     * @return ordenes asociadas al vehiculo.
     */
    @GetMapping(GET_ORDER_BY_PLATE)
    public List<OrderResponse> findByVehiclePlateNumber(@PathVariable String plateNumber) {
        return orderService.findByVehiclePlateNumber(plateNumber);
    }

    /**
     * Lista ordenes activas por VIN de vehiculo.
     *
     * @param vin VIN del vehiculo.
     * @return ordenes asociadas al vehiculo.
     */
    @GetMapping(GET_ORDER_BY_VIN)
    public List<OrderResponse> findByVehicleVin(@PathVariable String vin) {
        return orderService.findByVehicleVin(vin);
    }

    /**
     * Obtiene una orden activa con sus lineas.
     *
     * @param orderNumber numero funcional de la orden.
     * @return detalle de la orden.
     */
    @GetMapping(GET_ORDER_DETAIL_BY_NUMBER)
    public OrderDetailResponse findOrderWithLinesByOrderNumber(
            @PathVariable String orderNumber
    ) {
        return orderService.findOrderWithLinesByOrderNumber(orderNumber);
    }

    /**
     * Da de baja logicamente una orden activa.
     *
     * @param id identificador interno de la orden.
     */
    @PatchMapping(PATCH_OFF_BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        orderService.deactivate(id);
    }
}
