package com.example.sleepsound.fragment_main_activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sleepsound.R;
import com.example.sleepsound.databinding.FragmentMusicBinding;
import com.example.sleepsound.viewmodel.MusicFragmentViewModel;

public class MusicFragment extends Fragment {

    private FragmentMusicBinding fragmentMusicBinding;
    private MusicFragmentViewModel musicFragmentViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMusicBinding = FragmentMusicBinding.inflate(inflater, container, false);
        musicFragmentViewModel = new ViewModelProvider(requireActivity()).get(MusicFragmentViewModel.class);
        initialize();
        clicking();
        return fragmentMusicBinding.getRoot();
    }

    private void initialize() {
        try {

        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Something went wrong, please try again later !", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }

    private void clicking() {

    }
}