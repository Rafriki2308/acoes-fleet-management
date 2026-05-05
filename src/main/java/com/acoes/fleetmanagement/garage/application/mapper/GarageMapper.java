package com.acoes.fleetmanagement.garage.application.mapper;

import com.acoes.fleetmanagement.garage.domain.GarageJpaEntity;
import com.acoes.fleetmanagement.garage.infrastructure.dto.CreateGarageRequest;
import com.acoes.fleetmanagement.garage.infrastructure.dto.GarageResponse;
import com.acoes.fleetmanagement.garage.infrastructure.dto.PatchGarageRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface GarageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "address.street", source = "street")
    @Mapping(target = "address.city", source = "city")
    @Mapping(target = "address.province", source = "province")
    @Mapping(target = "address.postalCode", source = "postalCode")
    @Mapping(target = "address.country", source = "country")
    GarageJpaEntity toEntity(CreateGarageRequest request);

    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "province", source = "address.province")
    @Mapping(target = "postalCode", source = "address.postalCode")
    @Mapping(target = "country", source = "address.country")
    GarageResponse toResponse(GarageJpaEntity entity);

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
