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

}
