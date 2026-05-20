package com.acoes.fleetmanagement.workshop.execution.application.impl;

import com.acoes.fleetmanagement.shared.businessnumber.domain.application.BusinessNumberGenerator;
import com.acoes.fleetmanagement.shared.businessnumber.domain.model.BusinessSequenceKey;
import com.acoes.fleetmanagement.shared.exception.DuplicateResourceException;
import com.acoes.fleetmanagement.shared.exception.ResourceNotFoundException;
import com.acoes.fleetmanagement.shared.validation.VehicleNormalizationUtils;
import com.acoes.fleetmanagement.workshop.execution.application.WorkshopExecutionService;
import com.acoes.fleetmanagement.workshop.execution.application.mapper.WorkshopExecutionMapper;
import com.acoes.fleetmanagement.workshop.execution.application.repository.WorkshopExecutionJpaRepository;
import com.acoes.fleetmanagement.workshop.execution.domain.WorkshopExecutionJpaEntity;
import com.acoes.fleetmanagement.workshop.execution.domain.model.WorkshopExecutionStatus;
import com.acoes.fleetmanagement.workshop.execution.infractrusture.dto.CreateExecutionRequest;
import com.acoes.fleetmanagement.workshop.execution.infractrusture.dto.ExecutionResponse;
import com.acoes.fleetmanagement.workshop.execution.infractrusture.dto.UpdateExecutionStatusRequest;
import com.acoes.fleetmanagement.workshop.order.application.repository.WorkshopOrderJpaRepository;
import com.acoes.fleetmanagement.workshop.order.domain.WorkshopOrderJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.ExceptionMessageConstans.*;

@Service
@RequiredArgsConstructor
public class WorkshopExecutionServiceImpl implements WorkshopExecutionService {

    @Autowired
    private final WorkshopExecutionJpaRepository executionRepository;

    @Autowired
    private final WorkshopOrderJpaRepository orderRepository;

    @Autowired
    private final WorkshopExecutionMapper executionMapper;

    @Autowired
    private final BusinessNumberGenerator businessNumberGenerator;

    @Override
    @Transactional
    public ExecutionResponse create(
            String orderNumber,
            CreateExecutionRequest request
    ) {
        WorkshopOrderJpaEntity order = findActiveOrderByNumber(orderNumber);

        validateExecutionUniqueness(order.getId(), orderNumber);

        WorkshopExecutionJpaEntity execution = buildExecution(request, order);

        return executionMapper.toResponse(
                executionRepository.save(execution)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExecutionResponse> findAll() {
        return executionRepository.findByActiveTrue()
                .stream()
                .map(executionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ExecutionResponse findByExecutionNumber(String executionNumber) {
        WorkshopExecutionJpaEntity execution =
                findActiveExecutionByNumber(executionNumber);

        return executionMapper.toResponse(execution);
    }

    @Override
    @Transactional(readOnly = true)
    public ExecutionResponse findByOrderNumber(String orderNumber) {
        WorkshopExecutionJpaEntity execution =
                findActiveExecutionByOrderNumber(orderNumber);

        return executionMapper.toResponse(execution);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExecutionResponse> findByVehiclePlateNumber(String plateNumber) {
        String normalizedPlate =
                VehicleNormalizationUtils.normalizePlateNumber(plateNumber);

        return executionRepository
                .findByWorkshopOrderVehiclePlateNumberAndActiveTrueOrderByStartDateDesc(
                        normalizedPlate
                )
                .stream()
                .map(executionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExecutionResponse> findByVehicleVin(String vin) {
        String normalizedVin = VehicleNormalizationUtils.normalizeVin(vin);

        return executionRepository
                .findByWorkshopOrderVehicleVinAndActiveTrueOrderByStartDateDesc(
                        normalizedVin
                )
                .stream()
                .map(executionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ExecutionResponse updateStatus(
            String executionNumber,
            UpdateExecutionStatusRequest request
    ) {
        WorkshopExecutionJpaEntity execution =
                findActiveExecutionByNumber(executionNumber);

        updateExecutionStatus(execution, request.status());

        return executionMapper.toResponse(execution);
    }

    @Override
    @Transactional
    public void deactivate(String executionNumber) {
        WorkshopExecutionJpaEntity execution =
                findActiveExecutionByNumber(executionNumber);

        deactivateExecution(execution);
    }

    private WorkshopExecutionJpaEntity buildExecution(
            CreateExecutionRequest request,
            WorkshopOrderJpaEntity order
    ) {
        WorkshopExecutionJpaEntity execution =
                executionMapper.toEntity(request, order);

        execution.setExecutionNumber(generateExecutionNumber());
        execution.setWorkshopOrderNumber(order.getOrderNumber());
        execution.setStartDate(resolveStartDate(execution.getStartDate()));
        execution.setStatus(resolveInitialStatus(execution.getStatus()));

        return execution;
    }

    private String generateExecutionNumber() {
        return businessNumberGenerator.generate(
                "EX",
                BusinessSequenceKey.WORKSHOP_EXECUTION
        );
    }

    private LocalDate resolveStartDate(LocalDate startDate) {
        return startDate != null ? startDate : LocalDate.now();
    }

    private WorkshopExecutionStatus resolveInitialStatus(
            WorkshopExecutionStatus status
    ) {
        return status != null ? status : WorkshopExecutionStatus.OPEN;
    }

    private void updateExecutionStatus(
            WorkshopExecutionJpaEntity execution,
            WorkshopExecutionStatus status
    ) {
        execution.setStatus(status);

        if (status == WorkshopExecutionStatus.CLOSED
                && execution.getEndDate() == null) {
            execution.setEndDate(LocalDate.now());
        }
    }

    private void deactivateExecution(WorkshopExecutionJpaEntity execution) {
        // Managed entity → Hibernate will persist change automatically
        execution.setActive(false);
    }

    private WorkshopExecutionJpaEntity findActiveExecutionByNumber(
            String executionNumber
    ) {
        return executionRepository
                .findByExecutionNumberAndActiveTrue(executionNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                EXECUTION_NOT_FOUND + executionNumber
                        )
                );
    }

    private WorkshopExecutionJpaEntity findActiveExecutionByOrderNumber(
            String orderNumber
    ) {
        return executionRepository
                .findByWorkshopOrderOrderNumberAndActiveTrue(orderNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                EXECUTION_NOT_FOUND + orderNumber
                        )
                );
    }

    private WorkshopOrderJpaEntity findActiveOrderByNumber(String orderNumber) {
        return orderRepository
                .findByOrderNumberAndActiveTrue(orderNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ORDER_NOT_FOUND + orderNumber
                        )
                );
    }

    private void validateExecutionUniqueness(
            Long workshopOrderId,
            String orderNumber
    ) {
        if (executionRepository.existsByWorkshopOrderId(workshopOrderId)) {
            throw new DuplicateResourceException(
                    EXECUTION_ALREADY_EXISTS_FOR_ORDER + orderNumber
            );
        }
    }
}