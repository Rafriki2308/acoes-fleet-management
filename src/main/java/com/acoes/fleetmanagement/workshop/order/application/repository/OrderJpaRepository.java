package com.acoes.fleetmanagement.workshop.order.application.repository;

import com.acoes.fleetmanagement.workshop.order.domain.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para acceder a ordenes de taller y sus busquedas principales.
 */
@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {

    /**
     * Busca una orden activa por su numero funcional.
     *
     * @param orderNumber numero funcional de la orden.
     * @return orden encontrada, si existe.
     */
    Optional<OrderJpaEntity> findByOrderNumberAndActiveTrue(String orderNumber);

    /**
     * Comprueba si existe una orden con el numero funcional indicado.
     *
     * @param orderNumber numero funcional de la orden.
     * @return {@code true} si existe una coincidencia.
     */
    boolean existsByOrderNumber(String orderNumber);

    /**
     * Obtiene todas las ordenes activas.
     *
     * @return lista de ordenes activas.
     */
    List<OrderJpaEntity> findByActiveTrue();

    /**
     * Busca ordenes activas de un vehiculo.
     *
     * @param vehicleId identificador interno del vehiculo.
     * @return lista de ordenes asociadas.
     */
    List<OrderJpaEntity> findByVehicleIdAndActiveTrue(Long vehicleId);

    /**
     * Busca ordenes activas por matricula de vehiculo, ordenadas por fecha de apertura descendente.
     *
     * @param vehiclePlateNumber matricula normalizada del vehiculo.
     * @return lista de ordenes asociadas.
     */
    List<OrderJpaEntity> findByVehiclePlateNumberAndActiveTrueOrderByOpeningDateDesc(String vehiclePlateNumber);

    /**
     * Busca ordenes activas por VIN de vehiculo, ordenadas por fecha de apertura descendente.
     *
     * @param vin VIN normalizado del vehiculo.
     * @return lista de ordenes asociadas.
     */
    List<OrderJpaEntity> findByVehicleVinAndActiveTrueOrderByOpeningDateDesc(String vin);
}
