package com.acoes.fleetmanagement.shared.constants;

/**
 * Agrupa las rutas REST usadas por los controladores de la API.
 */
public final class EndpointConstants {

    // General.
    public static final String GET_BY_ID = "/{id}";
    public static final String PUT_BY_ID = "/{id}";
    public static final String PATCH_BY_ID = "/{id}";
    public static final String PATCH_OFF_BY_ID = "/{id}/deactivate";


    // Garage.
    public static final String GARAGES = "/api/garages";

    // Vehicle.
    public static final String VEHICLES = "/api/vehicles";
    public static final String PATCH_GARAGE_BY_ID = "/{id}/assign-garage";
    public static final String GET_VEHICLE_BY_PLATE = "/plate/{plateNumber}";
    public static final String GET_VEHICLE_BY_VIN = "/vin/{vin}";

    // Workshop orders.
    public static final String ORDERS = "/api/workshop-orders";
    public static final String GET_ORDER_BY_NUMBER = "/number/{orderNumber}";
    public static final String GET_ORDER_BY_VEHICLE = "/vehicle/{vehicleId}";
    public static final String GET_ORDER_BY_PLATE = "/vehicle-plate/{plateNumber}";
    public static final String GET_ORDER_BY_VIN = "/vehicle-vin/{vin}";
    public static final String GET_ORDER_DETAIL_BY_NUMBER = "/{orderNumber}/detail";
    public static final String CREATE_LINE_BY_ORDER = "/{orderNumber}/lines";

    // Workshop order lines.
    public static final String ORDER_LINES = "/api/workshop-order-lines";
    public static final String GET_LINES_BY_ORDER_ID = "/workshop-order/{workshopOrderId}";
    public static final String GET_LINES_BY_ORDER_NUMBER = "/workshop-order-number/{orderNumber}";
    public static final String PATCH_LINE_BY_ORDER_NUMBER = "/order/{orderNumber}/line/{lineNumber}";

    // Workshop executions.
    public static final String EXECUTIONS = "/api/workshop-executions";
    public static final String CREATE_EXEC_BY_ORDER = "/order/{orderNumber}";
    public static final String GET_EXEC_BY_NUMBER = "/{executionNumber}";
    public static final String GET_EXEC_BY_ORDER = "/order/{orderNumber}";
    public static final String GET_EXEC_BY_PLATE = "/vehicle-plate/{plateNumber}";
    public static final String GET_EXEC_BY_VIN = "/vehicle-vin/{vin}";
    public static final String PATCH_EXEC_STATUS_BY_NUMBER = "/{executionNumber}/status";
    public static final String PATCH_EXEC_OFF_BY_NUMBER = "/{executionNumber}/deactivate";

    // Workshop execution lines.
    public static final String EXEC_LINES = "/api/executions";
    public static final String CREATE_EXEC_LINE_BY_EXEC = "/{executionNumber}/lines";
    public static final String GET_EXEC_LINES_BY_EXEC = "/{executionNumber}/lines";
    public static final String PATCH_EXEC_LINE_BY_EXEC = "/{executionNumber}/lines/{lineNumber}";
    public static final String PATCH_EXEC_LINE_OFF_BY_EXEC = "/{executionNumber}/lines/{lineNumber}/deactivate";

}
