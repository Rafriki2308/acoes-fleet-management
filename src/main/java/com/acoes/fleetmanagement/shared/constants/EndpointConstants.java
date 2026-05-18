package com.acoes.fleetmanagement.shared.constants;

public final class EndpointConstants {

    //General
    public final static String BY_ID = "/{id}";
    public final static String DEACTIVATE_BY_ID = "/{id}/deactivate";


    //Garage
    public final static String MAIN_GARAGE_ENDPOINT = "/api/garages";

    //Vehicle
    public final static String MAIN_VEHICLE_ENDPOINT = "/api/vehicles";
    public final static String ASSIGN_GARAGE_ENDPOINT = "/{id}/assign-garage";
    public final static String FIND_VEHICLE_BY_PLATE = "/plate/{plateNumber}";
    public final static String FIND_VEHICLE_BY_VIN = "/vin/{vin}";

    //WORKSHOP_ORDER
    public final static String MAIN_WORKSHOP_ORDER_ENDPOINT = "/api/workshop-orders";
    public final static String FIND_WORKSHOP_ORDER_BY_NUMBER = "/number/{orderNumber}";
    public final static String FIND_WORKSHOP_ORDER_BY_VEHICLE_ID = "/vehicle/{vehicleId}";
    public final static String FIND_WORKSHOP_ORDER_BY_PLATE_NUMBER = "/vehicle-plate/{plateNumber}";
    public final static String FIND_WORKSHOP_ORDER_BY_VIN = "/vehicle-vin/{vin}";
    public final static String FIND_WORKSHOP_ORDER_DETAIL_BY_ORDERNUMBER = "/{orderNumber}/detail";

    //WORKSHOP_ORDER_LINE
    public final static String MAIN_WORKSHOP_ORDER_LINE = "/api/workshop-order-lines";
    public final static String FIND_LINES_BY_WORKSHOP_ORDER_ID = "/workshop-order/{workshopOrderId}";
    public final static String FIND_LINES_BY_WORKSHOP_ORDER_NUMBER = "/workshop-order-number/{orderNumber}";
    public final static String PATCH_LINE_BY_WORKSHOP_ORDER_NUMBER = "/order/{orderNumber}/line/{lineNumber}";

}
