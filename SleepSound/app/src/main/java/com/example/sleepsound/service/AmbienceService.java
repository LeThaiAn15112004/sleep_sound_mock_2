package com.example.sleepsound.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AmbienceService extends Service {
    public static final String PLAY_SOUND_ACTION = "PLAY_SOUND_ACTION";
    public static final String STOP_SOUND_ACTION = "STOP_SOUND_ACTION";
    public static final String STOP_ALL_SOUNDS_ACTION = "STOP_ALL_SOUNDS_ACTION";
    public static final String UPDATE_VOLUME_ACTION = "UPDATE_VOLUME_ACTION";
    public Map<Integer, MediaPlayer> mediaPlayerMap; // Lưu trữ các MediaPlayer instance theo resId

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayerMap = new HashMap<>(); // Khởi tạo Map
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            int resId = intent.getIntExtra("resId", -1);
            if (resId != -1) {
                if (PLAY_SOUND_ACTION.equals(action)) {
                    playSound(resId);
                } else if (STOP_SOUND_ACTION.equals(action)) {
                    stopSound(resId);
                } else if (UPDATE_VOLUME_ACTION.equals(action)){
                    int volume = intent.getIntExtra("volume", 50);
                    updateVolume(resId, volume);
                }
            } else if (STOP_ALL_SOUNDS_ACTION.equals(action)) {
                onDestroy();
            }
        }
        return START_STICKY;
    }


    private void playSound(int resId) {
        if (!mediaPlayerMap.containsKey(resId)) {
            try {
                MediaPlayer mediaPlayer = MediaPlayer.create(this, resId);
                if (mediaPlayer != null) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mp -> {
                        stopSound(resId); // Tự động dừng khi phát xong
                    });
                    mediaPlayerMap.put(resId, mediaPlayer); // Thêm vào Map
                } else {
                    Log.e("AmbienceService", "Failed to create MediaPlayer for resId: " + resId);
                }
            } catch (Exception e) {
                Log.e("AmbienceService", "Error while playing sound", e);
            }
        } else {
            stopSound(resId);
        }
    }

    private void stopSound(int resId) {
        MediaPlayer mediaPlayer = mediaPlayerMap.get(resId);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayerMap.remove(resId); // Delete from map
        }
    }

    private void updateVolume(int resId, int volume) {
        MediaPlayer mediaPlayer = mediaPlayerMap.get(resId);
        if (mediaPlayer != null) {
            float volumeLevel = volume / 100f;
            mediaPlayer.setVolume(volumeLevel, volumeLevel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (MediaPlayer mediaPlayer : mediaPlayerMap.values()) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
        mediaPlayerMap.clear(); // Xóa toàn bộ Map
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not binding to the service
    }
}