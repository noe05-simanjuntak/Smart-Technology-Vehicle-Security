package com.inovation.vehiclesecurity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.inovation.vehiclesecurity.models.FcmTokenRequest;
import com.inovation.vehiclesecurity.models.LoginRequest;
import com.inovation.vehiclesecurity.models.LoginResponse;
import com.inovation.vehiclesecurity.network.ApiService;
import com.inovation.vehiclesecurity.network.RetrofitClient;
import com.inovation.vehiclesecurity.session.UserSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerLink;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);

        apiService = RetrofitClient.getApiService();

        loginButton.setOnClickListener(v -> loginUser());

        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(email, password);

        apiService.loginUser(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    UserSession.getInstance().setUser(
                            loginResponse.getUser().getName(),
                            loginResponse.getUser().getEmail(),
                            loginResponse.getUser().getVehiclePlate(),
                            loginResponse.getUser().getVehicleType(),
                            loginResponse.getUser().getVehicleBrand(),
                            loginResponse.getUser().getVehicleColor(),
                            null
                    );

                    // ✅ Kirim FCM token ke backend
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    String fcmToken = task.getResult();
                                    sendFcmTokenToBackend(loginResponse.getUser().getEmail(), fcmToken);
                                }
                            });

                    Toast.makeText(LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("plat", loginResponse.getUser().getVehiclePlate());
                    intent.putExtra("merk", loginResponse.getUser().getVehicleBrand());
                    intent.putExtra("warna", loginResponse.getUser().getVehicleColor());
                    intent.putExtra("tipe", loginResponse.getUser().getVehicleType());
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Login gagal. Email atau password salah.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Kesalahan koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendFcmTokenToBackend(String email, String token) {
        FcmTokenRequest tokenRequest = new FcmTokenRequest(email, token);
        apiService.saveFcmToken(tokenRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Gagal kirim token FCM", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error kirim FCM token: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
