package com.acoes.fleetmanagement.workshop.execution.domain;

import com.acoes.fleetmanagement.workshop.execution.domain.model.ExecutionLineStatus;
import com.acoes.fleetmanagement.workshop.execution.domain.model.ExecutionLineType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidad JPA que representa una linea de trabajo ejecutada dentro de una ejecucion de taller.
 */
@Getter
@Setter
@Entity
@Table(
        name = "execution_lines",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_execution_line_number",
                        columnNames = {
                                "execution_id",
                                "line_number"
                        }
                )
        }
)
public class ExecutionLineJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cabecera ejecución asociada.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_id", nullable = false)
    private ExecutionJpaEntity execution;

    /**
     * Número funcional línea.
     * <p>
     * Único dentro de una ejecución.
     */
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;

    /**
     * Descripción trabajo real realizado.
     */
    @Column(nullable = false, length = 2000)
    private String description;

    /**
     * Tipo línea:
     * mano obra, pieza o externo.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ExecutionLineType type;

    /**
     * Cantidad:
     * horas, unidades, litros...
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    /**
     * Estado línea.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ExecutionLineStatus status =
            ExecutionLineStatus.PENDING;

    /**
     * Baja lógica.
     */
    @Column(nullable = false)
    private boolean active = true;
}
