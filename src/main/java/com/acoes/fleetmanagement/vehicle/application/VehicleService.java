package com.acoes.fleetmanagement.vehicle.application;

import com.acoes.fleetmanagement.vehicle.infraestructure.dto.CreateVehicleRequest;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.PatchVehicleRequest;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.UpdateVehicleRequest;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.VehicleResponse;

import java.util.List;

public interface VehicleService {

    VehicleResponse create(CreateVehicleRequest request);

    List<VehicleResponse> findAll();

    VehicleResponse findById(Long id);

    VehicleResponse update(Long id, UpdateVehicleRequest request);

    VehicleResponse patch(Long id, PatchVehicleRequest request);

    void deactivate(Long id);   // baja lógica
}
