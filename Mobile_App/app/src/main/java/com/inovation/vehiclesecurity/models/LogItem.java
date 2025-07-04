package com.inovation.vehiclesecurity.models;

import com.google.gson.annotations.SerializedName;

public class LogItem {

    @SerializedName("device")
    private String device;

    @SerializedName("alert")
    private String alert;

    @SerializedName("value")
    private String value;

    @SerializedName("created_at")
    private String createdAt;

    // Constructor kosong dibutuhkan oleh GSON
    public LogItem() {}

    // Getter dengan fallback default jika null
    public String getDevice() {
        return device != null ? device : "";
    }

    public String getAlert() {
        return alert != null ? alert : "";
    }

    public String getValue() {
        return value != null ? value : "";
    }

    public String getCreatedAt() {
        return createdAt != null ? createdAt : "";
    }
}
