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
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.ExceptionMessageConstans.GARAGE_NOT_FOUND_BY_ID;
import static com.acoes.fleetmanagement.shared.constants.ValidationMessageConstants.GARAGE_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class GarageServiceImpl implements GarageService {

    @Autowired
    private GarageJpaRepository garageRepository;
    @Autowired
    private GarageMapper garageMapper;

    @Override
    @Transactional
    public GarageResponse create(CreateGarageRequest request) {

        validateGarageNameUniqueness(request.name(), null);

        GarageJpaEntity entity = garageMapper.toEntity(request);

        normalizeGarageFields(entity);

        return garageMapper.toResponse(garageRepository.save(entity));
    }

    @Override
    @Transactional(readOnly=true)
    public List<GarageResponse> findAll() {
        return garageRepository.findByActiveTrue()
                .stream()
                .map(garageMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly=true)
    public GarageResponse findById(Long id) {
        return garageMapper.toResponse(findActiveById(id));
    }

    @Override
    @Transactional
    public GarageResponse update(Long id, UpdateGarageRequest request) {

        GarageJpaEntity entity = findActiveById(id);

        // Validar duplicado SOLO si cambia
        validateGarageNameUniqueness(request.name(), entity);

        // Actualizar campos
        updateGarageFields(entity, request);

        return garageMapper.toResponse(garageRepository.save(entity));
    }

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


    @Override
    @Transactional
    public void deactivate(Long id) {
        GarageJpaEntity garage = findActiveById(id);

        deactivateGarage(garage);
    }

    private void deactivateGarage(GarageJpaEntity garage) {
        // Managed entity → Hibernate will persist change automatically
        garage.setActive(false);
    }

    private GarageJpaEntity findActiveById(Long id) {
        return garageRepository.findById(id)
                .filter(GarageJpaEntity::isActive)
                .orElseThrow(() -> new ResourceNotFoundException(GARAGE_NOT_FOUND_BY_ID + id));
    }

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
