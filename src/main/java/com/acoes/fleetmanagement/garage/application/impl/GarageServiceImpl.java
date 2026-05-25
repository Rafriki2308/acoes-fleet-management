package com.acoes.fleetmanagement.garage.application.impl;

import com.acoes.fleetmanagement.garage.application.GarageService;
import com.acoes.fleetmanagement.garage.application.mapper.GarageMapper;
import com.acoes.fleetmanagement.garage.domain.AddressEmbeddable;
import com.acoes.fleetmanagement.garage.domain.GarageJpaEntity;
import com.acoes.fleetmanagement.garage.domain.repository.GarageJpaRepository;
import com.acoes.fleetmanagement.garage.infrastructure.dto.CreateGarageRequest;
import com.acoes.fleetmanagement.garage.infrastructure.dto.GarageResponse;
import com.acoes.fleetmanagement.garage.infrastructure.dto.PatchGarageRequest;
import com.acoes.fleetmanagement.garage.infrastructure.dto.UpdateGarageRequest;
import com.acoes.fleetmanagement.shared.exception.DuplicateResourceException;
import com.acoes.fleetmanagement.shared.exception.ResourceNotFoundException;
import com.acoes.fleetmanagement.shared.validation.GarageNormalizatedUtil;
import com.acoes.fleetmanagement.shared.validation.NormalizatedTextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.ExceptionMessageConstans.GARAGE_NOT_FOUND_BY_ID;
import static com.acoes.fleetmanagement.shared.constants.ValidationMessageConstants.GARAGE_ALREADY_EXISTS;

/**
 * Implementa las reglas de negocio y persistencia para garajes.
 */
@Service
@RequiredArgsConstructor
public class GarageServiceImpl implements GarageService {

    private final GarageJpaRepository garageRepository;
    private final GarageMapper garageMapper;

    /**
     * Crea un garaje activo tras validar que su nombre sea unico y normalizar sus campos.
     *
     * @param request datos necesarios para crear el garaje.
     * @return garaje creado.
     * @throws DuplicateResourceException si ya existe un garaje con el mismo nombre normalizado.
     */
    @Override
    @Transactional
    public GarageResponse create(CreateGarageRequest request) {

        validateGarageNameUniqueness(request.name(), null);

        GarageJpaEntity entity = garageMapper.toEntity(request);

        normalizeGarageFields(entity);

        return garageMapper.toResponse(garageRepository.save(entity));
    }

    /**
     * Obtiene todos los garajes activos registrados.
     *
     * @return lista de garajes activos.
     */
    @Override
    @Transactional(readOnly=true)
    public List<GarageResponse> findAll() {
        return garageRepository.findByActiveTrue()
                .stream()
                .map(garageMapper::toResponse)
                .toList();
    }

    /**
     * Busca un garaje activo por su identificador interno.
     *
     * @param id identificador interno del garaje.
     * @return garaje encontrado.
     * @throws ResourceNotFoundException si no existe un garaje activo con ese id.
     */
    @Override
    @Transactional(readOnly=true)
    public GarageResponse findById(Long id) {
        return garageMapper.toResponse(findActiveById(id));
    }

    /**
     * Reemplaza los datos principales de un garaje activo y normaliza el resultado.
     *
     * @param id identificador interno del garaje.
     * @param request datos completos que reemplazan el estado actual.
     * @return garaje actualizado.
     * @throws DuplicateResourceException si el nombre ya pertenece a otro garaje.
     * @throws ResourceNotFoundException si no existe un garaje activo con ese id.
     */
    @Override
    @Transactional
    public GarageResponse update(Long id, UpdateGarageRequest request) {

        GarageJpaEntity entity = findActiveById(id);

        // Valida duplicados solo cuando cambia el nombre.
        validateGarageNameUniqueness(request.name(), entity);

        // Actualiza los campos editables.
        updateGarageFields(entity, request);

        return garageMapper.toResponse(garageRepository.save(entity));
    }

    /**
     * Actualiza parcialmente un garaje activo, inicializando la direccion embebida si es necesario.
     *
     * @param id identificador interno del garaje.
     * @param request datos opcionales a modificar.
     * @return garaje actualizado.
     * @throws DuplicateResourceException si el nombre informado ya pertenece a otro garaje.
     * @throws ResourceNotFoundException si no existe un garaje activo con ese id.
     */
    @Override
    @Transactional
    public GarageResponse patch(Long id, PatchGarageRequest request) {
        GarageJpaEntity entity = findActiveById(id);

        updateGarageNameIfExists(entity, request);

        ensureAddressExists(entity);

        garageMapper.patchEntityFromRequest(request, entity);

        normalizeGarageFields(entity);

        return garageMapper.toResponse(entity);
    }


    /**
     * Da de baja logicamente un garaje activo.
     *
     * @param id identificador interno del garaje.
     * @throws ResourceNotFoundException si no existe un garaje activo con ese id.
     */
    @Override
    @Transactional
    public void deactivate(Long id) {
        GarageJpaEntity garage = findActiveById(id);

        deactivateGarage(garage);
    }

    private void deactivateGarage(GarageJpaEntity garage) {
        // Hibernate persistira este cambio por dirty checking.
        garage.setActive(false);
    }

    /**
     * Recupera un garaje activo o lanza una excepcion si no existe.
     *
     * @param id identificador interno del garaje.
     * @return garaje activo encontrado.
     */
    private GarageJpaEntity findActiveById(Long id) {
        return garageRepository.findById(id)
                .filter(GarageJpaEntity::isActive)
                .orElseThrow(() -> new ResourceNotFoundException(GARAGE_NOT_FOUND_BY_ID + id));
    }

    /**
     * Valida que el nombre normalizado no pertenezca a otro garaje.
     *
     * @param name nombre recibido.
     * @param current garaje actual en operaciones de actualizacion.
     */
    private void validateGarageNameUniqueness(String name, GarageJpaEntity current) {

        String normalizeName = NormalizatedTextUtil.normalizeUpper(name);
        boolean isSame = current != null && normalizeName.equals(current.getName());

        if (!isSame && garageRepository.existsByName(normalizeName)) {
            throw new DuplicateResourceException(GARAGE_ALREADY_EXISTS);
        }
    }

    private void updateGarageFields(
            GarageJpaEntity entity,
            UpdateGarageRequest request
    ) {
        updateAddress(entity, request);
        entity.setName(request.name());
        entity.setContactName(request.contactName());
        entity.setPhone(request.phone());
        entity.setEmail(request.email());
        entity.setNotes(request.notes());
        normalizeGarageFields(entity);
    }

    private void updateAddress(GarageJpaEntity entity, UpdateGarageRequest request) {
        if (entity.getAddress() == null) {
            entity.setAddress(new AddressEmbeddable());
        }

        AddressEmbeddable address = entity.getAddress();

        address.setStreet(request.street());
        address.setCity(request.city());
        address.setProvince(request.province());
        address.setPostalCode(request.postalCode());
        address.setCountry(request.country());
    }

    /**
     * Normaliza nombre, contacto, direccion y telefono del garaje.
     *
     * @param entity garaje que debe normalizarse.
     */
    private void normalizeGarageFields(GarageJpaEntity entity) {

        entity.setName(NormalizatedTextUtil.normalizeUpper(entity.getName()));
        entity.setContactName(NormalizatedTextUtil.normalizeUpper(entity.getContactName()));
        if (entity.getAddress() != null) {
            entity.getAddress().setStreet(NormalizatedTextUtil.normalizeUpper(entity.getAddress().getStreet()));
            entity.getAddress().setCity(NormalizatedTextUtil.normalizeUpper(entity.getAddress().getCity()));
            entity.getAddress().setProvince(NormalizatedTextUtil.normalizeUpper(entity.getAddress().getProvince()));
            entity.getAddress().setPostalCode(NormalizatedTextUtil.normalizeUpper(entity.getAddress().getPostalCode()));
            entity.getAddress().setCountry(NormalizatedTextUtil.normalizeUpper(entity.getAddress().getCountry()));
        }
        entity.setPhone(GarageNormalizatedUtil.normalizeHondurasPhone(entity.getPhone()));
    }

    private void ensureAddressExists(GarageJpaEntity entity) {
        if (entity.getAddress() == null) {
            entity.setAddress(new AddressEmbeddable());
        }
    }

    private void updateGarageNameIfExists(GarageJpaEntity entity, PatchGarageRequest request) {
        if (request.name() != null) {
            return;
        }
        String normalizedName = NormalizatedTextUtil.normalizeUpper(request.name());
        validateGarageNameUniqueness(normalizedName, entity);
        entity.setName(normalizedName);

    }


}
