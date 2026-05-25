package com.acoes.fleetmanagement.vehicle.infrastructure;

import com.acoes.fleetmanagement.vehicle.application.VehicleService;
import com.acoes.fleetmanagement.vehicle.infrastructure.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.EndpointConstants.*;

/**
 * Expone los endpoints REST para consultar y administrar vehiculos.
 */
@RestController
@RequestMapping(VEHICLES)
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * Crea un vehiculo.
     *
     * @param request datos del vehiculo a crear.
     * @return vehiculo creado.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse create(@Valid @RequestBody CreateVehicleRequest request) {
        return vehicleService.create(request);
    }

    /**
     * Lista todos los vehiculos activos.
     *
     * @return vehiculos activos.
     */
    @GetMapping
    public List<VehicleResponse> findAll() {
        return vehicleService.findAll();
    }

    /**
     * Obtiene un vehiculo activo por id.
     *
     * @param id identificador interno del vehiculo.
     * @return vehiculo encontrado.
     */
    @GetMapping(GET_BY_ID)
    public VehicleResponse findById(@PathVariable Long id) {
        return vehicleService.findById(id);
    }

    /**
     * Obtiene un vehiculo activo por matricula.
     *
     * @param plateNumber matricula del vehiculo.
     * @return vehiculo encontrado.
     */
    @GetMapping(GET_VEHICLE_BY_PLATE)
    public VehicleResponse findByPlateNumber(@PathVariable String plateNumber) {
        return vehicleService.findByPlateNumber(plateNumber);
    }

    /**
     * Obtiene un vehiculo activo por VIN.
     *
     * @param vin VIN del vehiculo.
     * @return vehiculo encontrado.
     */
    @GetMapping(GET_VEHICLE_BY_VIN)
    public VehicleResponse findByVin(@PathVariable String vin) {
        return vehicleService.findByVin(vin);
    }

    /**
     * Reemplaza los datos principales de un vehiculo activo.
     *
     * @param id identificador interno del vehiculo.
     * @param request datos nuevos del vehiculo.
     * @return vehiculo actualizado.
     */
    @PutMapping(PUT_BY_ID)
    public VehicleResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleRequest request
    ) {
        return vehicleService.update(id, request);
    }

    /**
     * Actualiza parcialmente un vehiculo activo.
     *
     * @param id identificador interno del vehiculo.
     * @param request datos opcionales a modificar.
     * @return vehiculo actualizado.
     */
    @PatchMapping(PATCH_BY_ID)
    public VehicleResponse patch(
            @PathVariable Long id,
            @RequestBody PatchVehicleRequest request
    ) {
        return vehicleService.patch(id, request);
    }

    /**
     * Asigna el garaje actual de un vehiculo.
     *
     * @param id identificador interno del vehiculo.
     * @param request garaje a asignar.
     * @return vehiculo actualizado.
     */
    @PatchMapping(PATCH_GARAGE_BY_ID)
    public VehicleResponse assignGarage(
            @PathVariable Long id,
            @Valid @RequestBody AssignGarageRequest request
    ) {
        return vehicleService.assignGarage(id, request);
    }

    /**
     * Da de baja logicamente un vehiculo activo.
     *
     * @param id identificador interno del vehiculo.
     */
    @PatchMapping(PATCH_OFF_BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        vehicleService.deactivate(id);
    }
}
