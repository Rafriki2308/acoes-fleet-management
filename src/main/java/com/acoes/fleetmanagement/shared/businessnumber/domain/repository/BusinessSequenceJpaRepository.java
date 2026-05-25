package com.acoes.fleetmanagement.shared.businessnumber.domain.repository;

import com.acoes.fleetmanagement.shared.businessnumber.domain.BusinessSequenceJpaEntity;
import com.acoes.fleetmanagement.shared.businessnumber.domain.model.BusinessSequenceKey;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para bloquear y actualizar secuencias de numeracion de negocio.
 */
@Repository
public interface BusinessSequenceJpaRepository
        extends JpaRepository<BusinessSequenceJpaEntity, Long> {

    /**
     * Busca una secuencia por clave, ano y mes aplicando bloqueo pesimista de escritura.
     *
     * @param sequenceKey clave funcional de la secuencia.
     * @param year ano de la secuencia.
     * @param month mes de la secuencia.
     * @return secuencia encontrada, si existe.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BusinessSequenceJpaEntity> findBySequenceKeyAndYearAndMonth(
            BusinessSequenceKey sequenceKey,
            Integer year,
            Integer month
    );
}
