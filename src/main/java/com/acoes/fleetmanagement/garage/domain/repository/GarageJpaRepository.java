package com.acoes.fleetmanagement.garage.domain.repository;

import com.acoes.fleetmanagement.garage.domain.GarageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GarageJpaRepository extends JpaRepository<GarageJpaEntity, Long> {

    Optional<GarageJpaEntity> findByName(String name);

    boolean existsByName(String name);

    List<GarageJpaEntity> findByActiveTrue();
}
