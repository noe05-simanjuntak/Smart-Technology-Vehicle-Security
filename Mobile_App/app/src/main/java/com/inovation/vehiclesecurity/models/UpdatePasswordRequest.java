package com.inovation.vehiclesecurity.models;

public class UpdatePasswordRequest {
    private String email;
    private String old_password;
    private String new_password;

    public UpdatePasswordRequest(String email, String old_password, String new_password) {
        this.email = email;
        this.old_password = old_password;
        this.new_password = new_password;
    }
}
