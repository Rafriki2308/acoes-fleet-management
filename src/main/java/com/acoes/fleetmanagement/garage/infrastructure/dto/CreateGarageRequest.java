package com.acoes.fleetmanagement.garage.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.acoes.fleetmanagement.shared.constants.ValidationConstants.HONDURAS_PHONE_REGEX;
import static com.acoes.fleetmanagement.shared.constants.ValidationConstants.HONDURAS_POSTAL_CODE_REGEX;
import static com.acoes.fleetmanagement.shared.constants.ValidationMessageConstants.*;

public record CreateGarageRequest(@NotBlank(message = GARAGE_NAME_REQUIRED)
                                  String name,

                                  String street,
                                  String city,
                                  String province,
                                  @Pattern(
                                          regexp = HONDURAS_POSTAL_CODE_REGEX,
                                          message = POSTAL_CODE_INVALID
                                  )
                                  String postalCode,
                                  String country,

                                  @NotBlank(message = CONTACT_NAME_REQUIRED)
                                  String contactName,
                                  @NotBlank(message = PHONE_CONTACT_REQUIRED)
                                  @Pattern(
                                          regexp = HONDURAS_PHONE_REGEX,
                                          message = PHONE_FORMAT_INVALID
                                  )
                                  String phone,
                                  @Email(message = EMAIL_FORMAT_INVALID)
                                  @Size(max = 100)
                                  String email,
                                  String notes) {}
