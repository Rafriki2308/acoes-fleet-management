package com.acoes.fleetmanagement.shared.constants;

public class ExceptionMessageConstants {

    //Vehicle Messages
    public static final String NUMBER_PLATE_REQUIRED = "Plate number is required";
    public static final String VIN_IS_REQUIRED = "VIN is required";
    public static final String VIN_NOT_VALID_MESSAGE = "VIN must have 17 characters and cannot contain I, O or Q";
    public static final String BRAND_REQUIRED = "Brand is required";
    public static final String MODEL_REQUIRED = "Model is required";
    public static final String VEHICLE_TYPE_REQUIRED = "Vehicle type is required";
    public static final String VEHICLE_STATUS_REQUIRED = "Vehicle status is required";
    public static final String MILEAGE_POSITIVE = "Current mileage must be zero or positive";
    public static final String REGISTRATION_DATE_PAST = "Official registration date cannot be in the future";
    public static final String EXPIRATION_INSURACE_FUTURE = "Insurance expiration date must be in the future";
    public static final String VEHICLE_NOT_FOUND_BY_ID = "Vehicle not found with id: ";
    public static final String PLATE_NUMBER_NOT_VALID_MESSAGE = "Plate number must contain 6 or 7 alphanumeric characters";

}
