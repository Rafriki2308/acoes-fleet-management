package com.acoes.fleetmanagement.workshop.execution.application.impl;

import com.acoes.fleetmanagement.shared.businessnumber.domain.application.BusinessNumberGenerator;
import com.acoes.fleetmanagement.shared.businessnumber.domain.model.BusinessSequenceKey;
import com.acoes.fleetmanagement.shared.exception.DuplicateResourceException;
import com.acoes.fleetmanagement.shared.exception.ResourceNotFoundException;
import com.acoes.fleetmanagement.shared.validation.VehicleNormalizationUtils;
import com.acoes.fleetmanagement.workshop.execution.application.ExecutionService;
import com.acoes.fleetmanagement.workshop.execution.application.mapper.ExecutionMapper;
import com.acoes.fleetmanagement.workshop.execution.application.repository.ExecutionJpaRepository;
import com.acoes.fleetmanagement.workshop.execution.domain.ExecutionJpaEntity;
import com.acoes.fleetmanagement.workshop.execution.domain.model.ExecutionStatus;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.CreateExecutionRequest;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.ExecutionResponse;
import com.acoes.fleetmanagement.workshop.execution.infrastructure.dto.UpdateExecutionStatusRequest;
import com.acoes.fleetmanagement.workshop.order.application.repository.OrderJpaRepository;
import com.acoes.fleetmanagement.workshop.order.domain.OrderJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.ExceptionMessageConstans.*;

/**
 * Implementa las reglas de negocio y persistencia para ejecuciones de taller.
 */
@Service
@RequiredArgsConstructor
public class ExecutionServiceImpl implements ExecutionService {

    private final ExecutionJpaRepository executionRepository;

    private final OrderJpaRepository orderRepository;

    private final ExecutionMapper executionMapper;

    private final BusinessNumberGenerator businessNumberGenerator;

    /**
     * Crea la ejecucion activa de una orden, generando su numero funcional y validando unicidad por orden.
     *
     * @param orderNumber numero funcional de la orden.
     * @param request datos necesarios para iniciar la ejecucion.
     * @return ejecucion creada.
     * @throws DuplicateResourceException si la orden ya tiene una ejecucion asociada.
     * @throws ResourceNotFoundException si la orden no existe o esta inactiva.
     */
    @Override
    @Transactional
    public ExecutionResponse create(
            String orderNumber,
            CreateExecutionRequest request
    ) {
        OrderJpaEntity order = findActiveOrderByNumber(orderNumber);

        validateExecutionUniqueness(order.getId(), orderNumber);

        ExecutionJpaEntity execution = buildExecution(request, order);

        return executionMapper.toResponse(
                executionRepository.save(execution)
        );
    }

    /**
     * Obtiene todas las ejecuciones activas.
     *
     * @return lista de ejecuciones activas.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExecutionResponse> findAll() {
        return executionRepository.findByActiveTrue()
                .stream()
                .map(executionMapper::toResponse)
                .toList();
    }

    /**
     * Busca una ejecucion activa por su numero funcional.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @return ejecucion encontrada.
     * @throws ResourceNotFoundException si no existe una ejecucion activa con ese numero.
     */
    @Override
    @Transactional(readOnly = true)
    public ExecutionResponse findByExecutionNumber(String executionNumber) {
        ExecutionJpaEntity execution =
                findActiveExecutionByNumber(executionNumber);

        return executionMapper.toResponse(execution);
    }

    /**
     * Busca una ejecucion activa por el numero funcional de su orden.
     *
     * @param orderNumber numero funcional de la orden.
     * @return ejecucion encontrada.
     * @throws ResourceNotFoundException si no existe una ejecucion activa para esa orden.
     */
    @Override
    @Transactional(readOnly = true)
    public ExecutionResponse findByOrderNumber(String orderNumber) {
        ExecutionJpaEntity execution =
                findActiveExecutionByOrderNumber(orderNumber);

        return executionMapper.toResponse(execution);
    }

    /**
     * Obtiene ejecuciones activas por matricula de vehiculo normalizada.
     *
     * @param plateNumber matricula recibida.
     * @return lista de ejecuciones asociadas.
     */
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

    /**
     * Obtiene ejecuciones activas por VIN de vehiculo normalizado.
     *
     * @param vin VIN recibido.
     * @return lista de ejecuciones asociadas.
     */
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

    /**
     * Cambia el estado de una ejecucion activa y completa la fecha de fin cuando pasa a cerrada.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @param request nuevo estado solicitado.
     * @return ejecucion actualizada.
     * @throws ResourceNotFoundException si no existe una ejecucion activa con ese numero.
     */
    @Override
    @Transactional
    public ExecutionResponse updateStatus(
            String executionNumber,
            UpdateExecutionStatusRequest request
    ) {
        ExecutionJpaEntity execution =
                findActiveExecutionByNumber(executionNumber);

        updateExecutionStatus(execution, request.status());

        return executionMapper.toResponse(execution);
    }

    /**
     * Da de baja logicamente una ejecucion activa.
     *
     * @param executionNumber numero funcional de la ejecucion.
     * @throws ResourceNotFoundException si no existe una ejecucion activa con ese numero.
     */
    @Override
    @Transactional
    public void deactivate(String executionNumber) {
        ExecutionJpaEntity execution =
                findActiveExecutionByNumber(executionNumber);

        deactivateExecution(execution);
    }

    /**
     * Construye una ejecucion nueva completando numero, orden snapshot, fecha y estado inicial.
     *
     * @param request datos recibidos para crear la ejecucion.
     * @param order orden activa asociada.
     * @return ejecucion lista para persistir.
     */
    private ExecutionJpaEntity buildExecution(
            CreateExecutionRequest request,
            OrderJpaEntity order
    ) {
        ExecutionJpaEntity execution =
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

    private ExecutionStatus resolveInitialStatus(
            ExecutionStatus status
    ) {
        return status != null ? status : ExecutionStatus.OPEN;
    }

    /**
     * Aplica el nuevo estado y cierra la fecha final si la ejecucion pasa a cerrada.
     *
     * @param execution ejecucion que se esta modificando.
     * @param status nuevo estado solicitado.
     */
    private void updateExecutionStatus(
            ExecutionJpaEntity execution,
            ExecutionStatus status
    ) {
        execution.setStatus(status);

        if (status == ExecutionStatus.CLOSED
                && execution.getEndDate() == null) {
            execution.setEndDate(LocalDate.now());
        }
    }

    private void deactivateExecution(ExecutionJpaEntity execution) {
        // Hibernate persistira este cambio por dirty checking.
        execution.setActive(false);
    }

    private ExecutionJpaEntity findActiveExecutionByNumber(
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

    private ExecutionJpaEntity findActiveExecutionByOrderNumber(
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

    private OrderJpaEntity findActiveOrderByNumber(String orderNumber) {
        return orderRepository
                .findByOrderNumberAndActiveTrue(orderNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ORDER_NOT_FOUND + orderNumber
                        )
                );
    }

    /**
     * Valida que una orden no tenga ya una ejecucion asociada.
     *
     * @param workshopOrderId identificador interno de la orden.
     * @param orderNumber numero funcional de la orden usado en el mensaje de error.
     */
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
