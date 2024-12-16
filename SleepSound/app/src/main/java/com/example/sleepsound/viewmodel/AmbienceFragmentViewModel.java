package com.example.sleepsound.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sleepsound.R;
import com.example.sleepsound.model.Sound;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
            for (Sound sound : sounds){
                System.out.println(sound);
            }
            liveSoundList.setValue(sounds);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public MutableLiveData<List<Sound>> getLiveSoundList() {
        return liveSoundList;
    }

    public void setLiveSoundList(MutableLiveData<List<Sound>> liveSoundList) {
        this.liveSoundList = liveSoundList;
    }
}
