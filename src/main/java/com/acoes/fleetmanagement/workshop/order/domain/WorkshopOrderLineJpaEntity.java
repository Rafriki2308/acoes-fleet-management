package com.acoes.fleetmanagement.workshop.order.domain;

import com.acoes.fleetmanagement.workshop.order.domain.model.WorkshopOrderLinePriority;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "workshop_order_lines",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_workshop_order_line_number",
                        columnNames = {"workshop_order_id", "line_number"}
                )
        }
)
public class WorkshopOrderLineJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_order_id", nullable = false)
    private WorkshopOrderJpaEntity workshopOrder;

    @Positive
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;

    @NotBlank
    @Column(name = "work_description", nullable = false, length = 1000)
    private String workDescription;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WorkshopOrderLinePriority priority;

    @Column(nullable = false)
    private boolean active = true;
}
