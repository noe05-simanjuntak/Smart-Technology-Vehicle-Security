package com.inovation.vehiclesecurity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inovation.vehiclesecurity.models.LogItem;
import com.inovation.vehiclesecurity.network.ApiService;
import com.inovation.vehiclesecurity.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView txtEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_riwayat, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewLogs);
        progressBar = view.findViewById(R.id.progressBar);
        txtEmpty = view.findViewById(R.id.txtEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadLogs();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLogs(); // refresh saat kembali ke fragment
    }

    private void loadLogs() {
        progressBar.setVisibility(View.VISIBLE);
        txtEmpty.setVisibility(View.GONE);

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<LogItem>> call = apiService.getSensorData();

        call.enqueue(new Callback<List<LogItem>>() {
            @Override
            public void onResponse(Call<List<LogItem>> call, Response<List<LogItem>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<LogItem> fullList = response.body();

                    // ✅ Filter hanya data dengan createdAt yang valid
                    List<LogItem> filteredList = new ArrayList<>();
                    for (LogItem item : fullList) {
                        if (item != null && item.getCreatedAt() != null && !item.getCreatedAt().isEmpty()) {
                            filteredList.add(item);
                        }
                    }

                    if (!filteredList.isEmpty()) {
                        try {
                            LogAdapter adapter = new LogAdapter(filteredList);
                            recyclerView.setAdapter(adapter);
                            txtEmpty.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Kesalahan saat memuat data log.", Toast.LENGTH_SHORT).show();
                            txtEmpty.setVisibility(View.VISIBLE);
                        }
                    } else {
                        txtEmpty.setText("Tidak ada data sensor yang tersedia.");
                        txtEmpty.setVisibility(View.VISIBLE);
                    }
                } else {
                    txtEmpty.setText("Data tidak ditemukan.");
                    txtEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<LogItem>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Gagal memuat data: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
