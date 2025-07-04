package com.inovation.vehiclesecurity.models;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String vehiclePlate;
    private String vehicleType;
    private String vehicleBrand;
    private String vehicleColor;

    public RegisterRequest(String name, String email, String password,
                           String vehiclePlate, String vehicleType,
                           String vehicleBrand, String vehicleColor) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.vehiclePlate = vehiclePlate;
        this.vehicleType = vehicleType;
        this.vehicleBrand = vehicleBrand;
        this.vehicleColor = vehicleColor;
    }

    // Tambahkan getter jika diperlukan
}
