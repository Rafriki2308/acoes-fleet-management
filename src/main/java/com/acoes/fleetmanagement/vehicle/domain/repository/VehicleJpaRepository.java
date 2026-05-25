package com.acoes.fleetmanagement.vehicle.domain.repository;

import com.acoes.fleetmanagement.vehicle.domain.VehicleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para acceder a vehiculos y realizar busquedas por identificadores de negocio.
 */
public interface VehicleJpaRepository extends JpaRepository<VehicleJpaEntity, Long> {

    /**
     * Busca un vehiculo por matricula, incluyendo vehiculos inactivos.
     *
     * @param plateNumber matricula normalizada.
     * @return vehiculo encontrado, si existe.
     */
    Optional<VehicleJpaEntity> findByPlateNumber(String plateNumber);

    /**
     * Busca un vehiculo por VIN, incluyendo vehiculos inactivos.
     *
     * @param vin VIN normalizado.
     * @return vehiculo encontrado, si existe.
     */
    Optional<VehicleJpaEntity> findByVin(String vin);

    /**
     * Comprueba si existe una matricula registrada.
     *
     * @param plateNumber matricula normalizada.
     * @return {@code true} si existe una coincidencia.
     */
    boolean existsByPlateNumber(String plateNumber);

    /**
     * Comprueba si existe un VIN registrado.
     *
     * @param vin VIN normalizado.
     * @return {@code true} si existe una coincidencia.
     */
    boolean existsByVin(String vin);

    /**
     * Busca vehiculos asociados a un garaje.
     *
     * @param garageId identificador interno del garaje.
     * @return lista de vehiculos asociados.
     */
    List<VehicleJpaEntity> findByCurrentGarageId(Long garageId);

    /**
     * Obtiene todos los vehiculos activos.
     *
     * @return lista de vehiculos activos.
     */
    List<VehicleJpaEntity> findByActiveTrue();

    /**
     * Busca un vehiculo activo por matricula.
     *
     * @param plateNumber matricula normalizada.
     * @return vehiculo activo encontrado, si existe.
     */
    Optional<VehicleJpaEntity> findByPlateNumberAndActiveTrue(String plateNumber);

    /**
     * Busca un vehiculo activo por VIN.
     *
     * @param vin VIN normalizado.
     * @return vehiculo activo encontrado, si existe.
     */
    Optional<VehicleJpaEntity> findByVinAndActiveTrue(String vin);
}
