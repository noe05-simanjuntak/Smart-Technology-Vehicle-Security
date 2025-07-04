package com.inovation.vehiclesecurity.models;

public class FcmTokenRequest {
    private String email;
    private String token;

    public FcmTokenRequest(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() { return email; }
    public String getToken() { return token; }
}
