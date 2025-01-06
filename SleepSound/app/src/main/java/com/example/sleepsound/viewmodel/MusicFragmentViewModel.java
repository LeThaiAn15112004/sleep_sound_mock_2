package com.example.sleepsound.viewmodel;

import android.util.Log;
import android.view.View;

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
    private List<MusicSoundItem> recentlyPlayed = new ArrayList<>();
    private String selectedGenre = "All";

    private MutableLiveData<List<MusicSoundItem>> liveFullMusicItem = new MutableLiveData<>();
    private MutableLiveData<List<MusicSoundGroup>> liveFullMusicGroups = new MutableLiveData<>();
    private MutableLiveData<List<String>> liveGenres = new MutableLiveData<>();
    private MutableLiveData<List<MusicSoundItem>> liveRecentlyPlayed = new MutableLiveData<>();
    private MutableLiveData<String> liveSelectedGenre = new MutableLiveData<>();

    public MusicFragmentViewModel(){
        loadMusicGroups();
    }

    private void loadMusicGroups() {
        SoundApi api = RetrofitInstance.getApi();
        api.getMusicGroups().enqueue(new Callback<List<MusicSoundGroup>>() {
            @Override
            public void onResponse(Call<List<MusicSoundGroup>> call, Response<List<MusicSoundGroup>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullMusicGroups.addAll(response.body());
                    liveFullMusicGroups.setValue(fullMusicGroups); // Post the full list of groups
                    loadMusicData();
                } else {
                    logError(response);
                }
            }

            @Override
            public void onFailure(Call<List<MusicSoundGroup>> call, Throwable t) {
                Log.e("MusicFragmentViewModel", "Failed to load data", t);
                liveFullMusicGroups.setValue(new ArrayList<>());
            }
        });
    }

    public void filterMusicByGenre(String genre) {
        List<MusicSoundItem> filtered = new ArrayList<>();
        for (MusicSoundItem item : fullMusicItem) {
            if ("All".equals(genre) || item.getGroup().equals(genre)) {
                filtered.add(item);
            }
        }
        liveFullMusicItem.setValue(filtered);
        liveSelectedGenre.setValue(genre);
    }

    private void loadMusicData(){
        if (fullMusicGroups != null && !fullMusicGroups.isEmpty()) {
            fullMusicItem.clear();
            genres.clear();
            genres.add("All");
            liveSelectedGenre.setValue("All");
            // Use explicit type MusicSoundGroup for the loop
            for (MusicSoundGroup group : fullMusicGroups) {
                fullMusicItem.addAll(group.getItems()); // group.getItems() returns List<MusicSoundItem>
                genres.add(group.getGroup()); // group.getGroup() returns String
            }

            liveFullMusicItem.setValue(fullMusicItem);
            liveGenres.setValue(genres);
        }
    }

    private void logError(Response<List<MusicSoundGroup>> response) {
        try {
            if (response.errorBody() != null) {
                Log.e("MusicFragmentViewModel", "API Error: " + response.errorBody().string());
            }
        } catch (IOException e) {
            Log.e("MusicFragmentViewModel", "Error parsing error body", e);
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

    public List<MusicSoundItem> getRecentlyPlayed() {
        return recentlyPlayed;
    }

    public void setRecentlyPlayed(List<MusicSoundItem> recentlyPlayed) {
        this.recentlyPlayed = recentlyPlayed;
    }

    public MutableLiveData<List<MusicSoundItem>> getLiveRecentlyPlayed() {
        return liveRecentlyPlayed;
    }

    public void setLiveRecentlyPlayed(MutableLiveData<List<MusicSoundItem>> liveRecentlyPlayed) {
        this.liveRecentlyPlayed = liveRecentlyPlayed;
    }

    public String getSelectedGenre() {
        return selectedGenre;
    }

    public void setSelectedGenre(String selectedGenre) {
        this.selectedGenre = selectedGenre;
    }

    public MutableLiveData<String> getLiveSelectedGenre() {
        return liveSelectedGenre;
    }

    public void setLiveSelectedGenre(MutableLiveData<String> liveSelectedGenre) {
        this.liveSelectedGenre = liveSelectedGenre;
    }
}
