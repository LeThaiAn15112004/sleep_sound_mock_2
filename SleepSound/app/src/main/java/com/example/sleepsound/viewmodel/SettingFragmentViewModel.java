package com.example.sleepsound.viewmodel;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sleepsound.service.AlarmReceiver;
import com.example.sleepsound.shared_preferences.MySharedPreferences;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class SettingFragmentViewModel extends ViewModel {
    private MySharedPreferences mySharedPreferences;

    private MutableLiveData<String> formattedReminderTime;
    private MutableLiveData<Boolean> liveTurnOn;

    public SettingFragmentViewModel() {
        formattedReminderTime = new MutableLiveData<>();
        liveTurnOn = new MutableLiveData<>();
    }

    @SuppressLint("DefaultLocale")
    public void updateFormattedReminderTime() {
        // Lấy giá trị từ SharedPreferences hoặc mặc định là 0
        int hour = mySharedPreferences.getIntValue("hourReminderSleep", 0);
        int minute = mySharedPreferences.getIntValue("minuteReminderSleep", 0);
        int second = mySharedPreferences.getIntValue("secondReminderSleep", 0);
        boolean isTurnedOn = mySharedPreferences.getBooleanValue("turnOnReminder", false);

        // Định dạng thời gian
        String formatted = String.format("%02d:%02d", hour, minute);

        // Cập nhật giá trị
        formattedReminderTime.setValue(formatted);
        liveTurnOn.setValue(isTurnedOn);
    }

    @SuppressLint("DefaultLocale")
    public void setupNumberPicker(NumberPicker numberPicker, int maxValue) {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(maxValue);
        numberPicker.setFormatter(i -> String.format("%02d", i));
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            System.out.println("NumberPicker updated: " + newVal);
        });
        setNumberPickerTextColor(numberPicker);
    }

    private void setNumberPickerTextColor(NumberPicker numberPicker) {
        try {
            // Duyệt qua tất cả các field của NumberPicker
            Field[] fields = NumberPicker.class.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("mSelectorWheelPaint")) {
                    field.setAccessible(true);
                    Paint paint = (Paint) field.get(numberPicker);
                    assert paint != null;
                    paint.setColor(Color.WHITE);
                    numberPicker.invalidate();
                }
            }

            // Duyệt qua các TextView con bên trong NumberPicker
            for (int i = 0; i < numberPicker.getChildCount(); i++) {
                View child = numberPicker.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setTextColor(Color.WHITE);
                    child.invalidate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTimeReminder(int hour, int minute, int second, boolean isTurnedOn, MySharedPreferences mySharedPreferences) {
        mySharedPreferences.putIntValue("hourReminderSleep", hour);
        mySharedPreferences.putIntValue("minuteReminderSleep", minute);
        mySharedPreferences.putIntValue("secondReminderSleep", second);
        mySharedPreferences.putBooleanValue("turnOnReminder", isTurnedOn);
        updateFormattedReminderTime();
    }

    public void startTimer(int hours, int minutes, int seconds, Context context) {
        LocalDateTime timeNow = LocalDateTime.now();
        int hourNow = timeNow.getHour();
        int minuteNow = timeNow.getMinute();
        int secondNow = timeNow.getSecond();

        int millisTimeSet = (hours * 3600 + minutes * 60 + seconds) * 1000;
        int millisTimeNow = (hourNow * 3600 + minuteNow * 60 + secondNow) * 1000;

        int totalMillis = millisTimeSet - millisTimeNow;
        if (totalMillis < 0) {
            totalMillis += 24 * 3600 * 1000;
        }

        new CountDownTimer(totalMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mySharedPreferences.putBooleanValue("turnOnReminder", false);
                liveTurnOn.postValue(false);
            }
        }.start();
    }

    @SuppressLint("ScheduleExactAlarm")
    public void reminderSleepTime(int hour, int minute, int second, boolean isTurnedOn, Context context) {
        if (isTurnedOn) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction(AlarmReceiver.REMINDER_SLEEPING_TIME);
            intent.putExtra("hour", hour);
            intent.putExtra("minute", minute);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            // Tính thời gian cho báo thức
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime reminderTime = now
                    .withHour(hour)
                    .withMinute(minute)
                    .withSecond(second)
                    .withNano(0);
            if (reminderTime.isBefore(now)) {
                // Nếu giờ đặt trước hiện tại, chuyển sang ngày hôm sau
                reminderTime = reminderTime.plusDays(1);
            }
            long triggerTimeMillis = reminderTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();

            // Đặt báo thức
            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTimeMillis,
                        pendingIntent
                );
            }
            startTimer(hour, minute, second, context);
        } else {
            liveTurnOn.postValue(false);
        }
    }

    public Intent getShareAppIntent(Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String appPackageName = context.getPackageName();
        String shareMessage = "Check out this amazing app: https://play.google.com/store/apps/details?id=" + appPackageName;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        return shareIntent;
    }


    public MySharedPreferences getMySharedPreferences() {
        return mySharedPreferences;
    }

    public void setMySharedPreferences(MySharedPreferences mySharedPreferences) {
        this.mySharedPreferences = mySharedPreferences;
    }

    public MutableLiveData<String> getFormattedReminderTime() {
        return formattedReminderTime;
    }

    public void setFormattedReminderTime(MutableLiveData<String> formattedReminderTime) {
        this.formattedReminderTime = formattedReminderTime;
    }

    public MutableLiveData<Boolean> getLiveTurnOn() {
        return liveTurnOn;
    }

    public void setLiveTurnOn(MutableLiveData<Boolean> liveTurnOn) {
        this.liveTurnOn = liveTurnOn;
    }
}
