package com.example.sleepsound.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sleepsound.API.RetrofitInstance;
import com.example.sleepsound.API.SoundApi;
import com.example.sleepsound.model.MusicSoundGroup;
import com.example.sleepsound.model.MusicSoundItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicFragmentViewModel extends ViewModel {
    private List<MusicSoundItem> fullMusicItem = new ArrayList<>();
    private List<MusicSoundGroup> fullMusicGroups = new ArrayList<>();
    private List<String> genres = new ArrayList<>();

    private MutableLiveData<List<MusicSoundItem>> liveFullMusicItem = new MutableLiveData<>();
    private MutableLiveData<List<MusicSoundGroup>> liveFullMusicGroups = new MutableLiveData<>();
    private MutableLiveData<List<String>> liveGenres = new MutableLiveData<>();

    private void loadMusicGroups() {
        SoundApi api = RetrofitInstance.getApi();
        api.getMusicGroups().enqueue(new Callback<List<MusicSoundGroup>>() {
            @Override
            public void onResponse(Call<List<MusicSoundGroup>> call, Response<List<MusicSoundGroup>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullMusicGroups.addAll(response.body());
                    liveFullMusicGroups.setValue(fullMusicGroups); // Post the full list of groups
                } else {
                    logError(response);
                }
            }

            @Override
            public void onFailure(Call<List<MusicSoundGroup>> call, Throwable t) {
                Log.e("MusicViewModel", "Failed to load data", t);
            }
        });
    }

    private void logError(Response<List<MusicSoundGroup>> response) {
        try {
            if (response.errorBody() != null) {
                Log.e("MusicViewModel", "API Error: " + response.errorBody().string());
            }
        } catch (IOException e) {
            Log.e("MusicViewModel", "Error parsing error body", e);
        }
    }

    public List<MusicSoundItem> getFullMusicItem() {
        return fullMusicItem;
    }

    public void setFullMusicItem(List<MusicSoundItem> fullMusicItem) {
        this.fullMusicItem = fullMusicItem;
    }

    public List<MusicSoundGroup> getFullMusicGroups() {
        return fullMusicGroups;
    }

    public void setFullMusicGroups(List<MusicSoundGroup> fullMusicGroups) {
        this.fullMusicGroups = fullMusicGroups;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public MutableLiveData<List<MusicSoundItem>> getLiveFullMusicItem() {
        return liveFullMusicItem;
    }

    public void setLiveFullMusicItem(MutableLiveData<List<MusicSoundItem>> liveFullMusicItem) {
        this.liveFullMusicItem = liveFullMusicItem;
    }

    public MutableLiveData<List<MusicSoundGroup>> getLiveFullMusicGroups() {
        return liveFullMusicGroups;
    }

    public void setLiveFullMusicGroups(MutableLiveData<List<MusicSoundGroup>> liveFullMusicGroups) {
        this.liveFullMusicGroups = liveFullMusicGroups;
    }

    public MutableLiveData<List<String>> getLiveGenres() {
        return liveGenres;
    }

    public void setLiveGenres(MutableLiveData<List<String>> liveGenres) {
        this.liveGenres = liveGenres;
    }
}
