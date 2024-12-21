package com.example.sleepsound.service;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.sleepsound.R;
import com.example.sleepsound.shared_preferences.MySharedPreferences;
import com.example.sleepsound.viewmodel.SettingFragmentViewModel;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String REMINDER_CHANNEL_ID = "ReminderServiceChannel";
    public static final String REMINDER_SLEEPING_TIME = "REMINDER_SLEEPING_TIME";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (AlarmReceiver.REMINDER_SLEEPING_TIME.equals(intent.getAction())) {
            int hour = intent.getIntExtra("hour", 0);
            int minute = intent.getIntExtra("minute", 0);

            // Thực hiện hành động khi báo thức kích hoạt
            // Ví dụ: Hiển thị thông báo
            showNotification(context, hour, minute);
        }
    }

    private void showNotification(Context context, int hour, int minute) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            String contentText = String.format("It's sleeping time: %02d:%02d", hour, minute);
            Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification_18_270129);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_sleeping) // Icon của thông báo
                    .setContentTitle("Remind sleeping time")
                    .setContentText(contentText)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setSound(soundUri)
                    .setAutoCancel(true);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        REMINDER_CHANNEL_ID,
                        "Remind sleeping time",
                        NotificationManager.IMPORTANCE_HIGH
                );
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();
                channel.setSound(soundUri, audioAttributes);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(1, builder.build());
        }
    }
}
