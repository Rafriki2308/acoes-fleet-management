package com.acoes.fleetmanagement.garage.application;

import com.acoes.fleetmanagement.garage.infrastructure.dto.CreateGarageRequest;
import com.acoes.fleetmanagement.garage.infrastructure.dto.GarageResponse;
import com.acoes.fleetmanagement.garage.infrastructure.dto.PatchGarageRequest;
import com.acoes.fleetmanagement.garage.infrastructure.dto.UpdateGarageRequest;

import java.util.List;

/**
 * Define las operaciones de aplicacion disponibles para garajes.
 */
public interface GarageService {

    /**
     * Crea un garaje activo con sus datos normalizados.
     *
     * @param request datos necesarios para crear el garaje.
     * @return garaje creado.
     */
    GarageResponse create(CreateGarageRequest request);

    /**
     * Obtiene todos los garajes activos.
     *
     * @return lista de garajes activos.
     */
    List<GarageResponse> findAll();

    /**
     * Busca un garaje activo por su identificador interno.
     *
     * @param id identificador interno del garaje.
     * @return garaje encontrado.
     */
    GarageResponse findById(Long id);

    /**
     * Reemplaza los datos principales de un garaje activo.
     *
     * @param id identificador interno del garaje.
     * @param request datos que reemplazan el estado actual.
     * @return garaje actualizado.
     */
    GarageResponse update(Long id, UpdateGarageRequest request);

    /**
     * Actualiza parcialmente los datos de un garaje activo.
     *
     * @param id identificador interno del garaje.
     * @param request datos opcionales a modificar.
     * @return garaje actualizado.
     */
    GarageResponse patch(Long id, PatchGarageRequest request);

    /**
     * Da de baja logicamente un garaje activo.
     *
     * @param id identificador interno del garaje.
     */
    void deactivate(Long id);
}
