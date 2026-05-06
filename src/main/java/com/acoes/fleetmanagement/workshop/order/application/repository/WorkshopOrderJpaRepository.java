package com.acoes.fleetmanagement.workshop.order.application.repository;

import com.acoes.fleetmanagement.workshop.order.domain.WorkshopOrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkshopOrderJpaRepository extends JpaRepository<WorkshopOrderJpaEntity, Long> {

    Optional<WorkshopOrderJpaEntity> findByOrderNumberAndActiveTrue(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);

    List<WorkshopOrderJpaEntity> findByActiveTrue();

    List<WorkshopOrderJpaEntity> findByVehicleIdAndActiveTrue(Long vehicleId);

    List<WorkshopOrderJpaEntity> findByVehiclePlateNumberAndActiveTrueOrderByOpeningDateDesc(String vehiclePlateNumber);

    List<WorkshopOrderJpaEntity> findByVehicleVinAndActiveTrueOrderByOpeningDateDesc(String vin);
}
