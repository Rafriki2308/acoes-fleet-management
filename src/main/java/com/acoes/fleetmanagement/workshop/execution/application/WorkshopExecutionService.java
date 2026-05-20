package com.acoes.fleetmanagement.workshop.execution.application;

import com.acoes.fleetmanagement.workshop.execution.infractrusture.dto.CreateExecutionRequest;
import com.acoes.fleetmanagement.workshop.execution.infractrusture.dto.ExecutionResponse;
import com.acoes.fleetmanagement.workshop.execution.infractrusture.dto.UpdateExecutionStatusRequest;

import java.util.List;

public interface WorkshopExecutionService {

    ExecutionResponse create(
            String orderNumber,
            CreateExecutionRequest request
    );

    List<ExecutionResponse> findAll();

    ExecutionResponse findByExecutionNumber(String executionNumber);

    ExecutionResponse findByOrderNumber(String orderNumber);

    List<ExecutionResponse> findByVehiclePlateNumber(String plateNumber);

    List<ExecutionResponse> findByVehicleVin(String vin);

    ExecutionResponse updateStatus(
            String executionNumber,
            UpdateExecutionStatusRequest request
    );

    void deactivate(String executionNumber);
}
