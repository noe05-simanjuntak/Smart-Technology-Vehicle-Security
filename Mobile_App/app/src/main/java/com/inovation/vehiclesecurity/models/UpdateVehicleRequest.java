package com.inovation.vehiclesecurity.models;

public class UpdateVehicleRequest {
    private String email;
    private String vehicletype;
    private String vehiclebrand;
    private String vehiclecolor;

    public UpdateVehicleRequest(String email, String vehicle_type, String vehicle_brand, String vehicle_color) {
        this.email = email;
        this.vehicletype = vehicletype;
        this.vehiclebrand = vehiclebrand;
        this.vehiclecolor = vehiclecolor;
    }
}
