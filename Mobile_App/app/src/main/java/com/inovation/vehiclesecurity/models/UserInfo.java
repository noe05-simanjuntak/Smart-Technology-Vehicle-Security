package com.inovation.vehiclesecurity.models;

public class UserInfo {
    private String name;
    private String email;
    private String vehiclePlate;
    private String vehicleType;
    private String vehicleBrand;
    private String vehicleColor;

    // Getter
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getVehiclePlate() { return vehiclePlate; }
    public String getVehicleType() { return vehicleType; }
    public String getVehicleBrand() { return vehicleBrand; }
    public String getVehicleColor() { return vehicleColor; }

    // Setter
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public void setVehicleBrand(String vehicleBrand) { this.vehicleBrand = vehicleBrand; }
    public void setVehicleColor(String vehicleColor) { this.vehicleColor = vehicleColor; }
}
