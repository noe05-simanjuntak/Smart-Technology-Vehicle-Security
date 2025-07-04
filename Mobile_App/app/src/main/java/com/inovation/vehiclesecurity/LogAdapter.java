package com.inovation.vehiclesecurity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inovation.vehiclesecurity.models.LogItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private List<LogItem> logList;

    public LogAdapter(List<LogItem> logList) {
        this.logList = logList;

        // Sort log berdasarkan waktu (terbaru di atas)
        Collections.sort(this.logList, new Comparator<LogItem>() {
            @Override
            public int compare(LogItem o1, LogItem o2) {
                Date date1 = parseDate(o1.getCreatedAt());
                Date date2 = parseDate(o2.getCreatedAt());
                return date2.compareTo(date1);
            }
        });

        // Batasi maksimum 15 data
        if (this.logList.size() > 15) {
            this.logList = this.logList.subList(0, 15);
        }
    }

    @NonNull
    @Override
    public LogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogAdapter.ViewHolder holder, int position) {
        LogItem log = logList.get(position);
        holder.txtDevice.setText("Device: " + safeText(log.getDevice()));
        holder.txtAlert.setText("Alert: " + safeText(log.getAlert()));
        holder.txtValue.setText("Value: " + safeText(log.getValue()));
        holder.txtTime.setText("Waktu: " + formatDate(log.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDevice, txtAlert, txtValue, txtTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDevice = itemView.findViewById(R.id.txtDevice);
            txtAlert = itemView.findViewById(R.id.txtAlert);
            txtValue = itemView.findViewById(R.id.txtValue);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }

    // 💡 Penanganan null untuk tampilan teks
    private String safeText(String value) {
        return value != null ? value : "-";
    }

    // 🕒 Format ISO 8601 → Waktu lokal
    private String formatDate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) {
            return "Tidak diketahui";
        }

        try {
            if (isoDate.length() > 23) {
                isoDate = isoDate.substring(0, 23) + "Z";
            }
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(isoDate);

            SimpleDateFormat localFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
            localFormat.setTimeZone(TimeZone.getDefault());
            return localFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Tidak diketahui";
        }
    }

    // 🧠 Parsing string ISO ke Date (dengan null-check)
    private Date parseDate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) {
            return new Date(0);
        }

        try {
            if (isoDate.length() > 23) {
                isoDate = isoDate.substring(0, 23) + "Z";
            }
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return isoFormat.parse(isoDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(0);
        }
    }
}
