package com.acoes.fleetmanagement.vehicle.infrastructure.dto;

import com.acoes.fleetmanagement.vehicle.domain.model.VehicleStatus;
import com.acoes.fleetmanagement.vehicle.domain.model.VehicleType;

import java.time.LocalDate;
/**
 * DTO de salida con la informacion publica de un vehiculo.
 */

public record VehicleResponse(

        Long id,

        String plateNumber,

        String vin,

        String brand,

        String model,

        String color,

        VehicleType type,

        VehicleStatus status,

        Double currentMileage,

        LocalDate officialRegistrationDate,

        LocalDate insuranceExpirationDate,

        Long currentGarageId,

        String currentGarageName,

        String notes,

        boolean active

) {
}
