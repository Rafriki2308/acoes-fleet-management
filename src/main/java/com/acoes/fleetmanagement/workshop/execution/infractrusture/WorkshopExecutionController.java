package com.acoes.fleetmanagement.workshop.execution.infractrusture;

import com.acoes.fleetmanagement.workshop.execution.application.WorkshopExecutionService;
import com.acoes.fleetmanagement.workshop.execution.infractrusture.dto.CreateExecutionRequest;
import com.acoes.fleetmanagement.workshop.execution.infractrusture.dto.ExecutionResponse;
import com.acoes.fleetmanagement.workshop.execution.infractrusture.dto.UpdateExecutionStatusRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.acoes.fleetmanagement.shared.constants.EndpointConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(MAIN_WORKSHOP_EXECUTION_ENDPOINT)
public class WorkshopExecutionController {

    @Autowired
    private WorkshopExecutionService workshopExecutionService;

    @PostMapping(CREATE_WORKSHOP_EXECUTION)
    @ResponseStatus(HttpStatus.CREATED)
    public ExecutionResponse create(
            @PathVariable String orderNumber,
            @Valid @RequestBody CreateExecutionRequest request
    ) {
        return workshopExecutionService.create(orderNumber, request);
    }

    @GetMapping
    public List<ExecutionResponse> findAll() {
        return workshopExecutionService.findAll();
    }

    @GetMapping(FIND_EXECUTION_BY_EXECUTION_NUMBER)
    public ExecutionResponse findByExecutionNumber(
            @PathVariable String executionNumber
    ) {
        return workshopExecutionService.findByExecutionNumber(executionNumber);
    }

    @GetMapping(FIND_EXECUTION_BY_ORDER_NUMBER)
    public ExecutionResponse findByOrderNumber(
            @PathVariable String orderNumber
    ) {
        return workshopExecutionService.findByOrderNumber(orderNumber);
    }

    @GetMapping(FIND_EXECUTION_BY_PLATE)
    public List<ExecutionResponse> findByVehiclePlateNumber(
            @PathVariable String plateNumber
    ) {
        return workshopExecutionService.findByVehiclePlateNumber(plateNumber);
    }

    @GetMapping(FIND_EXECUTION_BY_VIN)
    public List<ExecutionResponse> findByVehicleVin(
            @PathVariable String vin
    ) {
        return workshopExecutionService.findByVehicleVin(vin);
    }

    @PatchMapping(UPDATE_STATUS_EXECUTION_BY_NUMBER)
    public ExecutionResponse updateStatus(
            @PathVariable String executionNumber,
            @Valid @RequestBody UpdateExecutionStatusRequest request
    ) {
        return workshopExecutionService.updateStatus(
                executionNumber,
                request
        );
    }

    @PatchMapping(DEACTIVATE_EXECUTION_BY_NUMBER)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable String executionNumber) {
        workshopExecutionService.deactivate(executionNumber);
    }
}
