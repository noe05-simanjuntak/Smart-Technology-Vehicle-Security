package com.inovation.vehiclesecurity.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.inovation.vehiclesecurity.R;

public class AlarmPlayer {
    private static MediaPlayer mediaPlayer;
    private static int currentAlarmResId = R.raw.alarm_burglar; // Nada default

    public static void setAlarmResId(int resId) {
        currentAlarmResId = resId;
    }

    public static void play(Context context) {
        stop(); // Hindari overlap audio

        try {
            mediaPlayer = MediaPlayer.create(context, currentAlarmResId);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mediaPlayer = null;
            }
        }
    }

    public static int getCurrentAlarmResId() {
        return currentAlarmResId;
    }
}
