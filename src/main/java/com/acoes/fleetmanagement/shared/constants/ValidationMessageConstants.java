package com.acoes.fleetmanagement.shared.constants;

/**
 * Agrupa mensajes reutilizables para validaciones de entrada.
 */

public class ValidationMessageConstants {

    // Vehicle messages.
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
    public static final String PLATE_NUMBER_NOT_VALID_MESSAGE = "Plate number must contain 6 or 7 alphanumeric " +
            "characters";
    public static final String VEHICLE_ID_IS_REQUIRED_MESSAGE = "Vehicle id is required";

    // Garage messages.
    public static final String GARAGE_NAME_REQUIRED = "Garage name is required";
    public static final String CONTACT_NAME_REQUIRED = "Contact name is required";
    public static final String PHONE_CONTACT_REQUIRED = "Phone Contact is required";
    public static final String GARAGE_ALREADY_EXISTS = "Garage already exists";
    public static final String EMAIL_FORMAT_INVALID = "Email format invalid";
    public static final String PHONE_FORMAT_INVALID = "Phone must be a valid Honduras number";
    public static final String POSTAL_CODE_INVALID = "Postal code must have 5 digits";

    // Workshop orders.
    public static final String WORKSHOP_ID_REQUIRED = "Workshop order id is required";
    public static final String WORKSHOP_ORDER_NUMBER_REQUIRED = "Workshop order number is required";


    // Workshop order lines.
    public static final String WORKSHOP_ORDER_LINE_MUST_POSITIVE = "Line number must be greater than zero";
    public static final String WORKSHOP_ORDER_LINE_DESCRIPTION_REQUIRED = "Work description is required";
    public static final String WORKSHOP_ORDER_LINE_PRIORITY_REQUIRED = "Priority is required";

    // Workshop executions.
    public static final String EXECUTION_MUST_BE_PAST_OR_PRESENT = "Start date must be in the past or present";
    public static final String EXECUTION_STATUS_IS_REQUIRED = "Status is required";


    // Workshop execution lines.
    public static final String EXECUTION_LINE_REQUIRED = "Line number is required";
    public static final String EXECUTION_LINE_DESCRIPTION_REQUIRED = "Description is required";
    public static final String EXECUTION_LINE_TYPE_REQUIRED = "Execution line type is required";
    public static final String EXECUTION_LINE_QUANTITY_REQUIRED = "Quantity is required";
    public static final String EXECUTION_LINE_QUANTITY_MUST_BE_GREATER_ZERO = "Quantity must be greater than zero";
    public static final String EXECUTION_LINE_STATUS_REQUIRED = "Execution line status is required";

}
