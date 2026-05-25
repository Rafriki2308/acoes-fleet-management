package com.acoes.fleetmanagement.workshop.order.application.impl;

import com.acoes.fleetmanagement.shared.exception.DuplicateResourceException;
import com.acoes.fleetmanagement.shared.exception.ResourceNotFoundException;
import com.acoes.fleetmanagement.workshop.order.application.OrderLineService;
import com.acoes.fleetmanagement.workshop.order.application.mapper.OrderLineMapper;
import com.acoes.fleetmanagement.workshop.order.application.repository.OrderJpaRepository;
import com.acoes.fleetmanagement.workshop.order.application.repository.OrderLineJpaRepository;
import com.acoes.fleetmanagement.workshop.order.domain.OrderJpaEntity;
import com.acoes.fleetmanagement.workshop.order.domain.OrderLineJpaEntity;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.CreateOrderLineRequest;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.PatchOrderLineRequest;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.OrderLineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.ExceptionMessageConstans.*;


/**
 * Implementa las reglas de negocio y persistencia para lineas de orden de taller.
 */
@Service
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {

    private final OrderLineJpaRepository orderLineRepository;

    private final OrderJpaRepository orderRepository;

    private final OrderLineMapper orderLineMapper;

    /**
     * Crea una linea dentro de una orden activa validando que el numero de linea sea unico.
     *
     * @param orderNumber numero funcional de la orden.
     * @param request datos necesarios para crear la linea.
     * @return linea creada.
     * @throws DuplicateResourceException si ya existe una linea con ese numero dentro de la orden.
     * @throws ResourceNotFoundException si la orden no existe o esta inactiva.
     */
    @Override
    @Transactional
    public OrderLineResponse create(
            String orderNumber,
            CreateOrderLineRequest request
    ) {

        OrderJpaEntity workshopOrder =
                findActiveOrderByNumber(orderNumber);

        validateLineNumberUniqueness(
                workshopOrder.getId(),
                request.lineNumber()
        );

        OrderLineJpaEntity entity =
                orderLineMapper.toEntity(request, workshopOrder);

        return orderLineMapper.toResponse(
                orderLineRepository.save(entity)
        );
    }

    /**
     * Obtiene las lineas activas de una orden por su identificador interno.
     *
     * @param workshopOrderId identificador interno de la orden.
     * @return lista de lineas activas ordenadas por numero.
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderLineResponse> findByWorkshopOrder(Long workshopOrderId) {

        return orderLineRepository
                .findByWorkshopOrderIdAndActiveTrueOrderByLineNumberAsc(workshopOrderId)
                .stream()
                .map(orderLineMapper::toResponse)
                .toList();
    }

    /**
     * Obtiene las lineas activas de una orden por su numero funcional.
     *
     * @param orderNumber numero funcional de la orden.
     * @return lista de lineas activas ordenadas por numero.
     * @throws ResourceNotFoundException si no existe una orden activa con ese numero.
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderLineResponse> findByWorkshopOrderNumber(String orderNumber) {

        OrderJpaEntity workshopOrder = orderRepository
                .findByOrderNumberAndActiveTrue(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workshop order not found with number: " + orderNumber
                ));

        return orderLineRepository
                .findByWorkshopOrderIdAndActiveTrueOrderByLineNumberAsc(workshopOrder.getId())
                .stream()
                .map(orderLineMapper::toResponse)
                .toList();
    }

    /**
     * Actualiza parcialmente una linea activa, validando cambios de numero de linea.
     *
     * @param id identificador interno de la linea.
     * @param request datos opcionales a modificar.
     * @return linea actualizada.
     * @throws DuplicateResourceException si el nuevo numero de linea ya existe en la orden.
     * @throws ResourceNotFoundException si no existe una linea activa con ese id.
     */
    @Override
    @Transactional
    public OrderLineResponse patch(Long id, PatchOrderLineRequest request) {

        OrderLineJpaEntity entity = findActiveLineById(id);

        if (request.lineNumber() != null) {
            validateLineNumberUniquenessForPatch(
                    entity.getWorkshopOrder().getId(),
                    request.lineNumber(),
                    entity
            );
        }

        orderLineMapper.patchEntityFromRequest(request, entity);

        return orderLineMapper.toResponse(entity);
    }

    /**
     * Actualiza parcialmente una linea activa localizada por numero de orden y numero de linea.
     *
     * @param orderNumber numero funcional de la orden.
     * @param lineNumber numero de linea dentro de la orden.
     * @param request datos opcionales a modificar.
     * @return linea actualizada.
     * @throws ResourceNotFoundException si no existe una linea activa con esos identificadores.
     */
    @Override
    @Transactional
    public OrderLineResponse patchByOrderNumberAndLineNumber(
            String orderNumber,
            Integer lineNumber,
            PatchOrderLineRequest request
    ) {
        OrderLineJpaEntity entity =
                findActiveLineByOrderNumberAndLineNumber(orderNumber, lineNumber);

        orderLineMapper.patchEntityFromRequest(request, entity);

        return orderLineMapper.toResponse(entity);
    }

    /**
     * Da de baja logicamente una linea activa.
     *
     * @param id identificador interno de la linea.
     * @throws ResourceNotFoundException si no existe una linea activa con ese id.
     */
    @Override
    @Transactional
    public void deactivate(Long id) {

        OrderLineJpaEntity entity = findActiveLineById(id);

        // Hibernate persistira este cambio por dirty checking.
        entity.setActive(false);
    }

    private OrderJpaEntity findActiveOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumberAndActiveTrue(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        WORKSHOP_NOT_FOUND_BY_NUMBER + orderNumber
                ));
    }

    private OrderLineJpaEntity findActiveLineById(Long id) {

        return orderLineRepository.findById(id)
                .filter(OrderLineJpaEntity::isActive)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                WORKSHOP_ORDER_LINE_NOT_FOUND_BY_ID + id
                        )
                );
    }

    /**
     * Valida que el numero de linea sea unico dentro de una orden.
     *
     * @param workshopOrderId identificador interno de la orden.
     * @param lineNumber numero de linea a validar.
     */
    private void validateLineNumberUniqueness(
            Long workshopOrderId,
            Integer lineNumber
    ) {

        if (orderLineRepository
                .existsByWorkshopOrderIdAndLineNumber(
                        workshopOrderId,
                        lineNumber
                )) {

            throw new DuplicateResourceException(WORKSHOP_ORDER_LINE_NUMBER_ALREADY_EXISTS);
        }
    }

    /**
     * Valida que un nuevo numero de linea no colisione con otra linea de la misma orden.
     *
     * @param workshopOrderId identificador interno de la orden.
     * @param newLineNumber nuevo numero de linea a validar.
     * @param currentLine linea que se esta actualizando.
     */
    private void validateLineNumberUniquenessForPatch(
            Long workshopOrderId,
            Integer newLineNumber,
            OrderLineJpaEntity currentLine
    ) {
        boolean isSameLineNumber = newLineNumber.equals(currentLine.getLineNumber());

        if (!isSameLineNumber &&
                orderLineRepository.existsByWorkshopOrderIdAndLineNumber(
                        workshopOrderId,
                        newLineNumber
                )) {
            throw new DuplicateResourceException(
                    WORKSHOP_ORDER_LINE_NUMBER_ALREADY_EXISTS
            );
        }
    }

    private OrderLineJpaEntity findActiveLineByOrderNumberAndLineNumber(
            String orderNumber,
            Integer lineNumber
    ) {
        return orderLineRepository
                .findByWorkshopOrderOrderNumberAndLineNumberAndActiveTrue(orderNumber, lineNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        WORKSHOP_ORDER_LINE_NOT_FOUND_BY_ORDER_NUMBER
                                + orderNumber + LINE_NUMBER + lineNumber
                ));
    }

}
