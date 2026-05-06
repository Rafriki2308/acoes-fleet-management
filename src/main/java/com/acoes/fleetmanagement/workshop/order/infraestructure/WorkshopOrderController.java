package com.acoes.fleetmanagement.workshop.order.infraestructure;

import com.acoes.fleetmanagement.workshop.order.application.WorkshopOrderService;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.CreateWorkshopOrderRequest;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.WorkshopOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.EndpointConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(MAIN_WORKSHOP_ORDER_ENDPOINT)
public class WorkshopOrderController {

    @Autowired
    private WorkshopOrderService workshopOrderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkshopOrderResponse create(@Valid @RequestBody CreateWorkshopOrderRequest request) {
        return workshopOrderService.create(request);
    }

    @GetMapping
    public List<WorkshopOrderResponse> findAll() {
        return workshopOrderService.findAll();
    }

    @GetMapping(BY_ID)
    public WorkshopOrderResponse findById(@PathVariable Long id) {
        return workshopOrderService.findById(id);
    }

    @GetMapping(FIND_WORKSHOP_ORDER_BY_NUMBER)
    public WorkshopOrderResponse findByOrderNumber(@PathVariable String orderNumber) {
        return workshopOrderService.findByOrderNumber(orderNumber);
    }

    @GetMapping(FIND_WORKSHOP_ORDER_BY_VEHICLE_ID)
    public List<WorkshopOrderResponse> findByVehicleId(@PathVariable Long vehicleId) {
        return workshopOrderService.findByVehicleId(vehicleId);
    }

    @GetMapping(FIND_WORKSHOP_ORDER_BY_PLATE_NUMBER)
    public List<WorkshopOrderResponse> findByVehiclePlateNumber(@PathVariable String plateNumber) {
        return workshopOrderService.findByVehiclePlateNumber(plateNumber);
    }

    @GetMapping(FIND_WORKSHOP_ORDER_BY_VIN)
    public List<WorkshopOrderResponse> findByVehicleVin(@PathVariable String vin) {
        return workshopOrderService.findByVehicleVin(vin);
    }

    @PatchMapping(DEACTIVATE_BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        workshopOrderService.deactivate(id);
    }
}
