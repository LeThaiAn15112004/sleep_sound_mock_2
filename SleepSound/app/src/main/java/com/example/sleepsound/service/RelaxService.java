package com.example.sleepsound.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RelaxService extends Service {
    private Handler handler;

    private static final String RELAX_CHANNEL_ID = "RelaxServiceChannel";
    public static final String PLAY_SOUND_ACTION = "PLAY_SOUND_ACTION";
    public static final String STOP_SOUND_ACTION = "STOP_SOUND_ACTION";
    public static final String PAUSE_SOUND_ACTION = "PAUSE_SOUND_ACTION";
    public static final String STOP_ALL_SOUNDS_ACTION = "STOP_ALL_SOUNDS_ACTION";
    public static final String UPDATE_VOLUME_ACTION = "UPDATE_VOLUME_ACTION";
    public Map<Integer, MediaPlayer> mediaPlayerMap;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        mediaPlayerMap = new HashMap<>();
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
                } else if (UPDATE_VOLUME_ACTION.equals(action)) {
                    int volume = intent.getIntExtra("volume", 50);
                    updateVolume(resId, volume);
                }
            } else if (STOP_ALL_SOUNDS_ACTION.equals(action)) {
                onDestroy();
            }
        }
        return START_STICKY;
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
        stopSelf();
    }

    private void playSound(int resId){
        if (!mediaPlayerMap.containsKey(resId)) {
            try {
                MediaPlayer mediaPlayer = MediaPlayer.create(this, resId);
                if (mediaPlayer != null) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mp -> {
                        stopSound(resId); // Tự động dừng khi phát xong
                    });
                    mediaPlayerMap.put(resId, mediaPlayer);
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

    private void pauseSound(int resId){
        MediaPlayer mediaPlayer = mediaPlayerMap.get(resId);
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    private void stopSound(int resId) {
        MediaPlayer mediaPlayer = mediaPlayerMap.get(resId);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayerMap.remove(resId);
        }
    }

    private void updateVolume(int resId, int volume) {
        MediaPlayer mediaPlayer = mediaPlayerMap.get(resId);
        if (mediaPlayer != null) {
            float volumeLevel = volume / 100f;
            mediaPlayer.setVolume(volumeLevel, volumeLevel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}