package com.acoes.fleetmanagement.vehicle.application.Impl;

import com.acoes.fleetmanagement.garage.domain.GarageJpaEntity;
import com.acoes.fleetmanagement.garage.domain.repository.GarageJpaRepository;
import com.acoes.fleetmanagement.shared.exception.DuplicateResourceException;
import com.acoes.fleetmanagement.shared.exception.ResourceNotFoundException;
import com.acoes.fleetmanagement.shared.validation.NormalizatedTextUtil;
import com.acoes.fleetmanagement.shared.validation.VehicleNormalizationUtils;
import com.acoes.fleetmanagement.vehicle.application.VehicleService;
import com.acoes.fleetmanagement.vehicle.application.mapper.VehicleMapper;
import com.acoes.fleetmanagement.vehicle.domain.VehicleJpaEntity;
import com.acoes.fleetmanagement.vehicle.domain.repository.VehicleJpaRepository;
import com.acoes.fleetmanagement.vehicle.infrastructure.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.ExceptionMessageConstans.*;

/**
 * Implementa las reglas de negocio y persistencia para vehiculos.
 */
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleJpaRepository vehicleRepository;

    private final GarageJpaRepository garageRepository;

    private final VehicleMapper vehicleMapper;


    /**
     * Crea un vehiculo activo tras validar unicidad de matricula y VIN, resolver su garaje y normalizar sus campos.
     *
     * @param request datos necesarios para crear el vehiculo.
     * @return vehiculo creado.
     * @throws DuplicateResourceException si la matricula o el VIN ya existen.
     * @throws ResourceNotFoundException si el garaje indicado no existe.
     */
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

    /**
     * Obtiene todos los vehiculos activos registrados.
     *
     * @return lista de vehiculos activos.
     */
    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> findAll() {
        return vehicleRepository.findByActiveTrue()
                .stream()
                .map(vehicleMapper::toResponse)
                .toList();
    }

    /**
     * Busca un vehiculo activo por su identificador interno.
     *
     * @param id identificador interno del vehiculo.
     * @return vehiculo encontrado.
     * @throws ResourceNotFoundException si no existe un vehiculo activo con ese id.
     */
    @Override
    @Transactional(readOnly = true)
    public VehicleResponse findById(Long id) {

        return vehicleMapper.toResponse(findActiveVehicleById(id));
    }

    /**
     * Reemplaza los datos principales de un vehiculo activo y vuelve a normalizar sus campos.
     *
     * @param id identificador interno del vehiculo.
     * @param request datos completos que reemplazan el estado actual.
     * @return vehiculo actualizado.
     * @throws DuplicateResourceException si la nueva matricula o VIN ya pertenecen a otro vehiculo.
     * @throws ResourceNotFoundException si el vehiculo o garaje indicado no existe.
     */
    @Override
    @Transactional
    public VehicleResponse update(Long id, UpdateVehicleRequest request) {

        VehicleJpaEntity vehicle = findActiveVehicleById(id);

        validatePlateNumberUniqueness(request.plateNumber(), vehicle);
        validateVinUniqueness(request.vin(), vehicle);

        // MapStruct actualiza los campos simples.
        vehicleMapper.updateEntityFromRequest(request, vehicle);

        normalizeVehicleFields(vehicle);

        assignGarage(vehicle, request.currentGarageId());

        return vehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }

    /**
     * Actualiza parcialmente un vehiculo activo, validando cambios sensibles como matricula y VIN.
     *
     * @param id identificador interno del vehiculo.
     * @param request datos opcionales a modificar.
     * @return vehiculo actualizado.
     * @throws DuplicateResourceException si la matricula o VIN informados ya pertenecen a otro vehiculo.
     * @throws ResourceNotFoundException si no existe un vehiculo activo con ese id.
     */
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

    /**
     * Asigna un garaje existente como ubicacion actual del vehiculo.
     *
     * @param vehicleId identificador interno del vehiculo.
     * @param request peticion con el identificador del garaje a asignar.
     * @return vehiculo actualizado con su garaje actual.
     * @throws ResourceNotFoundException si el vehiculo o el garaje no existen.
     */
    @Override
    @Transactional
    public VehicleResponse assignGarage(Long vehicleId, AssignGarageRequest request) {

        VehicleJpaEntity vehicle = findActiveVehicleById(vehicleId);

        assignGarageToVehicle(vehicle, request.garageId());

        return vehicleMapper.toResponse(vehicle);
    }

    /**
     * Da de baja logicamente un vehiculo activo.
     *
     * @param id identificador interno del vehiculo.
     * @throws ResourceNotFoundException si no existe un vehiculo activo con ese id.
     */
    @Override
    @Transactional
    public void deactivate(Long id) {
        VehicleJpaEntity vehicle = findActiveVehicleById(id);

        deactivateVehicle(vehicle);

    }

    /**
     * Marca un vehiculo como inactivo sin eliminarlo fisicamente.
     *
     * @param vehicle vehiculo gestionado por Hibernate.
     */
    private void deactivateVehicle(VehicleJpaEntity vehicle) {
        // Hibernate persistira este cambio por dirty checking.
        vehicle.setActive(false);
    }

    /**
     * Recupera un vehiculo activo o lanza una excepcion si no existe.
     *
     * @param id identificador interno del vehiculo.
     * @return vehiculo activo encontrado.
     */
    private VehicleJpaEntity findActiveVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .filter(VehicleJpaEntity::isActive)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_BY_ID + id));
    }

    private void assignGarage(VehicleJpaEntity vehicle, Long garageId) {
        vehicle.setCurrentGarage(resolveGarage(garageId));
    }

    /**
     * Resuelve el garaje solicitado permitiendo que la asociacion sea nula.
     *
     * @param garageId identificador interno del garaje.
     * @return garaje encontrado o {@code null} si no se informo id.
     */
    private GarageJpaEntity resolveGarage(Long garageId) {

        if (garageId == null) {
            return null;
        }

        return garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException(GARAGE_NOT_FOUND_BY_ID + garageId));
    }

    /**
     * Busca un vehiculo activo por matricula normalizada.
     *
     * @param plateNumber matricula recibida.
     * @return vehiculo encontrado.
     * @throws ResourceNotFoundException si no existe un vehiculo activo con esa matricula.
     */
    @Override
    @Transactional(readOnly = true)
    public VehicleResponse findByPlateNumber(String plateNumber) {

        String normalizedPlateNumber = VehicleNormalizationUtils.normalizePlateNumber(plateNumber);

        VehicleJpaEntity vehicle = vehicleRepository.findByPlateNumberAndActiveTrue(normalizedPlateNumber)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_BY_PLATE_NUMBER + normalizedPlateNumber));

        return vehicleMapper.toResponse(vehicle);
    }

    /**
     * Busca un vehiculo activo por VIN normalizado.
     *
     * @param vin VIN recibido.
     * @return vehiculo encontrado.
     * @throws ResourceNotFoundException si no existe un vehiculo activo con ese VIN.
     */
    @Override
    @Transactional(readOnly = true)
    public VehicleResponse findByVin(String vin) {

        String normalizedVin = VehicleNormalizationUtils.normalizeVin(vin);

        VehicleJpaEntity vehicle = vehicleRepository.findByVinAndActiveTrue(normalizedVin)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_BY_VIN + normalizedVin));

        return vehicleMapper.toResponse(vehicle);
    }

    /**
     * Normaliza identificadores y campos textuales del vehiculo antes de persistir o responder.
     *
     * @param vehicle vehiculo que debe normalizarse.
     */
    private void normalizeVehicleFields(VehicleJpaEntity vehicle) {
        vehicle.setPlateNumber(VehicleNormalizationUtils.normalizePlateNumber(vehicle.getPlateNumber()));
        vehicle.setVin(VehicleNormalizationUtils.normalizeVin(vehicle.getVin()));
        vehicle.setBrand(NormalizatedTextUtil.normalizeUpper(vehicle.getBrand()));
        vehicle.setModel(NormalizatedTextUtil.normalizeUpper(vehicle.getModel()));
        vehicle.setColor(NormalizatedTextUtil.normalizeUpper(vehicle.getColor()));
    }

    /**
     * Valida que la matricula no este asignada a otro vehiculo.
     *
     * @param plateNumber matricula recibida.
     * @param currentVehicle vehiculo actual en operaciones de actualizacion.
     */
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

    /**
     * Valida que el VIN no este asignado a otro vehiculo.
     *
     * @param vin VIN recibido.
     * @param currentVehicle vehiculo actual en operaciones de actualizacion.
     */
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

    private void assignGarageToVehicle(VehicleJpaEntity vehicle, Long garageId) {
        vehicle.setCurrentGarage(resolveGarage(garageId));
    }
}
