package com.example.sleepsound.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sleepsound.model.Sound;
import com.example.sleepsound.service.RelaxService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelaxSoundActivityViewModel extends ViewModel {
    private List<Sound> sounds = new ArrayList<>();
    private List<Sound> playingSounds = new ArrayList<>();
    public Map<Integer, MediaPlayer> mediaPlayerMap = new HashMap<>();

    private final MutableLiveData<String> soundTitle = new MutableLiveData<>();
    private final MutableLiveData<String> soundImageUrl = new MutableLiveData<>();
    private final MutableLiveData<String> endTime = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentTime = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalTime = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPlaying = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedCount = new MutableLiveData<>();
    private final MutableLiveData<Map<Integer, MediaPlayer>> liveMediaPlayerMap = new MutableLiveData<>();
    private MutableLiveData<List<Sound>> liveSoundList = new MutableLiveData<>();
    private MutableLiveData<List<Sound>> livePlayingSoundList = new MutableLiveData<>();

    public RelaxSoundActivityViewModel(){
        loadPlaying();
    }

    private void loadPlaying(){
        isPlaying.setValue(true);
        currentTime.setValue(0);
        totalTime.setValue(1200);
        endTime.setValue("20:00");
    }

    public void toggleMix(Context context) {
        Boolean playing = isPlaying.getValue();
        if (playing == null || !playing) {
            for (Sound sound : playingSounds) {
                MediaPlayer mediaPlayer = mediaPlayerMap.get(sound.getId());
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            }
            isPlaying.setValue(true);
        } else {
            for (Sound sound : playingSounds) {
                MediaPlayer mediaPlayer = mediaPlayerMap.get(sound.getId());
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
            isPlaying.setValue(false);
        }
    }


    public Sound findSoundById(List<Sound> sounds, int id) {
        if (sounds != null) {
            for (Sound sound : sounds) {
                if (sound.getId() == id) {
                    return sound;
                }
            }
        }
        return null;
    }

    public void initializeMediaPlayer(String fileName, int soundId, Context context) {
        int resId = getResIdSong(fileName, context);
        MediaPlayer mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayerMap.put(soundId, mediaPlayer);
        liveMediaPlayerMap.setValue(mediaPlayerMap);
    }

    public void addSoundToPlayingList(int soundId, Context context) {
        Sound sound = findSoundById(sounds, soundId);
        if (sound != null && !playingSounds.contains(sound)) {
            playingSounds.add(sound);
            initializeMediaPlayer(sound.getFileName(), sound.getId(), context);
            livePlayingSoundList.setValue(playingSounds);
        }
    }

    public void removeSoundFromPlayingList(int soundId) {
        Sound sound = findSoundById(playingSounds, soundId);
        if (sound != null) {
            playingSounds.remove(sound);
            MediaPlayer mediaPlayer = mediaPlayerMap.remove(soundId);
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            livePlayingSoundList.setValue(playingSounds);
        }
    }

    public void updateTime(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            currentTime.setValue(mediaPlayer.getCurrentPosition() / 1000); // giây
            endTime.setValue(formatTime(mediaPlayer.getDuration() / 1000));
        }
    }

    @SuppressLint("DefaultLocale")
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void stopAllSounds() {
        for (MediaPlayer mediaPlayer : mediaPlayerMap.values()) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        mediaPlayerMap.clear();
        playingSounds.clear();
        livePlayingSoundList.setValue(playingSounds);
        isPlaying.setValue(false);
    }

    public void startServiceWithSound(Context context, int soundId) {
        int resId = getResIdSong(findSoundById(sounds, soundId).getFileName(), context);
        Intent intent = createPlaySoundIntent(resId);
        context.startService(intent);
    }

    public int getResIdSong(String fileName, Context context) {
        return context.getResources().getIdentifier(fileName, "raw", context.getPackageName());
    }

    public Intent createPlaySoundIntent(int resId) {
        Intent intent = new Intent();
        intent.setAction(RelaxService.PLAY_SOUND_ACTION);
        intent.putExtra("resId", resId);
        return intent;
    }

    public Intent createChangeVolumeSoundIntent(int resId, int volume) {
        Intent intent = new Intent();
        intent.setAction(RelaxService.UPDATE_VOLUME_ACTION);
        intent.putExtra("resId", resId);
        intent.putExtra("volume", volume);
        return intent;
    }

    public MutableLiveData<String> getSoundTitle() {
        return soundTitle;
    }

    public MutableLiveData<String> getSoundImageUrl() {
        return soundImageUrl;
    }

    public MutableLiveData<String> getEndTime() {
        return endTime;
    }

    public MutableLiveData<Integer> getCurrentTime() {
        return currentTime;
    }

    public MutableLiveData<Boolean> getIsPlaying() {
        return isPlaying;
    }

    public MutableLiveData<Integer> getSelectedCount() {
        return selectedCount;
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
    }

    public List<Sound> getPlayingSounds() {
        return playingSounds;
    }

    public void setPlayingSounds(List<Sound> playingSounds) {
        this.playingSounds = playingSounds;
    }

    public MutableLiveData<List<Sound>> getLiveSoundList() {
        return liveSoundList;
    }

    public void setLiveSoundList(MutableLiveData<List<Sound>> liveSoundList) {
        this.liveSoundList = liveSoundList;
    }

    public MutableLiveData<List<Sound>> getLivePlayingSoundList() {
        return livePlayingSoundList;
    }

    public void setLivePlayingSoundList(MutableLiveData<List<Sound>> livePlayingSoundList) {
        this.livePlayingSoundList = livePlayingSoundList;
    }

    public Map<Integer, MediaPlayer> getMediaPlayerMap() {
        return mediaPlayerMap;
    }

    public void setMediaPlayerMap(Map<Integer, MediaPlayer> mediaPlayerMap) {
        this.mediaPlayerMap = mediaPlayerMap;
    }

    public MutableLiveData<Map<Integer, MediaPlayer>> getLiveMediaPlayerMap() {
        return liveMediaPlayerMap;
    }

    public MutableLiveData<Integer> getTotalTime() {
        return totalTime;
    }
}
