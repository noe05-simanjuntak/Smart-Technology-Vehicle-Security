package com.inovation.vehiclesecurity.network;

import com.inovation.vehiclesecurity.models.LoginRequest;
import com.inovation.vehiclesecurity.models.LoginResponse;
import com.inovation.vehiclesecurity.models.RegisterRequest;
import com.inovation.vehiclesecurity.models.UpdatePasswordRequest;
import com.inovation.vehiclesecurity.models.UpdateVehicleRequest;
import com.inovation.vehiclesecurity.models.LogItem;
import com.inovation.vehiclesecurity.models.FcmTokenRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/register")
    Call<Void> registerUser(@Body RegisterRequest request);

    @POST("/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @GET("/sensor-data")
    Call<List<LogItem>> getSensorData();

    @POST("/user/vehicle")
    Call<Void> updateVehicle(@Body UpdateVehicleRequest request);

    @POST("/user/password")
    Call<Void> changePassword(@Body UpdatePasswordRequest request);

    @POST("/fcm-token") // endpoint FCM dari FastAPI backend
    Call<Void> saveFcmToken(@Body FcmTokenRequest request);

    @POST("/fcm-token")
    Call<Void> sendFcmToken(@Body FcmTokenRequest request);// <- nama fungsi ini harus cocok dengan yang di LoginActivity
}
