package com.example.sleepsound.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.sleepsound.R;
import com.example.sleepsound.activity.MainActivity;
import com.example.sleepsound.viewmodel.AmbienceFragmentViewModel;

import java.util.HashMap;
import java.util.Map;

public class AmbienceService extends Service {
    private static final String AMBIENCE_CHANNEL_ID = "AmbienceServiceChannel";
    public static final String PLAY_SOUND_ACTION = "PLAY_SOUND_ACTION";
    public static final String STOP_SOUND_ACTION = "STOP_SOUND_ACTION";
    public static final String STOP_ALL_SOUNDS_ACTION = "STOP_ALL_SOUNDS_ACTION";
    public static final String UPDATE_VOLUME_ACTION = "UPDATE_VOLUME_ACTION";
    public Map<Integer, MediaPlayer> mediaPlayerMap; // Lưu trữ các MediaPlayer instance theo resId

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayerMap = new HashMap<>(); // Khởi tạo Map
        createNotificationChannel();
        startForeground(1, buildNotification());
    }

    public void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    AMBIENCE_CHANNEL_ID,
                    "Ambience Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    @SuppressLint("RemoteViewLayout")
    private Notification buildNotification(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sound);

        Intent stopAllIntent = new Intent(this, AmbienceService.class);
        stopAllIntent.setAction(STOP_ALL_SOUNDS_ACTION);
        PendingIntent stopAllPendingIntent = PendingIntent.getService(this, 0, stopAllIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new Notification.Builder(this, AMBIENCE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_sound)
                .setContentTitle("Playing sound")
                .setLargeIcon(largeIconBitmap)
                .setContentText("Click to stop sound")
                .addAction(R.drawable.ic_pause, "Stop All Sound", stopAllPendingIntent)
                .build();
    }

    private void updateNotification(){
        Notification notification = buildNotification();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(1, notification); // Cập nhật notification với ID đã dùng cho startForeground
        }
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
                    mediaPlayerMap.put(resId, mediaPlayer);
                    updateNotification();
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}