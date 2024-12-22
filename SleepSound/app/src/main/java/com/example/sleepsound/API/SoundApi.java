package com.example.sleepsound.API;


import com.example.mook21.model.MusicSoundGroup;
import com.example.mook21.model.Sound;
import com.example.sleepsound.model.MusicSoundGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

import com.example.sleepsound.model.MusicSoundGroup;

public interface SoundApi {
    @GET("ios/sleep_music.json")
    Call<List<com.example.sleepsound.model.Sound>> getSounds();

    @GET("ios/sleep_music.json")
    Call<List<com.example.sleepsound.model.MusicSoundGroup>> getMusicGroups();
}
