package com.example.sleepsound.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sleepsound.shared_preferences.MySharedPreferences;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class SettingFragmentViewModel extends ViewModel {
    private MySharedPreferences mySharedPreferences;

    private MutableLiveData<String> formattedReminderTime;
    private MutableLiveData<Boolean> liveTurnOn;

    public SettingFragmentViewModel(){
        formattedReminderTime = new MutableLiveData<>();
        liveTurnOn = new MutableLiveData<>();
    }

    @SuppressLint("DefaultLocale")
    public void updateFormattedReminderTime() {
        // Lấy giá trị từ SharedPreferences hoặc mặc định là 0
        int hour = mySharedPreferences.getIntValue("hourReminderSleep", 0);
        int minute = mySharedPreferences.getIntValue("minuteReminderSleep", 0);
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

    public void setTimeReminder(int hour, int minute, boolean isTurnedOn, MySharedPreferences mySharedPreferences){
        mySharedPreferences.putIntValue("hourReminderSleep", hour);
        mySharedPreferences.putIntValue("minuteReminderSleep", minute);
        mySharedPreferences.putBooleanValue("turnOnReminder", isTurnedOn);
        updateFormattedReminderTime();
    }

    public void startTimer(int hours, int minutes, Context context) {
        LocalDateTime timeNow = LocalDateTime.now();
        int hourNow = timeNow.getHour();
        int minuteNow = timeNow.getMinute();

        int millisTimeSet = (hours * 3600 + minutes * 60) * 1000;
        int millisTimeNow = (hourNow * 3600 + minuteNow * 60) * 1000;

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
                reminderSleepTime(context);
            }
        }.start();
    }

    public void reminderSleepTime(Context context){

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
