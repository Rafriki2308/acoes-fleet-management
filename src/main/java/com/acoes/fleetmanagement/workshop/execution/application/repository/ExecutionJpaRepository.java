package com.acoes.fleetmanagement.workshop.execution.application.repository;

import com.acoes.fleetmanagement.workshop.execution.domain.ExecutionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para acceder a ejecuciones de taller y sus busquedas principales.
 */
@Repository
public interface ExecutionJpaRepository
        extends JpaRepository<ExecutionJpaEntity, Long> {

    /**
     * Busca una ejecucion activa por su identificador interno.
     *
     * @param id identificador interno de la ejecucion.
     * @return ejecucion encontrada, si existe.
     */
    Optional<ExecutionJpaEntity> findByIdAndActiveTrue(Long id);

    /**
     * Busca una ejecucion activa por numero funcional.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @return ejecucion encontrada, si existe.
     */
    Optional<ExecutionJpaEntity>
    findByExecutionNumberAndActiveTrue(String executionNumber);

    /**
     * Busca una ejecucion activa por el numero funcional de su orden.
     *
     * @param orderNumber numero funcional de la orden.
     * @return ejecucion encontrada, si existe.
     */
    Optional<ExecutionJpaEntity>
    findByWorkshopOrderOrderNumberAndActiveTrue(String orderNumber);

    /**
     * Comprueba si ya existe una ejecucion para una orden.
     *
     * @param workshopOrderId identificador interno de la orden.
     * @return {@code true} si existe una ejecucion asociada.
     */
    boolean existsByWorkshopOrderId(Long workshopOrderId);

    /**
     * Obtiene todas las ejecuciones activas.
     *
     * @return lista de ejecuciones activas.
     */
    List<ExecutionJpaEntity> findByActiveTrue();

    /**
     * Busca ejecuciones activas por matricula de vehiculo, ordenadas por inicio descendente.
     *
     * @param plateNumber matricula normalizada del vehiculo.
     * @return lista de ejecuciones asociadas.
     */
    List<ExecutionJpaEntity>
    findByWorkshopOrderVehiclePlateNumberAndActiveTrueOrderByStartDateDesc(
            String plateNumber
    );

    /**
     * Busca ejecuciones activas por VIN de vehiculo, ordenadas por inicio descendente.
     *
     * @param vin VIN normalizado del vehiculo.
     * @return lista de ejecuciones asociadas.
     */
    List<ExecutionJpaEntity>
    findByWorkshopOrderVehicleVinAndActiveTrueOrderByStartDateDesc(
            String vin
    );
}
