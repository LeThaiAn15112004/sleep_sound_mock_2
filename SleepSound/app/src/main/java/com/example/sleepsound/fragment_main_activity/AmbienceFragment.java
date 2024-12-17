package com.example.sleepsound.fragment_main_activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sleepsound.adapter.SoundAdapter;
import com.example.sleepsound.databinding.FragmentAmbienceBinding;
import com.example.sleepsound.model.Sound;
import com.example.sleepsound.service.AmbienceService;
import com.example.sleepsound.spaceitem.SpaceItem;
import com.example.sleepsound.viewmodel.AmbienceFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class AmbienceFragment extends Fragment implements SoundAdapter.OnItemClickListener {
    private SoundAdapter soundAdapter;
    private FragmentAmbienceBinding fragmentAmbienceBinding;
    private AmbienceFragmentViewModel ambienceFragmentViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAmbienceBinding = FragmentAmbienceBinding.inflate(inflater, container, false);
        initialize();
        return fragmentAmbienceBinding.getRoot();
    }

    private void initialize() {
        try {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
            fragmentAmbienceBinding.ambienceRecycleView.setLayoutManager(layoutManager);
            fragmentAmbienceBinding.ambienceRecycleView.setHasFixedSize(true);
            ambienceFragmentViewModel = new ViewModelProvider(requireActivity()).get(AmbienceFragmentViewModel.class);
            ambienceFragmentViewModel.getLiveSoundList().observe(requireActivity(), sounds -> {
                soundAdapter = new SoundAdapter(requireActivity(), sounds);
                fragmentAmbienceBinding.ambienceRecycleView.setAdapter(soundAdapter);
                soundAdapter.setOnItemClickListener(this::onItemClick); // Gắn listener sau khi tạo adapter
            });
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Something went wrong, please try again later !", Toast.LENGTH_SHORT).show();
            System.err.println(e);
        }
    }

    @Override
    public void onItemClick(int pos) {
        Sound clickedSound = soundAdapter.getSoundList().get(pos);
        String soundUri = clickedSound.getFileName();
        int resId = ambienceFragmentViewModel.getResIdSong(soundUri, requireActivity());
        soundAdapter.notifyDataSetChanged();
        ambienceFragmentViewModel.toastForPlaying(clickedSound, requireActivity());
        Intent intent = ambienceFragmentViewModel.createPlaySoundIntent(resId);
        intent.setClass(requireActivity(), AmbienceService.class);
        requireContext().startService(intent);
    }
}