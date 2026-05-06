package com.acoes.fleetmanagement.vehicle.application;

import com.acoes.fleetmanagement.vehicle.infraestructure.dto.*;

import java.util.List;

public interface VehicleService {

    VehicleResponse create(CreateVehicleRequest request);

    List<VehicleResponse> findAll();

    VehicleResponse findById(Long id);

    VehicleResponse update(Long id, UpdateVehicleRequest request);

    VehicleResponse patch(Long id, PatchVehicleRequest request);

    VehicleResponse assignGarage(Long vehicleId, AssignGarageRequest request);

    VehicleResponse findByPlateNumber(String plateNumber);

    VehicleResponse findByVin(String vin);

    void deactivate(Long id);   // baja lógica
}
