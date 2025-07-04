package com.inovation.vehiclesecurity.session;

public class UserSession {
    private static UserSession instance;
    private String name;
    private String email;
    private String vehiclePlate;
    private String vehicleType;
    private String vehicleBrand;
    private String vehicleColor;
    private String accessToken;

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(String name, String email, String vehiclePlate, String vehicleType, String vehicleBrand, String vehicleColor, String accessToken) {
        this.name = name;
        this.email = email;
        this.vehiclePlate = vehiclePlate;
        this.vehicleType = vehicleType;
        this.vehicleBrand = vehicleBrand;
        this.vehicleColor = vehicleColor;
        this.accessToken = accessToken;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getVehiclePlate() { return vehiclePlate; }
    public String getVehicleType() { return vehicleType; }
    public String getVehicleBrand() { return vehicleBrand; }
    public String getVehicleColor() { return vehicleColor; }
    public String getAccessToken() { return accessToken; }

    public void clearSession() {
        name = null;
        email = null;
        vehiclePlate = null;
        vehicleType = null;
        vehicleBrand = null;
        vehicleColor = null;
        accessToken = null;
        instance = null;
    }

    public boolean isLoggedIn() {
        return accessToken != null || (email != null && vehiclePlate != null);
    }
}
