package com.acoes.fleetmanagement.workshop.order.application.impl;

import com.acoes.fleetmanagement.shared.businessnumber.domain.application.BusinessNumberGenerator;
import com.acoes.fleetmanagement.shared.businessnumber.domain.model.BusinessSequenceKey;
import com.acoes.fleetmanagement.shared.exception.ResourceNotFoundException;
import com.acoes.fleetmanagement.shared.validation.VehicleNormalizationUtils;
import com.acoes.fleetmanagement.vehicle.domain.VehicleJpaEntity;
import com.acoes.fleetmanagement.vehicle.domain.repository.VehicleJpaRepository;
import com.acoes.fleetmanagement.workshop.order.application.OrderService;
import com.acoes.fleetmanagement.workshop.order.application.mapper.OrderDetailMapper;
import com.acoes.fleetmanagement.workshop.order.application.mapper.OrderLineMapper;
import com.acoes.fleetmanagement.workshop.order.application.mapper.OrderMapper;
import com.acoes.fleetmanagement.workshop.order.application.repository.OrderJpaRepository;
import com.acoes.fleetmanagement.workshop.order.application.repository.OrderLineJpaRepository;
import com.acoes.fleetmanagement.workshop.order.domain.OrderJpaEntity;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.CreateOrderRequest;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.OrderDetailResponse;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.OrderLineResponse;
import com.acoes.fleetmanagement.workshop.order.infrastructure.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.ExceptionMessageConstans.*;


/**
 * Implementa las reglas de negocio y persistencia para ordenes de taller.
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository orderRepository;

    private final OrderLineJpaRepository orderLineRepository;

    private final VehicleJpaRepository vehicleRepository;

    private final BusinessNumberGenerator businessNumberGenerator;

    private final OrderMapper orderMapper;

    private final OrderLineMapper orderLineMapper;

    private final OrderDetailMapper orderDetailMapper;

    /**
     * Crea una orden de taller para un vehiculo activo y genera su numero funcional.
     *
     * @param request datos necesarios para abrir la orden.
     * @return orden creada.
     * @throws ResourceNotFoundException si el vehiculo indicado no existe o esta inactivo.
     */
    @Override
    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        VehicleJpaEntity vehicle = findActiveVehicleById(request.vehicleId());

        OrderJpaEntity entity = orderMapper.toEntity(request, vehicle);
        // La secuencia funcional evita duplicados cuando se crean ordenes en paralelo.
        entity.setOrderNumber(businessNumberGenerator.generate("WO", BusinessSequenceKey.WORKSHOP_ORDER));

        return orderMapper.toResponse(orderRepository.save(entity));
    }

    /**
     * Obtiene todas las ordenes de taller activas.
     *
     * @return lista de ordenes activas.
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findByActiveTrue()
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    /**
     * Busca una orden activa por su identificador interno.
     *
     * @param id identificador interno de la orden.
     * @return orden encontrada.
     * @throws ResourceNotFoundException si no existe una orden activa con ese id.
     */
    @Override
    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        return orderMapper.toResponse(findActiveOrderById(id));
    }

    /**
     * Busca una orden activa por su numero funcional.
     *
     * @param orderNumber numero funcional de la orden.
     * @return orden encontrada.
     * @throws ResourceNotFoundException si no existe una orden activa con ese numero.
     */
    @Override
    @Transactional(readOnly = true)
    public OrderResponse findByOrderNumber(String orderNumber) {
        OrderJpaEntity entity = orderRepository.findByOrderNumberAndActiveTrue(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(WORKSHOP_NOT_FOUND_BY_NUMBER + orderNumber));

        return orderMapper.toResponse(entity);
    }

    /**
     * Obtiene las ordenes activas asociadas a un vehiculo.
     *
     * @param vehicleId identificador interno del vehiculo.
     * @return lista de ordenes asociadas al vehiculo.
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByVehicleId(Long vehicleId) {
        return orderRepository.findByVehicleIdAndActiveTrue(vehicleId)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    /**
     * Obtiene ordenes activas por matricula de vehiculo normalizada.
     *
     * @param plateNumber matricula recibida.
     * @return lista de ordenes asociadas al vehiculo.
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByVehiclePlateNumber(String plateNumber) {

        String normalizedPlateNumber = VehicleNormalizationUtils.normalizePlateNumber(plateNumber);

        return orderRepository.findByVehiclePlateNumberAndActiveTrueOrderByOpeningDateDesc(normalizedPlateNumber)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    /**
     * Obtiene ordenes activas por VIN de vehiculo normalizado.
     *
     * @param vin VIN recibido.
     * @return lista de ordenes asociadas al vehiculo.
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByVehicleVin(String vin) {

        String normalizedVin = VehicleNormalizationUtils.normalizeVin(vin);

        return orderRepository
                .findByVehicleVinAndActiveTrueOrderByOpeningDateDesc(normalizedVin)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    /**
     * Recupera una orden activa junto con sus lineas activas ordenadas por numero de linea.
     *
     * @param orderNumber numero funcional de la orden.
     * @return detalle de la orden con sus lineas.
     * @throws ResourceNotFoundException si no existe una orden activa con ese numero.
     */
    @Override
    @Transactional(readOnly = true)
    public OrderDetailResponse findOrderWithLinesByOrderNumber(String orderNumber) {

        OrderJpaEntity order = orderRepository
                .findByOrderNumberAndActiveTrue(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workshop order not found with number: " + orderNumber
                ));

        List<OrderLineResponse> lines = orderLineRepository
                .findByWorkshopOrderIdAndActiveTrueOrderByLineNumberAsc(order.getId())
                .stream()
                .map(orderLineMapper::toResponse)
                .toList();

        return orderDetailMapper.toResponse(order, lines);
    }

    /**
     * Da de baja logicamente una orden activa.
     *
     * @param id identificador interno de la orden.
     * @throws ResourceNotFoundException si no existe una orden activa con ese id.
     */
    @Override
    @Transactional
    public void deactivate(Long id) {
        OrderJpaEntity entity = findActiveOrderById(id);

        // Hibernate persistira este cambio por dirty checking.
        entity.setActive(false);
    }

    /**
     * Recupera el vehiculo activo necesario para abrir una orden.
     *
     * @param id identificador interno del vehiculo.
     * @return vehiculo activo encontrado.
     */
    private VehicleJpaEntity findActiveVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .filter(VehicleJpaEntity::isActive)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_BY_ID + id));
    }

    /**
     * Recupera una orden activa o lanza una excepcion si no existe.
     *
     * @param id identificador interno de la orden.
     * @return orden activa encontrada.
     */
    private OrderJpaEntity findActiveOrderById(Long id) {
        return orderRepository.findById(id)
                .filter(OrderJpaEntity::isActive)
                .orElseThrow(() -> new ResourceNotFoundException(WORKSHOP_NOT_FOUND_BY_ID + id));
    }


    private LocalDate resolveOpeningDate(LocalDate openingDate) {
        return openingDate != null ? openingDate : LocalDate.now();
    }

    private String generateOrderNumber() {
        long nextNumber = orderRepository.count() + 1;
        return "WO-" + Year.now().getValue() + "-" + String.format("%06d", nextNumber);
    }
}
