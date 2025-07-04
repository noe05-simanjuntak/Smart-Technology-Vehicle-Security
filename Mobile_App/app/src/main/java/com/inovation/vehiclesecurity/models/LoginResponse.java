package com.inovation.vehiclesecurity.models;

public class LoginResponse {
    private String message;
    private UserInfo user;

    public String getMessage() {
        return message;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
