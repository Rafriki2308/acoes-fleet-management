package com.acoes.fleetmanagement.workshop.order.application.mapper;

import com.acoes.fleetmanagement.workshop.order.domain.OrderJpaEntity;
import com.acoes.fleetmanagement.workshop.order.domain.OrderLineJpaEntity;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.CreateOrderLineRequest;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.PatchOrderLineRequest;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.OrderLineResponse;
import org.mapstruct.*;

/**
 * Mapea entre entidades de linea de orden y DTOs de entrada o salida.
 */
@Mapper(componentModel = "spring")
public interface OrderLineMapper {

    /**
     * Convierte una peticion de creacion y su orden en una linea nueva.
     *
     * @param request datos recibidos para crear la linea.
     * @param workshopOrder orden activa propietaria de la linea.
     * @return entidad de linea sin persistir.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "workshopOrder", source = "workshopOrder")
    OrderLineJpaEntity toEntity(
            CreateOrderLineRequest request,
            OrderJpaEntity workshopOrder
    );

    /**
     * Copia los campos presentes de una peticion parcial sobre una linea existente.
     *
     * @param request datos opcionales a aplicar.
     * @param entity entidad gestionada que debe actualizarse.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "workshopOrder", ignore = true)
    void patchEntityFromRequest(
            PatchOrderLineRequest request,
            @MappingTarget OrderLineJpaEntity entity
    );

    @Mapping(
            target = "workshopOrderNumber",
            source = "workshopOrder.orderNumber"
    )
    /**
     * Convierte una entidad de linea en su DTO de salida.
     *
     * @param entity entidad de linea.
     * @return DTO de respuesta de la linea.
     */
    @Mapping(target = "id", ignore = true)
    OrderLineResponse toResponse(OrderLineJpaEntity entity);

}
