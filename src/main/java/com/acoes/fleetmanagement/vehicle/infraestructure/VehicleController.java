package com.acoes.fleetmanagement.vehicle.infraestructure;

import com.acoes.fleetmanagement.vehicle.application.VehicleService;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.EndpointConstants.*;

@RestController
@RequestMapping(MAIN_VEHICLE_ENDPOINT)
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse create(@Valid @RequestBody CreateVehicleRequest request) {
        return vehicleService.create(request);
    }

    @GetMapping
    public List<VehicleResponse> findAll() {
        return vehicleService.findAll();
    }

    @GetMapping(BY_ID)
    public VehicleResponse findById(@PathVariable Long id) {
        return vehicleService.findById(id);
    }

    @GetMapping(FIND_VEHICLE_BY_PLATE)
    public VehicleResponse findByPlateNumber(@PathVariable String plateNumber) {
        return vehicleService.findByPlateNumber(plateNumber);
    }

    @GetMapping(FIND_VEHICLE_BY_VIN)
    public VehicleResponse findByVin(@PathVariable String vin) {
        return vehicleService.findByVin(vin);
    }

    @PutMapping(BY_ID)
    public VehicleResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleRequest request
    ) {
        return vehicleService.update(id, request);
    }

    @PatchMapping(BY_ID)
    public VehicleResponse patch(
            @PathVariable Long id,
            @RequestBody PatchVehicleRequest request
    ) {
        return vehicleService.patch(id, request);
    }

    @PatchMapping(ASSIGN_GARAGE_ENDPOINT)
    public VehicleResponse assignGarage(
            @PathVariable Long id,
            @Valid @RequestBody AssignGarageRequest request
    ) {
        return vehicleService.assignGarage(id, request);
    }

    @PatchMapping(DEACTIVATE_BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        vehicleService.deactivate(id);
    }
}
