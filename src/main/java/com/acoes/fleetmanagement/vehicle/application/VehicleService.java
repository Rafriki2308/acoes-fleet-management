package com.acoes.fleetmanagement.vehicle.application;

import com.acoes.fleetmanagement.vehicle.infrastructure.dto.*;

import java.util.List;

/**
 * Define las operaciones de aplicacion disponibles para vehiculos.
 */
public interface VehicleService {

    /**
     * Crea un vehiculo activo con sus datos normalizados.
     *
     * @param request datos necesarios para crear el vehiculo.
     * @return vehiculo creado.
     */
    VehicleResponse create(CreateVehicleRequest request);

    /**
     * Obtiene todos los vehiculos activos.
     *
     * @return lista de vehiculos activos.
     */
    List<VehicleResponse> findAll();

    /**
     * Busca un vehiculo activo por su identificador interno.
     *
     * @param id identificador interno del vehiculo.
     * @return vehiculo encontrado.
     */
    VehicleResponse findById(Long id);

    /**
     * Reemplaza los datos principales de un vehiculo activo.
     *
     * @param id identificador interno del vehiculo.
     * @param request datos que reemplazan el estado actual.
     * @return vehiculo actualizado.
     */
    VehicleResponse update(Long id, UpdateVehicleRequest request);

    /**
     * Actualiza parcialmente los datos de un vehiculo activo.
     *
     * @param id identificador interno del vehiculo.
     * @param request datos opcionales a modificar.
     * @return vehiculo actualizado.
     */
    VehicleResponse patch(Long id, PatchVehicleRequest request);

    /**
     * Asigna el garaje actual de un vehiculo activo.
     *
     * @param vehicleId identificador interno del vehiculo.
     * @param request garaje que debe quedar asociado.
     * @return vehiculo actualizado con su garaje actual.
     */
    VehicleResponse assignGarage(Long vehicleId, AssignGarageRequest request);

    /**
     * Busca un vehiculo activo por matricula.
     *
     * @param plateNumber matricula a buscar.
     * @return vehiculo encontrado.
     */
    VehicleResponse findByPlateNumber(String plateNumber);

    /**
     * Busca un vehiculo activo por VIN.
     *
     * @param vin VIN a buscar.
     * @return vehiculo encontrado.
     */
    VehicleResponse findByVin(String vin);

    /**
     * Da de baja logicamente un vehiculo activo.
     *
     * @param id identificador interno del vehiculo.
     */
    void deactivate(Long id);
}
