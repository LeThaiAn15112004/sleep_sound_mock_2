package com.example.sleepsound.API;


import com.example.sleepsound.model.MusicSoundGroup;
import com.example.sleepsound.model.Sound;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface SoundApi {
    @GET("ios/sleep_music.json")
    Call<List<Sound>> getSounds();

    @GET("ios/sleep_music.json")
    Call<List<MusicSoundGroup>> getMusicGroups();
}
