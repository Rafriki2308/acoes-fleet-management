package com.acoes.fleetmanagement.vehicle.application.Impl;

import com.acoes.fleetmanagement.garage.domain.GarageJpaEntity;
import com.acoes.fleetmanagement.garage.domain.repository.GarageJpaRepository;
import com.acoes.fleetmanagement.shared.exception.DuplicateResourceException;
import com.acoes.fleetmanagement.shared.exception.ResourceNotFoundException;
import com.acoes.fleetmanagement.shared.validation.VehicleNormalizationUtils;
import com.acoes.fleetmanagement.vehicle.application.VehicleService;
import com.acoes.fleetmanagement.vehicle.application.mapper.VehicleMapper;
import com.acoes.fleetmanagement.vehicle.domain.VehicleJpaEntity;
import com.acoes.fleetmanagement.vehicle.domain.repository.VehicleJpaRepository;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.CreateVehicleRequest;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.PatchVehicleRequest;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.UpdateVehicleRequest;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.VehicleResponse;
import com.acoes.fleetmanagement.shared.validation.NormalizatedTextUtil;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.ExceptionMessageConstans.*;

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

        validatePlateNumberUniqueness(request.plateNumber(), null);
        validateVinUniqueness(request.vin(), null);

        VehicleJpaEntity vehicle = vehicleMapper.toEntity(request);

        assignGarage(vehicle, request.currentGarageId());

        normalizeVehicleFields(vehicle);

        return vehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> findAll() {
        return vehicleRepository.findByActiveTrue()
                .stream()
                .map(vehicleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponse findById(Long id) {

        return vehicleMapper.toResponse(findActiveVehicleById(id));
    }

    @Override
    @Transactional
    public VehicleResponse update(Long id, UpdateVehicleRequest request) {

        VehicleJpaEntity vehicle = findActiveVehicleById(id);

        validatePlateNumberUniqueness(request.plateNumber(), vehicle);
        validateVinUniqueness(request.vin(), vehicle);

        // MapStruct actualiza los campos simples
        vehicleMapper.updateEntityFromRequest(request, vehicle);

        normalizeVehicleFields(vehicle);

        assignGarage(vehicle, request.currentGarageId());

        return vehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional
    public VehicleResponse patch(Long id, PatchVehicleRequest request) {

        VehicleJpaEntity vehicle = findActiveVehicleById(id);

        updatePlateNumberIfPresent(vehicle, request.plateNumber());
        updateVinIfPresent(vehicle, request.vin());

        vehicleMapper.patchEntityFromRequest(request, vehicle);

        normalizeVehicleFields(vehicle);

        return vehicleMapper.toResponse(vehicle);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        VehicleJpaEntity vehicle = findActiveVehicleById(id);

        deactivateVehicle(vehicle);

    }

    private void deactivateVehicle(VehicleJpaEntity vehicle) {
        // Managed entity → Hibernate will persist change automatically
        vehicle.setActive(false);
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
                .orElseThrow(() -> new ResourceNotFoundException(GARAGE_NOT_FOUND_BY_ID + garageId));
    }

    private void normalizeVehicleFields(VehicleJpaEntity vehicle) {
        vehicle.setPlateNumber(VehicleNormalizationUtils.normalizePlateNumber(vehicle.getPlateNumber()));
        vehicle.setVin(VehicleNormalizationUtils.normalizeVin(vehicle.getVin()));
        vehicle.setBrand(NormalizatedTextUtil.normalizeUpper(vehicle.getBrand()));
        vehicle.setModel(NormalizatedTextUtil.normalizeUpper(vehicle.getModel()));
        vehicle.setColor(NormalizatedTextUtil.normalizeUpper(vehicle.getColor()));
    }

    private void validatePlateNumberUniqueness(String plateNumber, VehicleJpaEntity currentVehicle) {
        if (plateNumber == null) {
            return;
        }
        String normalizePlate = VehicleNormalizationUtils.normalizePlateNumber(plateNumber);

        boolean isSameVehicle = currentVehicle != null
                && normalizePlate.equals(currentVehicle.getPlateNumber());

        if (!isSameVehicle && vehicleRepository.existsByPlateNumber(normalizePlate)) {
            throw new DuplicateResourceException(PLATE_NUMBER_ALREADY_EXIST + plateNumber);
        }
    }

    private void validateVinUniqueness(String vin, VehicleJpaEntity currentVehicle) {

        if (vin == null) {
            return;
        }

        String normalizeVin = VehicleNormalizationUtils.normalizeVin(vin);

        boolean isSameVehicle = currentVehicle != null
                && normalizeVin.equals(currentVehicle.getVin());

        if (!isSameVehicle && vehicleRepository.existsByVin(normalizeVin)) {
            throw new DuplicateResourceException(VIN_ALREADY_EXIST + vin);
        }
    }

    private void updatePlateNumberIfPresent(VehicleJpaEntity vehicle, String plateNumber) {
        if (plateNumber == null) {
            return;
        }

        String normalizedPlate = VehicleNormalizationUtils.normalizePlateNumber(plateNumber);

        validatePlateNumberUniqueness(normalizedPlate, vehicle);

        vehicle.setPlateNumber(normalizedPlate);
    }

    private void updateVinIfPresent(VehicleJpaEntity vehicle, String vin) {
        if (vin == null) {
            return;
        }

        String normalizedVin = VehicleNormalizationUtils.normalizeVin(vin);

        validateVinUniqueness(normalizedVin, vehicle);

        vehicle.setVin(normalizedVin);
    }
}
