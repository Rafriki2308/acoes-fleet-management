package com.acoes.fleetmanagement.workshop.execution.domain;

import com.acoes.fleetmanagement.workshop.execution.domain.model.WorkshopExecutionStatus;
import com.acoes.fleetmanagement.workshop.order.domain.WorkshopOrderJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@Entity
@Table(
        name = "workshop_executions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_workshop_execution_number",
                        columnNames = "execution_number"
                ),
                @UniqueConstraint(
                        name = "uk_workshop_execution_order",
                        columnNames = "workshop_order_id"
                )
        }
)
public class WorkshopExecutionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Número funcional de ejecución.
     * <p>
     * Ejemplo:
     * EX-2026-000001
     */
    @Column(name = "execution_number", nullable = false, unique = true, length = 40)
    private String executionNumber;

    /**
     * Orden de taller asociada.
     * <p>
     * Una orden solo puede tener
     * una ejecución activa.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_order_id", nullable = false)
    private WorkshopOrderJpaEntity workshopOrder;

    /**
     * Snapshot del número de orden.
     */
    @Column(name = "workshop_order_number", nullable = false, length = 40)
    private String workshopOrderNumber;

    /**
     * Estado real de ejecución.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WorkshopExecutionStatus status =
            WorkshopExecutionStatus.IN_PROGRESS;

    /**
     * Fecha inicio real.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * Fecha finalización real.
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Resumen técnico final.
     */
    @Column(name = "final_summary", length = 4000)
    private String finalSummary;

    /**
     * Baja lógica.
     */
    @Column(nullable = false)
    private boolean active = true;
}
