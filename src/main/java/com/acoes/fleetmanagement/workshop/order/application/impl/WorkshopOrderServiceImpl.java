package com.acoes.fleetmanagement.workshop.order.application.impl;

import com.acoes.fleetmanagement.shared.exception.ResourceNotFoundException;
import com.acoes.fleetmanagement.shared.validation.VehicleNormalizationUtils;
import com.acoes.fleetmanagement.vehicle.domain.VehicleJpaEntity;
import com.acoes.fleetmanagement.vehicle.domain.repository.VehicleJpaRepository;
import com.acoes.fleetmanagement.workshop.order.application.WorkshopOrderService;
import com.acoes.fleetmanagement.workshop.order.application.mapper.WorkshopOrderMapper;
import com.acoes.fleetmanagement.workshop.order.application.repository.WorkshopOrderJpaRepository;
import com.acoes.fleetmanagement.workshop.order.domain.WorkshopOrderJpaEntity;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.CreateWorkshopOrderRequest;
import com.acoes.fleetmanagement.workshop.order.infraestructure.dto.WorkshopOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.ExceptionMessageConstans.*;


@Service
@RequiredArgsConstructor
public class WorkshopOrderServiceImpl implements WorkshopOrderService {

    @Autowired
    private WorkshopOrderJpaRepository workshopOrderRepository;

    @Autowired
    private VehicleJpaRepository vehicleRepository;

    @Autowired
    private WorkshopOrderMapper workshopOrderMapper;

    @Override
    @Transactional
    public WorkshopOrderResponse create(CreateWorkshopOrderRequest request) {
        VehicleJpaEntity vehicle = findActiveVehicleById(request.vehicleId());

        WorkshopOrderJpaEntity entity = workshopOrderMapper.toEntity(request, vehicle);
//        Ojo con generateOrderNumber(): para desarrollo vale, pero en producción lo mejor será una secuencia/tabla
//        específica para evitar duplicados por concurrencia.
        entity.setOrderNumber(generateOrderNumber());

        return workshopOrderMapper.toResponse(workshopOrderRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkshopOrderResponse> findAll() {
        return workshopOrderRepository.findByActiveTrue()
                .stream()
                .map(workshopOrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WorkshopOrderResponse findById(Long id) {
        return workshopOrderMapper.toResponse(findActiveOrderById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public WorkshopOrderResponse findByOrderNumber(String orderNumber) {
        WorkshopOrderJpaEntity entity = workshopOrderRepository.findByOrderNumberAndActiveTrue(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(WORKSHOP_NOT_FOUND_BY_NUMBER + orderNumber));

        return workshopOrderMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkshopOrderResponse> findByVehicleId(Long vehicleId) {
        return workshopOrderRepository.findByVehicleIdAndActiveTrue(vehicleId)
                .stream()
                .map(workshopOrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkshopOrderResponse> findByVehiclePlateNumber(String plateNumber) {

        String normalizedPlateNumber = VehicleNormalizationUtils.normalizePlateNumber(plateNumber);

        return workshopOrderRepository.findByVehiclePlateNumberAndActiveTrueOrderByOpeningDateDesc(normalizedPlateNumber)
                .stream()
                .map(workshopOrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkshopOrderResponse> findByVehicleVin(String vin) {

        String normalizedVin = VehicleNormalizationUtils.normalizeVin(vin);

        return workshopOrderRepository
                .findByVehicleVinAndActiveTrueOrderByOpeningDateDesc(normalizedVin)
                .stream()
                .map(workshopOrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        WorkshopOrderJpaEntity entity = findActiveOrderById(id);

        // Managed entity → Hibernate will persist change automatically
        entity.setActive(false);
    }

    private VehicleJpaEntity findActiveVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .filter(VehicleJpaEntity::isActive)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_BY_ID + id));
    }

    private WorkshopOrderJpaEntity findActiveOrderById(Long id) {
        return workshopOrderRepository.findById(id)
                .filter(WorkshopOrderJpaEntity::isActive)
                .orElseThrow(() -> new ResourceNotFoundException(WORKSHOP_NOT_FOUND_BY_ID + id));
    }

    private LocalDate resolveOpeningDate(LocalDate openingDate) {
        return openingDate != null ? openingDate : LocalDate.now();
    }

    private String generateOrderNumber() {
        long nextNumber = workshopOrderRepository.count() + 1;
        return "WO-" + Year.now().getValue() + "-" + String.format("%06d", nextNumber);
    }
}
