package com.acoes.fleetmanagement.workshop.order.application.impl;

import com.acoes.fleetmanagement.shared.exception.DuplicateResourceException;
import com.acoes.fleetmanagement.shared.exception.ResourceNotFoundException;
import com.acoes.fleetmanagement.workshop.order.application.WorkshopOrderLineService;
import com.acoes.fleetmanagement.workshop.order.application.mapper.WorkshopOrderLineMapper;
import com.acoes.fleetmanagement.workshop.order.application.repository.WorkshopOrderJpaRepository;
import com.acoes.fleetmanagement.workshop.order.application.repository.WorkshopOrderLineJpaRepository;
import com.acoes.fleetmanagement.workshop.order.domain.WorkshopOrderJpaEntity;
import com.acoes.fleetmanagement.workshop.order.domain.WorkshopOrderLineJpaEntity;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.CreateWorkshopOrderLineRequest;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.PatchWorkshopOrderLineRequest;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.WorkshopOrderLineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.ExceptionMessageConstans.*;


@Service
@RequiredArgsConstructor
public class WorkshopOrderLineServiceImpl implements WorkshopOrderLineService {

    @Autowired
    private WorkshopOrderLineJpaRepository workshopOrderLineRepository;

    @Autowired
    private WorkshopOrderJpaRepository workshopOrderRepository;

    @Autowired
    private WorkshopOrderLineMapper workshopOrderLineMapper;

    @Override
    @Transactional
    public WorkshopOrderLineResponse create(
            String orderNumber,
            CreateWorkshopOrderLineRequest request
    ) {

        WorkshopOrderJpaEntity workshopOrder =
                findActiveWorkshopOrderByNumber(orderNumber);

        validateLineNumberUniqueness(
                workshopOrder.getId(),
                request.lineNumber()
        );

        WorkshopOrderLineJpaEntity entity =
                workshopOrderLineMapper.toEntity(request, workshopOrder);

        return workshopOrderLineMapper.toResponse(
                workshopOrderLineRepository.save(entity)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkshopOrderLineResponse> findByWorkshopOrder(Long workshopOrderId) {

        return workshopOrderLineRepository
                .findByWorkshopOrderIdAndActiveTrueOrderByLineNumberAsc(workshopOrderId)
                .stream()
                .map(workshopOrderLineMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkshopOrderLineResponse> findByWorkshopOrderNumber(String orderNumber) {

        WorkshopOrderJpaEntity workshopOrder = workshopOrderRepository
                .findByOrderNumberAndActiveTrue(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workshop order not found with number: " + orderNumber
                ));

        return workshopOrderLineRepository
                .findByWorkshopOrderIdAndActiveTrueOrderByLineNumberAsc(workshopOrder.getId())
                .stream()
                .map(workshopOrderLineMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public WorkshopOrderLineResponse patch(Long id, PatchWorkshopOrderLineRequest request) {

        WorkshopOrderLineJpaEntity entity = findActiveLineById(id);

        if (request.lineNumber() != null) {
            validateLineNumberUniquenessForPatch(
                    entity.getWorkshopOrder().getId(),
                    request.lineNumber(),
                    entity
            );
        }

        workshopOrderLineMapper.patchEntityFromRequest(request, entity);

        return workshopOrderLineMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public WorkshopOrderLineResponse patchByOrderNumberAndLineNumber(
            String orderNumber,
            Integer lineNumber,
            PatchWorkshopOrderLineRequest request
    ) {
        WorkshopOrderLineJpaEntity entity =
                findActiveLineByOrderNumberAndLineNumber(orderNumber, lineNumber);

        workshopOrderLineMapper.patchEntityFromRequest(request, entity);

        return workshopOrderLineMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {

        WorkshopOrderLineJpaEntity entity = findActiveLineById(id);

        // Managed entity → Hibernate dirty checking
        entity.setActive(false);
    }

    private WorkshopOrderJpaEntity findActiveWorkshopOrderByNumber(String orderNumber) {
        return workshopOrderRepository.findByOrderNumberAndActiveTrue(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        WORKSHOP_NOT_FOUND_BY_NUMBER + orderNumber
                ));
    }

    private WorkshopOrderLineJpaEntity findActiveLineById(Long id) {

        return workshopOrderLineRepository.findById(id)
                .filter(WorkshopOrderLineJpaEntity::isActive)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                WORKSHOP_ORDER_LINE_NOT_FOUND_BY_ID + id
                        )
                );
    }

    private void validateLineNumberUniqueness(
            Long workshopOrderId,
            Integer lineNumber
    ) {

        if (workshopOrderLineRepository
                .existsByWorkshopOrderIdAndLineNumber(
                        workshopOrderId,
                        lineNumber
                )) {

            throw new DuplicateResourceException(WORKSHOP_ORDER_LINE_NUMBER_ALREADY_EXISTS);
        }
    }

    private void validateLineNumberUniquenessForPatch(
            Long workshopOrderId,
            Integer newLineNumber,
            WorkshopOrderLineJpaEntity currentLine
    ) {
        boolean isSameLineNumber = newLineNumber.equals(currentLine.getLineNumber());

        if (!isSameLineNumber &&
                workshopOrderLineRepository.existsByWorkshopOrderIdAndLineNumber(
                        workshopOrderId,
                        newLineNumber
                )) {
            throw new DuplicateResourceException(
                    WORKSHOP_ORDER_LINE_NUMBER_ALREADY_EXISTS
            );
        }
    }

    private WorkshopOrderLineJpaEntity findActiveLineByOrderNumberAndLineNumber(
            String orderNumber,
            Integer lineNumber
    ) {
        return workshopOrderLineRepository
                .findByWorkshopOrderOrderNumberAndLineNumberAndActiveTrue(orderNumber, lineNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        WORKSHOP_ORDER_LINE_NOT_FOUND_BY_ORDER_NUMBER
                                + orderNumber + LINE_NUMBER + lineNumber
                ));
    }

}
