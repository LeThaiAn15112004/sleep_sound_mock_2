package com.example.sleepsound.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.sleepsound.API.JsonHelper;
import com.example.sleepsound.R;
import com.example.sleepsound.adapter.SoundPlayingAdapter;
import com.example.sleepsound.databinding.ActivityRelaxSoundBinding;
import com.example.sleepsound.databinding.DialogListAmbienceBottomBinding;
import com.example.sleepsound.databinding.DialogPlayingSoundListBinding;
import com.example.sleepsound.model.Mix;
import com.example.sleepsound.model.Sound;
import com.example.sleepsound.service.RelaxService;
import com.example.sleepsound.viewmodel.RelaxSoundActivityViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RelaxSoundActivity extends AppCompatActivity implements SoundPlayingAdapter.OnItemCancelClickListener, SoundPlayingAdapter.OnSeekBarChangeListener {
    private ActivityRelaxSoundBinding activityRelaxSoundBinding;
    private RelaxSoundActivityViewModel relaxSoundActivityViewModel;
    private SoundPlayingAdapter soundPlayingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityRelaxSoundBinding = ActivityRelaxSoundBinding.inflate(getLayoutInflater());
        setContentView(activityRelaxSoundBinding.getRoot());
        relaxSoundActivityViewModel = new ViewModelProvider(this).get(RelaxSoundActivityViewModel.class);
        initialize();
        clicking();
    }

    private void clicking() {
        activityRelaxSoundBinding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListSoundPlaying();
            }
        });

        activityRelaxSoundBinding.btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMix();
            }
        });

        activityRelaxSoundBinding.btnSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListAmbienceDialog();
            }
        });
    }

    private void pauseMix(){
        try {
            relaxSoundActivityViewModel.toggleMix(RelaxSoundActivity.this);
            Intent intent = new Intent(this, RelaxService.class);
            intent.setAction(RelaxService.PAUSE_SOUND_ACTION);
            startService(intent);
            this.startActivity(intent);
        }catch (Exception e) {
            Toast.makeText(this, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            System.err.println(e);
        }
    }

    private void showListSoundPlaying() {
        BottomSheetDialog bottomListPlayingDialog = new BottomSheetDialog(RelaxSoundActivity.this);
        DialogPlayingSoundListBinding dialogPlayingSoundListBinding = DialogPlayingSoundListBinding.inflate(getLayoutInflater());
        bottomListPlayingDialog.setContentView(dialogPlayingSoundListBinding.getRoot());
        LinearLayoutManager layoutManager = new LinearLayoutManager(RelaxSoundActivity.this);
        dialogPlayingSoundListBinding.ambiencePlayingRecycleView.setLayoutManager(layoutManager);
        if (soundPlayingAdapter == null) {
            soundPlayingAdapter = new SoundPlayingAdapter(RelaxSoundActivity.this, new ArrayList<>());
            soundPlayingAdapter.setOnItemCancelClickListener(this);
            soundPlayingAdapter.setOnSeekBarChangeListener(this);
        }
        dialogPlayingSoundListBinding.ambiencePlayingRecycleView.setAdapter(soundPlayingAdapter);

        relaxSoundActivityViewModel.getLivePlayingSoundList().observe(this, playingSounds -> {
            if (playingSounds != null && !playingSounds.isEmpty()) {
                soundPlayingAdapter.setSoundPlayingList(playingSounds);
                dialogPlayingSoundListBinding.ambiencePlayingRecycleView.setVisibility(View.VISIBLE);
                dialogPlayingSoundListBinding.noSoundTextView.setVisibility(View.GONE);
            } else {
                dialogPlayingSoundListBinding.ambiencePlayingRecycleView.setVisibility(View.GONE);
                dialogPlayingSoundListBinding.noSoundTextView.setVisibility(View.VISIBLE);
            }
        });

        dialogPlayingSoundListBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomListPlayingDialog.dismiss();
            }
        });
        bottomListPlayingDialog.show();
    }

    private void showListAmbienceDialog() {
        BottomSheetDialog bottomListAmbienceDialog = new BottomSheetDialog(RelaxSoundActivity.this);
        DialogListAmbienceBottomBinding dialogListAmbienceBottomBinding = DialogListAmbienceBottomBinding.inflate(getLayoutInflater());
        bottomListAmbienceDialog.setContentView(dialogListAmbienceBottomBinding.getRoot());

        dialogListAmbienceBottomBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomListAmbienceDialog.dismiss();
            }
        });


        bottomListAmbienceDialog.show();

    }

    private void initialize() {
        try {
            String name = getIntent().getStringExtra("name");
            String imageUrl = "file:///android_asset/images/" + getIntent().getStringExtra("coverThumbnail") + ".webp";
            ArrayList<Mix.Sound> sounds = getIntent().getParcelableArrayListExtra("sounds");
            List<Sound> soundList = JsonHelper.loadSounds(this);
            if (sounds != null) {
                for (Mix.Sound sound : sounds) {
                    // Xử lý từng `Sound`
                    System.out.println("Sound ID: " + sound.getId() + ", Volume: " + sound.getVolume());
                    Sound foundedSound = relaxSoundActivityViewModel.findSoundById(soundList, sound.getId());
                    playSound(foundedSound);
                }
            }

            // Cập nhật dữ liệu vào ViewModel
            relaxSoundActivityViewModel.getSoundTitle().setValue(name);
            relaxSoundActivityViewModel.getSoundImageUrl().setValue(imageUrl);

            // Quan sát dữ liệu từ ViewModel và cập nhật UI
            relaxSoundActivityViewModel.getSoundTitle().observe(this, title -> {
                activityRelaxSoundBinding.tvSoundTitle.setText(title);
            });

            relaxSoundActivityViewModel.getIsPlaying().observe(this, isPlaying -> {
                if (isPlaying != null && isPlaying) {
                    activityRelaxSoundBinding.ivPlayIcon.setImageResource(R.drawable.ic_pause); // Đặt biểu tượng nút tạm dừng
                } else {
                    activityRelaxSoundBinding.ivPlayIcon.setImageResource(R.drawable.ic_play); // Đặt biểu tượng nút phát
                }
            });

            relaxSoundActivityViewModel.getEndTime().observe(this, endTime ->{
                activityRelaxSoundBinding.tvEndTime.setText(endTime);
            });

            relaxSoundActivityViewModel.getSoundImageUrl().observe(this, url -> {
                Glide.with(activityRelaxSoundBinding.getRoot().getContext())
                        .load(url)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(activityRelaxSoundBinding.ivSoundImage);
            });

            relaxSoundActivityViewModel.getCurrentTime().observe(this, time ->{
                activityRelaxSoundBinding.seekBar.setProgress(0);
            });

//            relaxSoundActivityViewModel.getIsPlaying().observe(this, isPlaying -> {
//                activityRelaxSoundBinding.ivPlayIcon.setImageResource(isPlaying != null && isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
//            });
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            System.err.println(e);
        }
    }

    public void playSound(Sound sound) {
        try {
            relaxSoundActivityViewModel.initializeMediaPlayer(sound.getFileName(), sound.getId(), this);
            relaxSoundActivityViewModel.addSoundToPlayingList(sound.getId(), this);
            int resId = relaxSoundActivityViewModel.getResIdSong(sound.getFileName(), this);
            Intent intent = new Intent(this, RelaxService.class);
            intent.setAction(RelaxService.PLAY_SOUND_ACTION);
            intent.putExtra("resId", resId); // Đảm bảo file audio có trong thư mục res/raw
            startService(intent);
            this.startActivity(intent);
            updateSeekBar();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            System.err.println(e);
        }
    }

    @Override
    public void onCancelClick(int position) {
        try {

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            System.err.println(e);
        }
    }

    @Override
    public void onSeekBarChange(int position) {
        try {

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            System.err.println(e);
        }
    }

    private void updateSeekBar() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<Integer, MediaPlayer> entry : relaxSoundActivityViewModel.getMediaPlayerMap().entrySet()) {
                    MediaPlayer mediaPlayer = entry.getValue();
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        int duration = mediaPlayer.getDuration();
                        activityRelaxSoundBinding.seekBar.setMax(duration);
                        activityRelaxSoundBinding.seekBar.setProgress(currentPosition);
                    }
                }
                handler.postDelayed(this, 1000); // Cập nhật mỗi giây
            }
        };
        handler.post(runnable);
    }

}
