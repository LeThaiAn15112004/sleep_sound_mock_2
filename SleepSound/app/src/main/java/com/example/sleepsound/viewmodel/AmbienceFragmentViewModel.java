package com.example.sleepsound.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sleepsound.R;
import com.example.sleepsound.model.Sound;
import com.example.sleepsound.service.AmbienceService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AmbienceFragmentViewModel extends ViewModel {
    private List<Sound> soundList;

    private MutableLiveData<List<Sound>> liveSoundList;

    public AmbienceFragmentViewModel() {
        this.liveSoundList = new MutableLiveData<>();
        loadSounds();
    }

    private void loadSounds() {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Sound>>() {
        }.getType();
        try (InputStreamReader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("assets/sounds.json"))) {
            List<Sound> sounds = gson.fromJson(reader, listType);
            liveSoundList.setValue(sounds);
            for (Sound s : sounds){
                System.out.println(s);
            }
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

    public void toastForPlaying(Sound sound, Context context){
        if (sound.isPlaying()){
            Toast.makeText(context, "Stopping: " + sound.getName(), Toast.LENGTH_SHORT).show();
            sound.setPlaying(false);
        }else{
            Toast.makeText(context, "Playing: " + sound.getName(), Toast.LENGTH_SHORT).show();
            sound.setPlaying(true);
        }
    }

    public int getResIdSong(String fileName, Context context) {
        return context.getResources().getIdentifier(fileName, "raw", context.getPackageName());
    }

    public MutableLiveData<List<Sound>> getLiveSoundList() {
        return liveSoundList;
    }

    public void setLiveSoundList(MutableLiveData<List<Sound>> liveSoundList) {
        this.liveSoundList = liveSoundList;
    }

}
