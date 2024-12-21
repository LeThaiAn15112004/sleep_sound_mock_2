package com.example.sleepsound.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RelaxSoundActivityViewModel extends ViewModel {

    private final MutableLiveData<String> soundTitle = new MutableLiveData<>();
    private final MutableLiveData<String> soundImageUrl = new MutableLiveData<>();
    private final MutableLiveData<String> endTime = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentTime = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPlaying = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedCount = new MutableLiveData<>();

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
}
