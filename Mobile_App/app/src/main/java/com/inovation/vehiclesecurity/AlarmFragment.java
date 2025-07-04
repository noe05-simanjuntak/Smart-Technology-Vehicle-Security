package com.inovation.vehiclesecurity;
import com.inovation.vehiclesecurity.utils.AlarmPlayer;

import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AlarmFragment extends Fragment {

    Button btnChangeMusic, btnRepeat, btnVibrate, btnDeleteAlarm;
    int currentAlarmResId = R.raw.alarm_burglar;

    String[] alarmNames = {
            "Burglar", "Modern", "One House", "Red Alert", "Reveille",
            "Ringtone", "Security", "Sound", "Tornado"
    };

    int[] alarmResIds = {
            R.raw.alarm_burglar, R.raw.alarm_modern, R.raw.alarm_one_house,
            R.raw.alarm_red_alert, R.raw.alarm_reveille, R.raw.alarm_ringtone,
            R.raw.alarm_security, R.raw.alarm_sound, R.raw.alarm_tornado
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        btnChangeMusic = view.findViewById(R.id.btnChangeMusic);
        btnRepeat = view.findViewById(R.id.btnRepeat);
        btnVibrate = view.findViewById(R.id.btnVibrate);
        btnDeleteAlarm = view.findViewById(R.id.btnDeleteAlarm);

        btnChangeMusic.setOnClickListener(v -> showAlarmSelectorDialog());
        btnRepeat.setOnClickListener(v -> AlarmPlayer.play(requireContext()));
        btnVibrate.setOnClickListener(v -> showToast("Mode getar diaktifkan..."));
        btnDeleteAlarm.setOnClickListener(v -> AlarmPlayer.stop());

        return view;
    }

    private void showAlarmSelectorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pilih Nada Alarm");

        builder.setItems(alarmNames, (dialog, which) -> {
            currentAlarmResId = alarmResIds[which];
            AlarmPlayer.setAlarmResId(currentAlarmResId);
            AlarmPlayer.play(requireContext());
            showToast("Musik alarm diganti ke: " + alarmNames[which]);
        });

        builder.show();
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AlarmPlayer.stop();
    }
}
