package com.inovation.vehiclesecurity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private SwitchCompat switchSecurityMode, switchAlarm;
    private TextView textViewPlatNomor, textViewMerk, textViewWarna, textViewTipe;
    private ImageButton btnProfile, btnAlarmBottom;
    private Button btnRiwayat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Switch Buttons
        switchSecurityMode = findViewById(R.id.switchSecurityMode);
        switchAlarm = findViewById(R.id.switchAlarm);

        switchSecurityMode.setOnCheckedChangeListener((buttonView, isChecked) ->
                Toast.makeText(this, "Security Mode " + (isChecked ? "Aktif" : "Nonaktif"), Toast.LENGTH_SHORT).show());

        switchAlarm.setOnCheckedChangeListener((buttonView, isChecked) ->
                Toast.makeText(this, "Alarm " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show());

        // Kendaraan Info
        textViewPlatNomor = findViewById(R.id.textViewPlatNomor);
        textViewMerk = findViewById(R.id.textViewMerk);
        textViewWarna = findViewById(R.id.textViewWarna);
        textViewTipe = findViewById(R.id.textViewTipe);
        loadVehicleInfoFromIntent();

        // Tombol navigasi
        btnProfile = findViewById(R.id.btnProfile);
        btnAlarmBottom = findViewById(R.id.btnAlarmBottom);
        btnRiwayat = findViewById(R.id.btnRiwayat);

        // Default tampilkan HomeFragment jika belum ada fragment
        if (savedInstanceState == null) {
            loadFragment(new RiwayatFragment());
        }

        btnProfile.setOnClickListener(view -> loadFragment(new ProfileFragment()));
        btnAlarmBottom.setOnClickListener(view -> loadFragment(new AlarmFragment()));
        btnRiwayat.setOnClickListener(view -> loadFragment(new RiwayatFragment()));
    }

    private void loadVehicleInfoFromIntent() {
        String plat = getIntent().getStringExtra("plat");
        String merk = getIntent().getStringExtra("merk");
        String warna = getIntent().getStringExtra("warna");
        String tipe = getIntent().getStringExtra("tipe");

        textViewPlatNomor.setText("Plat Nomor: " + plat);
        textViewMerk.setText("Merk: " + merk);
        textViewWarna.setText("Warna: " + warna);
        textViewTipe.setText("Tipe: " + tipe);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); // efek transisi
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
