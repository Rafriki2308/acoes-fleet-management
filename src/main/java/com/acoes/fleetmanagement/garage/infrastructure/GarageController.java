package com.acoes.fleetmanagement.garage.infrastructure;

import com.acoes.fleetmanagement.garage.application.GarageService;
import com.acoes.fleetmanagement.garage.infrastructure.dto.CreateGarageRequest;
import com.acoes.fleetmanagement.garage.infrastructure.dto.GarageResponse;
import com.acoes.fleetmanagement.garage.infrastructure.dto.PatchGarageRequest;
import com.acoes.fleetmanagement.garage.infrastructure.dto.UpdateGarageRequest;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.EndpointConstants.*;

@RestController
@RequestMapping(MAIN_GARAGE_ENDPOINT)
@NoArgsConstructor
public class GarageController {

    @Autowired
    private GarageService garageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GarageResponse create(@Valid @RequestBody CreateGarageRequest request) {
        return garageService.create(request);
    }

    @GetMapping
    public List<GarageResponse> findAll() {
        return garageService.findAll();
    }

    @GetMapping(BY_ID)
    public GarageResponse findById(@PathVariable Long id) {
        return garageService.findById(id);
    }

    @PutMapping(BY_ID)
    public GarageResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGarageRequest request
    ) {
        return garageService.update(id, request);
    }

    @PatchMapping(BY_ID)
    public GarageResponse patch(
            @PathVariable Long id,
            @Valid @RequestBody PatchGarageRequest request
    ) {
        return garageService.patch(id, request);
    }

    @PatchMapping(DEACTIVATE_BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        garageService.deactivate(id);
    }
}
