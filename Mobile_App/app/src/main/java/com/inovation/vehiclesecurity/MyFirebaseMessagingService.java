package com.inovation.vehiclesecurity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.inovation.vehiclesecurity.models.FcmTokenRequest;
import com.inovation.vehiclesecurity.network.ApiService;
import com.inovation.vehiclesecurity.network.RetrofitClient;
import com.inovation.vehiclesecurity.session.UserSession;
import com.inovation.vehiclesecurity.utils.AlarmPlayer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    // Saat token FCM diperbarui (misalnya setelah instal ulang atau update app)
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "🎯 Token FCM baru: " + token);

        // Kirim token ke backend hanya jika user sudah login
        String email = UserSession.getInstance().getEmail();
        if (email != null && !email.isEmpty()) {
            sendFcmTokenToServer(email, token);
        } else {
            Log.w(TAG, "❌ Email user belum tersedia, token tidak dikirim");
        }
    }

    // Saat pesan FCM diterima
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Tampilkan notifikasi jika ada payload notifikasi
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            showNotification(title, body);
        }
        // Jika payload berisi data
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            showNotification(title, body);
        }

        // Jalankan alarm
        AlarmPlayer.play(getApplicationContext());
    }

    // Kirim token FCM ke backend
    private void sendFcmTokenToServer(String email, String token) {
        ApiService apiService = RetrofitClient.getApiService();
        FcmTokenRequest request = new FcmTokenRequest(email, token);

        apiService.sendFcmToken(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "✅ Token berhasil dikirim ke server");
                } else {
                    Log.e(TAG, "❌ Gagal kirim token. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "❌ Error kirim token: " + t.getMessage());
            }
        });
    }

    // Tampilkan notifikasi Android
    private void showNotification(String title, String message) {
        String channelId = "alarm_channel";
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        // Android 8+ butuh channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Alarm Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setSound(soundUri, null);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title != null ? title : "Notifikasi")
                .setContentText(message != null ? message : "")
                .setAutoCancel(true)
                .setSound(soundUri);

        if (notificationManager != null) {
            notificationManager.notify(0, builder.build());
        }
    }
}
