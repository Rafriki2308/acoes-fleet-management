package com.acoes.fleetmanagement.workshop.order.application.repository;

import com.acoes.fleetmanagement.workshop.order.domain.WorkshopOrderLineJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkshopOrderLineJpaRepository extends JpaRepository<WorkshopOrderLineJpaEntity, Long> {

    List<WorkshopOrderLineJpaEntity> findByWorkshopOrderIdAndActiveTrueOrderByLineNumberAsc(Long workshopOrderId);

    boolean existsByWorkshopOrderIdAndLineNumber(Long workshopOrderId, Integer lineNumber);
}
