package com.acoes.fleetmanagement.garage.application.mapper;

import com.acoes.fleetmanagement.garage.domain.GarageJpaEntity;
import com.acoes.fleetmanagement.garage.infrastructure.dto.CreateGarageRequest;
import com.acoes.fleetmanagement.garage.infrastructure.dto.GarageResponse;
import com.acoes.fleetmanagement.garage.infrastructure.dto.PatchGarageRequest;
import org.mapstruct.*;

/**
 * Mapea entre entidades de garaje y DTOs de entrada o salida.
 */
@Mapper(componentModel = "spring")
public interface GarageMapper {

    /**
     * Convierte una peticion de creacion en una entidad nueva.
     *
     * @param request datos recibidos para crear el garaje.
     * @return entidad de garaje sin persistir.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "address.street", source = "street")
    @Mapping(target = "address.city", source = "city")
    @Mapping(target = "address.province", source = "province")
    @Mapping(target = "address.postalCode", source = "postalCode")
    @Mapping(target = "address.country", source = "country")
    GarageJpaEntity toEntity(CreateGarageRequest request);

    /**
     * Convierte una entidad de garaje en su DTO de salida.
     *
     * @param entity entidad de garaje.
     * @return DTO de respuesta del garaje.
     */
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "province", source = "address.province")
    @Mapping(target = "postalCode", source = "address.postalCode")
    @Mapping(target = "country", source = "address.country")
    GarageResponse toResponse(GarageJpaEntity entity);

    /**
     * Copia los campos presentes de una peticion parcial sobre una entidad existente.
     *
     * @param request datos opcionales a aplicar.
     * @param entity entidad gestionada que debe actualizarse.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "address.street", source = "street")
    @Mapping(target = "address.city", source = "city")
    @Mapping(target = "address.province", source = "province")
    @Mapping(target = "address.postalCode", source = "postalCode")
    @Mapping(target = "address.country", source = "country")
    void patchEntityFromRequest(PatchGarageRequest request, @MappingTarget GarageJpaEntity entity);
}
