package com.acoes.fleetmanagement.workshop.order.application.mapper;

import com.acoes.fleetmanagement.workshop.order.domain.WorkshopOrderJpaEntity;
import com.acoes.fleetmanagement.workshop.order.domain.WorkshopOrderLineJpaEntity;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.CreateWorkshopOrderLineRequest;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.PatchWorkshopOrderLineRequest;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.WorkshopOrderLineResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WorkshopOrderLineMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "workshopOrder", source = "workshopOrder")
    WorkshopOrderLineJpaEntity toEntity(
            CreateWorkshopOrderLineRequest request,
            WorkshopOrderJpaEntity workshopOrder
    );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "workshopOrder", ignore = true)
    void patchEntityFromRequest(
            PatchWorkshopOrderLineRequest request,
            @MappingTarget WorkshopOrderLineJpaEntity entity
    );

    @Mapping(
            target = "workshopOrderNumber",
            source = "workshopOrder.orderNumber"
    )
    WorkshopOrderLineResponse toResponse(WorkshopOrderLineJpaEntity entity);

}
