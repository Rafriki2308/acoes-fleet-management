package com.acoes.fleetmanagement.shared.constants;

/**
 * Agrupa mensajes reutilizables para excepciones de dominio.
 */

public class ExceptionMessageConstans {

    // Garages.
    public static final String GARAGE_NOT_FOUND_BY_ID = "Garage not found with id: ";

    // Vehicles.
    public static final String VEHICLE_NOT_FOUND_BY_ID = "Vehicle not found with id: ";
    public static final String PLATE_NUMBER_ALREADY_EXIST = "Plate number already exists: ";
    public static final String VIN_ALREADY_EXIST = "VIN already exists: ";
    public static final String VEHICLE_NOT_FOUND_BY_PLATE_NUMBER = "Vehicle not found with plate number: ";
    public static final String VEHICLE_NOT_FOUND_BY_VIN = "Vehicle not found with VIN: ";

    // Workshop orders.
    public static final String WORKSHOP_NOT_FOUND_BY_ID = "Workshop not found with id: ";
    public static final String WORKSHOP_NOT_FOUND_BY_NUMBER = "Workshop order not found with number: ";

    // Workshop order lines.
    public static final String WORKSHOP_ORDER_LINE_NOT_FOUND_BY_ID = "Workshop order line not found with id: ";
    public static final String WORKSHOP_ORDER_LINE_NUMBER_ALREADY_EXISTS = "Line number already exists " +
            "for this workshop order";
    public static final String WORKSHOP_ORDER_LINE_NOT_FOUND_BY_ORDER_NUMBER = "Workshop order line not " +
            "found for order number: ";
    public static final String LINE_NUMBER = " and line number: ";

    // Workshop executions.
    public static final String EXECUTION_NOT_FOUND = "Workshop execution not found: ";
    public static final String ORDER_NOT_FOUND = "Workshop order not found: ";
    public static final String EXECUTION_ALREADY_EXISTS_FOR_ORDER = "Workshop execution already exists for order: ";

    // Workshop execution lines.
    public static final String EXECUTION_LINE_NOT_FOUND = "Execution line not found for execution: ";
    public static final String LINE_ALREADY_EXISTS = "Line number already exists for this execution";
}
