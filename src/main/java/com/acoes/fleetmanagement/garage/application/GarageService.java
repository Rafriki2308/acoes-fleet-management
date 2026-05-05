package com.acoes.fleetmanagement.garage.application;

import com.acoes.fleetmanagement.garage.infrastructure.dto.CreateGarageRequest;
import com.acoes.fleetmanagement.garage.infrastructure.dto.GarageResponse;
import com.acoes.fleetmanagement.garage.infrastructure.dto.PatchGarageRequest;
import com.acoes.fleetmanagement.garage.infrastructure.dto.UpdateGarageRequest;

import java.util.List;

public interface GarageService {

    GarageResponse create(CreateGarageRequest request);

    List<GarageResponse> findAll();

    GarageResponse findById(Long id);

    GarageResponse update(Long id, UpdateGarageRequest request);

    GarageResponse patch(Long id, PatchGarageRequest request);

    void deactivate(Long id);
}
