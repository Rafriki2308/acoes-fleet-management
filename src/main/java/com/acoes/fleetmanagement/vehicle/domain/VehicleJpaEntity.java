package com.acoes.fleetmanagement.vehicle.domain;

import com.acoes.fleetmanagement.garage.domain.GarageJpaEntity;
import com.acoes.fleetmanagement.vehicle.domain.model.VehicleStatus;
import com.acoes.fleetmanagement.vehicle.domain.model.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.acoes.fleetmanagement.shared.constants.ErrorMenssagesConstants.VIN_NOT_VALID_MESSAGE;
import static com.acoes.fleetmanagement.shared.constants.ValidationConstants.*;

@Getter
@Setter
@Entity
@Table(
        name = "vehicles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_vehicle_plate_number", columnNames = "plate_number"),
                @UniqueConstraint(name = "uk_vehicle_vin", columnNames = "vin")
        }
)
public class VehicleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "plate_number", nullable = false, unique = true, length = 20)
    private String plateNumber;

    @NotBlank
    @Pattern(
            regexp = REGEX_VALIDATION_VIN,
            message = VIN_NOT_VALID_MESSAGE
    )
    @Column(name = "vin", nullable = false, unique = true, length = 17)
    private String vin;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String brand;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String model;

    @Column(length = 40)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 30)
    private VehicleType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VehicleStatus status;

    @PositiveOrZero
    @Column(name = "current_mileage")
    private Double currentMileage;

    @PastOrPresent
    @Column(name = "official_registration_date")
    private LocalDate officialRegistrationDate;

    @Future
    @Column(name = "insurance_expiration_date")
    private LocalDate insuranceExpirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_garage_id")
    private GarageJpaEntity currentGarage;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private boolean active = true;
}
