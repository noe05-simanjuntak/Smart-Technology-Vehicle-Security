package com.inovation.vehiclesecurity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inovation.vehiclesecurity.models.LogItem;
import com.inovation.vehiclesecurity.network.ApiService;
import com.inovation.vehiclesecurity.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LogAdapter logAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs); // Pastikan layout ini ada

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchSensorData();
    }

    private void fetchSensorData() {
        ApiService apiService = RetrofitClient.getApiService();

        Call<List<LogItem>> call = apiService.getSensorData();
        call.enqueue(new Callback<List<LogItem>>() {
            @Override
            public void onResponse(Call<List<LogItem>> call, Response<List<LogItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    logAdapter = new LogAdapter(response.body());
                    recyclerView.setAdapter(logAdapter);
                } else {
                    Toast.makeText(LogActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LogItem>> call, Throwable t) {
                Log.e("API_ERROR", "Gagal mengambil data: " + t.getMessage(), t);
                Toast.makeText(LogActivity.this, "Terjadi kesalahan koneksi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
