package com.acoes.fleetmanagement.shared.businessnumber.domain.repository;

import com.acoes.fleetmanagement.shared.businessnumber.domain.BusinessSequenceJpaEntity;
import com.acoes.fleetmanagement.shared.businessnumber.domain.model.BusinessSequenceKey;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessSequenceJpaRepository
        extends JpaRepository<BusinessSequenceJpaEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BusinessSequenceJpaEntity> findBySequenceKeyAndYearAndMonth(
            BusinessSequenceKey sequenceKey,
            Integer year,
            Integer month
    );
}