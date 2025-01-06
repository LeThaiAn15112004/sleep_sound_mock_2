package com.example.sleepsound.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sleepsound.databinding.ActivityMusicBinding;

public class MusicActivity extends AppCompatActivity {
    private ActivityMusicBinding activityMusicBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMusicBinding = ActivityMusicBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(activityMusicBinding.getRoot());
        initialize();
        clicking();
    }

    private void clicking() {
    }

    private void initialize() {
        
    }
}
