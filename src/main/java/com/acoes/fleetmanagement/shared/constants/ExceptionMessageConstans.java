package com.acoes.fleetmanagement.shared.constants;

public class ExceptionMessageConstans {

    //GARAGE
    public static final String GARAGE_NOT_FOUND_BY_ID = "Garage not found with id: ";

    //VEHICLE
    public static final String VEHICLE_NOT_FOUND_BY_ID = "Vehicle not found with id: ";
    public static final String PLATE_NUMBER_ALREADY_EXIST = "Plate number already exists: ";
    public static final String VIN_ALREADY_EXIST = "VIN already exists: ";
    public static final String VEHICLE_NOT_FOUND_BY_PLATE_NUMBER = "Vehicle not found with plate number: ";
    public static final String VEHICLE_NOT_FOUND_BY_VIN = "Vehicle not found with VIN: ";

    //WORKSHOP_ORDER
    public static final String WORKSHOP_NOT_FOUND_BY_ID = "Workshop not found with id: ";
    public static final String WORKSHOP_NOT_FOUND_BY_NUMBER = "Workshop order not found with number: ";

    //WORKSHOP_ORDER_LINE
    public static final String WORKSHOP_ORDER_LINE_NOT_FOUND_BY_ID = "Workshop order line not found with id: ";
    public static final String WORKSHOP_ORDER_LINE_NUMBER_ALREADY_EXISTS = "Line number already exists " +
            "for this workshop order";
    public static final String WORKSHOP_ORDER_LINE_NOT_FOUND_BY_ORDER_NUMBER = "Workshop order line not " +
            "found for order number: ";
    public static final String LINE_NUMBER = " and line number: ";
}
