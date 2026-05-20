package com.acoes.fleetmanagement.workshop.execution.application.repository;

import com.acoes.fleetmanagement.workshop.execution.domain.WorkshopExecutionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkshopExecutionJpaRepository
        extends JpaRepository<WorkshopExecutionJpaEntity, Long> {

    /**
     * Busca una ejecución activa por id.
     */
    Optional<WorkshopExecutionJpaEntity> findByIdAndActiveTrue(Long id);

    /**
     * Busca una ejecución activa por número de ejecución.
     */
    Optional<WorkshopExecutionJpaEntity>
    findByExecutionNumberAndActiveTrue(String executionNumber);

    /**
     * Busca una ejecución activa por número de orden.
     */
    Optional<WorkshopExecutionJpaEntity>
    findByWorkshopOrderOrderNumberAndActiveTrue(String orderNumber);

    /**
     * Comprueba si ya existe una ejecución activa
     * para una orden concreta.
     */
    boolean existsByWorkshopOrderId(Long workshopOrderId);

    /**
     * Obtiene todas las ejecuciones activas.
     */
    List<WorkshopExecutionJpaEntity> findByActiveTrue();

    /**
     * Busca ejecuciones activas por matrícula snapshot.
     */
    List<WorkshopExecutionJpaEntity>
    findByWorkshopOrderVehiclePlateNumberAndActiveTrueOrderByStartDateDesc(
            String plateNumber
    );

    /**
     * Busca ejecuciones activas por VIN del vehículo.
     */
    List<WorkshopExecutionJpaEntity>
    findByWorkshopOrderVehicleVinAndActiveTrueOrderByStartDateDesc(
            String vin
    );
}
