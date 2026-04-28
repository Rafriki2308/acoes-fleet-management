package com.acoes.fleetmanagement.vehicle.domain.repository;

import com.acoes.fleetmanagement.vehicle.domain.VehicleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleJpaRepository extends JpaRepository<VehicleJpaEntity, Long> {

    Optional<VehicleJpaEntity> findByPlateNumber(String plateNumber);

    Optional<VehicleJpaEntity> findByVin(String vin);

    boolean existsByPlateNumber(String plateNumber);

    boolean existsByVin(String vin);

    List<VehicleJpaEntity> findByCurrentGarageId(Long garageId);

    List<VehicleJpaEntity> findByActiveTrue();
}
