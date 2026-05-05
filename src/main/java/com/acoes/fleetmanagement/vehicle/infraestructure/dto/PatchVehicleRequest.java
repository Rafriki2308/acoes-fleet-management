package com.acoes.fleetmanagement.vehicle.infraestructure.dto;

import com.acoes.fleetmanagement.vehicle.domain.model.VehicleStatus;
import com.acoes.fleetmanagement.vehicle.domain.model.VehicleType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

import static com.acoes.fleetmanagement.shared.constants.ValidationConstants.REGEX_VALIDATION_PLATES;
import static com.acoes.fleetmanagement.shared.constants.ValidationConstants.REGEX_VALIDATION_VIN;
import static com.acoes.fleetmanagement.shared.constants.ValidationMessageConstants.*;

public record PatchVehicleRequest(@Pattern(
                                          regexp = REGEX_VALIDATION_PLATES,
                                          message = PLATE_NUMBER_NOT_VALID_MESSAGE)
                                  String plateNumber,

                                  @Pattern(
                                          regexp = REGEX_VALIDATION_VIN,
                                          message = VIN_NOT_VALID_MESSAGE
                                  )
                                  String vin,
                                  String brand,

                                  String model,

                                  String color,
                                  VehicleType type,
                                  VehicleStatus status,

                                  @PositiveOrZero(message = MILEAGE_POSITIVE)
                                  Double currentMileage,

                                  @PastOrPresent(message = REGISTRATION_DATE_PAST)
                                  LocalDate officialRegistrationDate,

                                  @Future(message = EXPIRATION_INSURACE_FUTURE)
                                  LocalDate insuranceExpirationDate,

                                  Long currentGarageId,

                                  String notes) {
}
