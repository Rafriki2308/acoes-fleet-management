package com.acoes.fleetmanagement.workshop.order.domain;

import com.acoes.fleetmanagement.vehicle.domain.VehicleJpaEntity;
import com.acoes.fleetmanagement.workshop.order.domain.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad JPA que representa una orden de taller asociada a un vehiculo.
 */
@Getter
@Setter
@Entity
@Table(
        name = "workshop_orders",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_workshop_order_number", columnNames = "order_number")
        }
)
public class OrderJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true, length = 40)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleJpaEntity vehicle;

    @Column(name = "vehicle_plate_number", nullable = false, length = 20)
    private String vehiclePlateNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status = OrderStatus.OPEN;

    @Column(name = "opening_date", nullable = false)
    private LocalDate openingDate;

    @Column(name = "closing_date")
    private LocalDate closingDate;

    @Column(nullable = false)
    private boolean active = true;
}
