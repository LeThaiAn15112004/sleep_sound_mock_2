package com.example.sleepsound.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.sleepsound.R;
import com.example.sleepsound.databinding.ActivityRelaxSoundBinding;
import com.example.sleepsound.viewmodel.RelaxSoundActivityViewModel;

public class RelaxSoundActivity extends AppCompatActivity {
    private ActivityRelaxSoundBinding activityRelaxSoundBinding;
    private RelaxSoundActivityViewModel relaxSoundActivityViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityRelaxSoundBinding = ActivityRelaxSoundBinding.inflate(getLayoutInflater());
        setContentView(activityRelaxSoundBinding.getRoot());
        relaxSoundActivityViewModel = new ViewModelProvider(this).get(RelaxSoundActivityViewModel.class);
        initialize();
    }

    private void initialize() {
        String name = getIntent().getStringExtra("name");
        String imageUrl = "file:///android_asset/images/" + getIntent().getStringExtra("coverThumbnail") + ".webp";

        activityRelaxSoundBinding.tvSoundTitle.setText(name);
        Glide.with(activityRelaxSoundBinding.getRoot().getContext()).
                load(imageUrl).placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(activityRelaxSoundBinding.ivSoundImage);
    }
}
