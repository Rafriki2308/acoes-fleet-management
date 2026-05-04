package com.acoes.fleetmanagement.vehicle.application.Impl;

import com.acoes.fleetmanagement.garage.domain.GarageJpaEntity;
import com.acoes.fleetmanagement.garage.domain.repository.GarageJpaRepository;
import com.acoes.fleetmanagement.shared.exception.DuplicateResourceException;
import com.acoes.fleetmanagement.shared.exception.ResourceNotFoundException;
import com.acoes.fleetmanagement.shared.validation.PlateNumberUtils;
import com.acoes.fleetmanagement.vehicle.application.VehicleService;
import com.acoes.fleetmanagement.vehicle.application.mapper.VehicleMapper;
import com.acoes.fleetmanagement.vehicle.domain.VehicleJpaEntity;
import com.acoes.fleetmanagement.vehicle.domain.repository.VehicleJpaRepository;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.CreateVehicleRequest;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.UpdateVehicleRequest;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.VehicleResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.ExceptionMessageConstants.VEHICLE_NOT_FOUND_BY_ID;

@Service
@AllArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private  VehicleJpaRepository vehicleRepository;

    @Autowired
    private  GarageJpaRepository garageRepository;

    @Autowired
    private  VehicleMapper vehicleMapper;


    @Override
    @Transactional
    public VehicleResponse create(CreateVehicleRequest request) {

        String normalizedPlate = PlateNumberUtils.normalize(request.plateNumber());
        validatePlateNumberUniqueness(normalizedPlate, null);
        validateVinUniqueness(request.vin(), null);

        VehicleJpaEntity vehicle = vehicleMapper.toEntity(request);
        assignGarage(vehicle, request.currentGarageId());

        return vehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional()
    public List<VehicleResponse> findAll() {
        return vehicleRepository.findByActiveTrue()
                .stream()
                .map(vehicleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional()
    public VehicleResponse findById(Long id) {

        return vehicleMapper.toResponse(findActiveVehicleById(id));
    }

    @Override
    @Transactional
    public VehicleResponse update(Long id, UpdateVehicleRequest request) {

        VehicleJpaEntity vehicle = findActiveVehicleById(id);

        String normalizedPlate = PlateNumberUtils.normalize(request.plateNumber());
        validatePlateNumberUniqueness(normalizedPlate, vehicle);
        validateVinUniqueness(request.vin(), vehicle);

        // MapStruct actualiza los campos simples
        vehicleMapper.updateEntityFromRequest(request, vehicle);

        // Asignación manual de relación
        assignGarage(vehicle, request.currentGarageId());

        return vehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        VehicleJpaEntity vehicle = findActiveVehicleById(id);

        vehicle.setActive(false);

        vehicleRepository.save(vehicle);
    }

    private VehicleJpaEntity findActiveVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .filter(VehicleJpaEntity::isActive)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_BY_ID + id));
    }

    private void assignGarage(VehicleJpaEntity vehicle, Long garageId) {
        vehicle.setCurrentGarage(resolveGarage(garageId));
    }

    private GarageJpaEntity resolveGarage(Long garageId) {

        if (garageId == null) {
            return null;
        }

        return garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found"));
    }

    private void validatePlateNumberUniqueness(String plateNumber, VehicleJpaEntity currentVehicle) {
        if (plateNumber == null) {
            return;
        }

        boolean isSameVehicle = currentVehicle != null
                && plateNumber.equals(currentVehicle.getPlateNumber());

        if (!isSameVehicle && vehicleRepository.existsByPlateNumber(plateNumber)) {
            throw new DuplicateResourceException("Plate number already exists");
        }
    }

    private void validateVinUniqueness(String vin, VehicleJpaEntity currentVehicle) {

        if (vin == null) {
            return;
        }

        boolean isSameVehicle = currentVehicle != null
                && vin.equals(currentVehicle.getVin());

        if (!isSameVehicle && vehicleRepository.existsByVin(vin)) {
            throw new DuplicateResourceException("VIN already exists");
        }
    }
}
