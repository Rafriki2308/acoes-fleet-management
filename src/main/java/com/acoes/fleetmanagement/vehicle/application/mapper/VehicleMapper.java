package com.acoes.fleetmanagement.vehicle.application.mapper;

import com.acoes.fleetmanagement.vehicle.domain.VehicleJpaEntity;
import com.acoes.fleetmanagement.vehicle.infrastructure.dto.CreateVehicleRequest;
import com.acoes.fleetmanagement.vehicle.infrastructure.dto.PatchVehicleRequest;
import com.acoes.fleetmanagement.vehicle.infrastructure.dto.UpdateVehicleRequest;
import com.acoes.fleetmanagement.vehicle.infrastructure.dto.VehicleResponse;
import org.mapstruct.*;

/**
 * Mapea entre entidades de vehiculo y DTOs de entrada o salida.
 */
@Mapper(componentModel = "spring")
public interface VehicleMapper {

    /**
     * Convierte una peticion de creacion en una entidad nueva.
     *
     * @param request datos recibidos para crear el vehiculo.
     * @return entidad de vehiculo sin persistir.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentGarage", ignore = true)
    @Mapping(target = "active", constant = "true")
    VehicleJpaEntity toEntity(CreateVehicleRequest request);

    /**
     * Copia una peticion de reemplazo sobre una entidad existente.
     *
     * @param request datos nuevos del vehiculo.
     * @param entity entidad gestionada que debe actualizarse.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentGarage", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntityFromRequest(UpdateVehicleRequest request, @MappingTarget VehicleJpaEntity entity);

    /**
     * Copia los campos presentes de una peticion parcial sobre una entidad existente.
     *
     * @param request datos opcionales a aplicar.
     * @param entity entidad gestionada que debe actualizarse.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "plateNumber", ignore = true)
    @Mapping(target = "vin", ignore = true)
    @Mapping(target = "currentGarage", ignore = true)
    void patchEntityFromRequest(PatchVehicleRequest request, @MappingTarget VehicleJpaEntity entity);

    /**
     * Convierte una entidad de vehiculo en su DTO de salida.
     *
     * @param entity entidad de vehiculo.
     * @return DTO de respuesta del vehiculo.
     */
    @Mapping(target = "currentGarageId", source = "currentGarage.id")
    @Mapping(target = "currentGarageName", source = "currentGarage.name")
    VehicleResponse toResponse(VehicleJpaEntity entity);
}
