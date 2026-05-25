package com.acoes.fleetmanagement.workshop.order.application.repository;

import com.acoes.fleetmanagement.workshop.order.domain.OrderLineJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para acceder a lineas de orden de taller.
 */
public interface OrderLineJpaRepository extends JpaRepository<OrderLineJpaEntity, Long> {

    /**
     * Busca lineas activas de una orden por su identificador interno.
     *
     * @param workshopOrderId identificador interno de la orden.
     * @return lista de lineas activas ordenadas por numero.
     */
    List<OrderLineJpaEntity> findByWorkshopOrderIdAndActiveTrueOrderByLineNumberAsc(Long workshopOrderId);

    /**
     * Busca una linea activa por numero de orden y numero de linea.
     *
     * @param orderNumber numero funcional de la orden.
     * @param lineNumber numero de linea dentro de la orden.
     * @return linea encontrada, si existe.
     */
    Optional<OrderLineJpaEntity> findByWorkshopOrderOrderNumberAndLineNumberAndActiveTrue(
            String orderNumber,
            Integer lineNumber
    );

    /**
     * Comprueba si una orden ya tiene una linea con el numero indicado.
     *
     * @param workshopOrderId identificador interno de la orden.
     * @param lineNumber numero de linea.
     * @return {@code true} si existe una coincidencia.
     */
    boolean existsByWorkshopOrderIdAndLineNumber(Long workshopOrderId, Integer lineNumber);
}
