package com.acoes.fleetmanagement.vehicle.infraestructure.dto;

import com.acoes.fleetmanagement.vehicle.domain.model.VehicleStatus;
import com.acoes.fleetmanagement.vehicle.domain.model.VehicleType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

import static com.acoes.fleetmanagement.shared.constants.ValidationMessageConstants.*;
import static com.acoes.fleetmanagement.shared.constants.ValidationConstants.REGEX_VALIDATION_PLATES;
import static com.acoes.fleetmanagement.shared.constants.ValidationConstants.REGEX_VALIDATION_VIN;

public record CreateVehicleRequest(

        @NotBlank(message = NUMBER_PLATE_REQUIRED)
        @Pattern(
                regexp = REGEX_VALIDATION_PLATES,
                message = PLATE_NUMBER_NOT_VALID_MESSAGE)
        String plateNumber,

        @NotBlank(message = VIN_IS_REQUIRED)
        @Pattern(
                regexp = REGEX_VALIDATION_VIN,
                message = VIN_NOT_VALID_MESSAGE
        )
        String vin,

        @NotBlank(message = BRAND_REQUIRED)
        String brand,

        @NotBlank(message = MODEL_REQUIRED)
        String model,

        String color,

        @NotNull(message = VEHICLE_TYPE_REQUIRED)
        VehicleType type,

        @NotNull(message = VEHICLE_STATUS_REQUIRED)
        VehicleStatus status,

        @PositiveOrZero(message = MILEAGE_POSITIVE)
        Double currentMileage,

        @PastOrPresent(message = REGISTRATION_DATE_PAST)
        LocalDate officialRegistrationDate,

        @Future(message = EXPIRATION_INSURACE_FUTURE)
        LocalDate insuranceExpirationDate,

        Long currentGarageId,

        String notes

) {
}
