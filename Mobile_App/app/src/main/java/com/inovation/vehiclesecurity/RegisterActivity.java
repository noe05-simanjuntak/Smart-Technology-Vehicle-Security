package com.inovation.vehiclesecurity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.inovation.vehiclesecurity.models.RegisterRequest;
import com.inovation.vehiclesecurity.network.ApiService;
import com.inovation.vehiclesecurity.network.RetrofitClient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, vehiclePlateEditText, vehicleColor;
    private Spinner vehicleTypeSpinner, brandSpinner;
    private Button registerButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.nameInput);
        emailEditText = findViewById(R.id.emailInputRegister);
        passwordEditText = findViewById(R.id.passwordInputRegister);
        vehiclePlateEditText = findViewById(R.id.vehiclePlateInputRegister);
        vehicleColor = findViewById(R.id.vehicleColor);
        vehicleTypeSpinner = findViewById(R.id.vehicleTypeSpinner);
        brandSpinner = findViewById(R.id.brandSpinner);
        registerButton = findViewById(R.id.registerButton);

        // Ganti ApiClient dengan RetrofitClient
        apiService = RetrofitClient.getApiService();

        ArrayAdapter<CharSequence> vehicleTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.vehicle_types, android.R.layout.simple_spinner_item);
        vehicleTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleTypeSpinner.setAdapter(vehicleTypeAdapter);

        ArrayAdapter<CharSequence> brandAdapter = ArrayAdapter.createFromResource(this,
                R.array.vehicle_brands, android.R.layout.simple_spinner_item);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(brandAdapter);

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String vehiclePlate = vehiclePlateEditText.getText().toString().trim();
        String vehicleType = vehicleTypeSpinner.getSelectedItem().toString();
        String vehicleBrand = brandSpinner.getSelectedItem().toString();
        String color = vehicleColor.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || vehiclePlate.isEmpty() || color.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data yang diperlukan", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (vehicleType.equals("Pilih Tipe") || vehicleBrand.equals("Pilih Merek")) {
            Toast.makeText(this, "Pilih tipe dan merek kendaraan", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(
                name, email, password, vehiclePlate, vehicleType, vehicleBrand, color
        );

        apiService.registerUser(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registrasi gagal: Email sudah digunakan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Kesalahan koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
