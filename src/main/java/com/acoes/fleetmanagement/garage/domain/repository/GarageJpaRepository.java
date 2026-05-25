package com.acoes.fleetmanagement.garage.domain.repository;

import com.acoes.fleetmanagement.garage.domain.GarageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para acceder a garajes activos y validar nombres unicos.
 */
public interface GarageJpaRepository extends JpaRepository<GarageJpaEntity, Long> {

    /**
     * Busca un garaje por nombre normalizado.
     *
     * @param name nombre normalizado del garaje.
     * @return garaje encontrado, si existe.
     */
    Optional<GarageJpaEntity> findByName(String name);

    /**
     * Comprueba si existe un garaje con el nombre indicado.
     *
     * @param name nombre normalizado del garaje.
     * @return {@code true} si existe una coincidencia.
     */
    boolean existsByName(String name);

    /**
     * Obtiene todos los garajes activos.
     *
     * @return lista de garajes activos.
     */
    List<GarageJpaEntity> findByActiveTrue();
}
