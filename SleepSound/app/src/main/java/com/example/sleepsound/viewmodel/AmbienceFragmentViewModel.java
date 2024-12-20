package com.example.sleepsound.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sleepsound.R;
import com.example.sleepsound.model.Sound;
import com.example.sleepsound.service.AmbienceService;
import com.example.sleepsound.shared_preferences.MySharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AmbienceFragmentViewModel extends ViewModel {
    private List<Sound> sounds;
    private List<Sound> playingSounds;

    private MutableLiveData<List<Sound>> liveSoundList;
    private MutableLiveData<List<Sound>> livePlayingSoundList;
    private MutableLiveData<Integer> liveNumberPlaying;
    private MutableLiveData<Boolean> liveHavingPlayingSound;
    private MutableLiveData<String> formattedStopTime;

    private MySharedPreferences mySharedPreferences;

    public AmbienceFragmentViewModel() {
        sounds = new ArrayList<>();
        playingSounds = new ArrayList<>();
        this.liveSoundList = new MutableLiveData<>();
        this.livePlayingSoundList = new MutableLiveData<>();
        this.liveNumberPlaying = new MutableLiveData<>();
        this.liveHavingPlayingSound = new MutableLiveData<>();
        this.formattedStopTime = new MutableLiveData<>();
        loadSounds();
        getPlayingSounds();
    }

    @SuppressLint("DefaultLocale")
    public void updateFormattedStopTime() {
        // Lấy giá trị từ SharedPreferences hoặc mặc định là 0
        int hour = mySharedPreferences.getIntValue("hourStopSound", 0);
        int minute = mySharedPreferences.getIntValue("minuteStopSound", 0);
        int second = mySharedPreferences.getIntValue("secondStopSound", 0);

        // Định dạng thời gian
        String formatted = String.format("%02d:%02d:%02d", hour, minute, second);

        // Cập nhật giá trị
        formattedStopTime.setValue(formatted);
    }


    private void loadSounds() {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Sound>>() {
        }.getType();
        try (InputStreamReader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("assets/sounds.json"))) {
            sounds = gson.fromJson(reader, listType);
            liveSoundList.setValue(sounds);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public Intent createPlaySoundIntent(int resId) {
        Intent intent = new Intent();
        intent.setAction(AmbienceService.PLAY_SOUND_ACTION);
        intent.putExtra("resId", resId);
        return intent;
    }

    public Intent createChangeVolumeSoundIntent(int resId, int volume) {
        Intent intent = new Intent();
        intent.setAction(AmbienceService.UPDATE_VOLUME_ACTION);
        intent.putExtra("resId", resId);
        intent.putExtra("volume", volume);
        return intent;
    }

    public void toggleSound(Sound sound, Context context) {
        if (sound.isPlaying()) {
            Toast.makeText(context, "Stopping: " + sound.getName(), Toast.LENGTH_SHORT).show();
            sound.setPlaying(false);
        } else {
            Toast.makeText(context, "Playing: " + sound.getName(), Toast.LENGTH_SHORT).show();
            sound.setPlaying(true);
        }
        updatePlayingStatus();
        liveSoundList.setValue(new ArrayList<>(sounds));
        getPlayingSounds();
    }

    private void updatePlayingStatus() {
        if (liveSoundList.getValue() != null) {
            long count = liveSoundList.getValue().stream().filter(Sound::isPlaying).count();
            liveNumberPlaying.setValue((int) count);
            liveHavingPlayingSound.setValue(count > 0);
        }
    }

    public void stopAllSounds(Context context) {
        Intent stopAllSoundsIntent = new Intent(context, AmbienceService.class);
        stopAllSoundsIntent.setAction(AmbienceService.STOP_ALL_SOUNDS_ACTION);
        context.startService(stopAllSoundsIntent);
        for (Sound sound : sounds) {
            if (sound.isPlaying()) {
                sound.setPlaying(false);
            }
        }
        liveSoundList.setValue(sounds);
        liveNumberPlaying.setValue(0);
        liveHavingPlayingSound.setValue(false);
        getPlayingSounds();
    }

    public int getResIdSong(String fileName, Context context) {
        return context.getResources().getIdentifier(fileName, "raw", context.getPackageName());
    }

    public void startTimer(int hours, int minutes, int seconds, Context context) {
        LocalDateTime timeNow = LocalDateTime.now();
        int hourNow = timeNow.getHour();
        int minuteNow = timeNow.getMinute();
        int secondNow = timeNow.getSecond();
        System.out.println("Start timer with: hour=" + hourNow + ", minute=" + minuteNow + ", second=" + secondNow);

        // Tính thời gian hiện tại và thời gian đặt
        int millisTimeSet = (hours * 3600 + minutes * 60 + seconds) * 1000;
        int millisTimeNow = (hourNow * 3600 + minuteNow * 60 + secondNow) * 1000;

        // Kiểm tra thời gian đặt trước hay sau thời gian hiện tại
        int totalMillis = millisTimeSet - millisTimeNow;
        if (totalMillis < 0) {
            // Thời gian đặt là ngày hôm sau, cộng thêm 24 giờ
            totalMillis += 24 * 3600 * 1000;
        }
        System.out.println("totalMillis: " + totalMillis);

        // Đếm ngược
        new CountDownTimer(totalMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Hiển thị hoặc xử lý trong mỗi giây nếu cần
            }

            @Override
            public void onFinish() {
                // Khi kết thúc, dừng nhạc và hiển thị thông báo
                stopAllSounds(context);
                Toast.makeText(context, "Stop all sounds", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }


    public void setTimeStopSound(int hour, int minute, int second, MySharedPreferences mySharedPreferences) {
        mySharedPreferences.putIntValue("hourStopSound", hour);
        mySharedPreferences.putIntValue("minuteStopSound", minute);
        mySharedPreferences.putIntValue("secondStopSound", second);
        updateFormattedStopTime();
    }

    public void getPlayingSounds() {
        playingSounds.clear();
        for (Sound sound : sounds) {
            if (sound.isPlaying()) { // Giả sử có cờ "isPlaying" trong model Sound
                playingSounds.add(sound);
                System.out.println(sound);
            }
        }
        livePlayingSoundList.setValue(playingSounds);
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

    public MutableLiveData<List<Sound>> getLiveSoundList() {
        return liveSoundList;
    }

    public void setLiveSoundList(MutableLiveData<List<Sound>> liveSoundList) {
        this.liveSoundList = liveSoundList;
    }

    public MutableLiveData<Integer> getLiveNumberPlaying() {
        return liveNumberPlaying;
    }

    public void setLiveNumberPlaying(MutableLiveData<Integer> liveNumberPlaying) {
        this.liveNumberPlaying = liveNumberPlaying;
    }

    public MutableLiveData<Boolean> getLiveHavingPlayingSound() {
        return liveHavingPlayingSound;
    }

    public void setLiveHavingPlayingSound(MutableLiveData<Boolean> liveHavingPlayingSound) {
        this.liveHavingPlayingSound = liveHavingPlayingSound;
    }

    public MutableLiveData<List<Sound>> getLivePlayingSoundList() {
        return livePlayingSoundList;
    }

    public void setLivePlayingSoundList(MutableLiveData<List<Sound>> livePlayingSoundList) {
        this.livePlayingSoundList = livePlayingSoundList;
    }

    public MutableLiveData<String> getFormattedStopTime() {
        return formattedStopTime;
    }

    public void setFormattedStopTime(MutableLiveData<String> formattedStopTime) {
        this.formattedStopTime = formattedStopTime;
    }

    public MySharedPreferences getMySharedPreferences() {
        return mySharedPreferences;
    }

    public void setMySharedPreferences(MySharedPreferences mySharedPreferences) {
        this.mySharedPreferences = mySharedPreferences;
    }
}
