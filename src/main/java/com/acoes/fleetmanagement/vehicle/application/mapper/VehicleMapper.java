package com.acoes.fleetmanagement.vehicle.application.mapper;

import com.acoes.fleetmanagement.vehicle.domain.VehicleJpaEntity;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.CreateVehicleRequest;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.UpdateVehicleRequest;
import com.acoes.fleetmanagement.vehicle.infraestructure.dto.VehicleResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    // =========================
    // CREATE (DTO → ENTITY)
    // =========================

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentGarage", ignore = true) // se resuelve manualmente en el service
    @Mapping(target = "active", constant = "true")
    VehicleJpaEntity toEntity(CreateVehicleRequest request);

    // =========================
    // UPDATE (DTO → EXISTING ENTITY)
    // =========================

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentGarage", ignore = true) // se resuelve manualmente en el service
    @Mapping(target = "active", ignore = true)
    void updateEntityFromRequest(UpdateVehicleRequest request, @MappingTarget VehicleJpaEntity entity);

    // =========================
    // RESPONSE (ENTITY → DTO)
    // =========================

    @Mapping(target = "currentGarageId", source = "currentGarage.id")
    @Mapping(target = "currentGarageName", source = "currentGarage.name")
    VehicleResponse toResponse(VehicleJpaEntity entity);
}
