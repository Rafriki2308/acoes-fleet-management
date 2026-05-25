package com.acoes.fleetmanagement.garage.infrastructure;

import com.acoes.fleetmanagement.garage.application.GarageService;
import com.acoes.fleetmanagement.garage.infrastructure.dto.CreateGarageRequest;
import com.acoes.fleetmanagement.garage.infrastructure.dto.GarageResponse;
import com.acoes.fleetmanagement.garage.infrastructure.dto.PatchGarageRequest;
import com.acoes.fleetmanagement.garage.infrastructure.dto.UpdateGarageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.EndpointConstants.*;

/**
 * Expone los endpoints REST para consultar y administrar garajes.
 */
@RestController
@RequestMapping(GARAGES)
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;

    /**
     * Crea un garaje.
     *
     * @param request datos del garaje a crear.
     * @return garaje creado.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GarageResponse create(@Valid @RequestBody CreateGarageRequest request) {
        return garageService.create(request);
    }

    /**
     * Lista todos los garajes activos.
     *
     * @return garajes activos.
     */
    @GetMapping
    public List<GarageResponse> findAll() {
        return garageService.findAll();
    }

    /**
     * Obtiene un garaje activo por id.
     *
     * @param id identificador interno del garaje.
     * @return garaje encontrado.
     */
    @GetMapping(GET_BY_ID)
    public GarageResponse findById(@PathVariable Long id) {
        return garageService.findById(id);
    }

    /**
     * Reemplaza los datos principales de un garaje activo.
     *
     * @param id identificador interno del garaje.
     * @param request datos nuevos del garaje.
     * @return garaje actualizado.
     */
    @PutMapping(PUT_BY_ID)
    public GarageResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGarageRequest request
    ) {
        return garageService.update(id, request);
    }

    /**
     * Actualiza parcialmente un garaje activo.
     *
     * @param id identificador interno del garaje.
     * @param request datos opcionales a modificar.
     * @return garaje actualizado.
     */
    @PatchMapping(PATCH_BY_ID)
    public GarageResponse patch(
            @PathVariable Long id,
            @Valid @RequestBody PatchGarageRequest request
    ) {
        return garageService.patch(id, request);
    }

    /**
     * Da de baja logicamente un garaje activo.
     *
     * @param id identificador interno del garaje.
     */
    @PatchMapping(PATCH_OFF_BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        garageService.deactivate(id);
    }
}
