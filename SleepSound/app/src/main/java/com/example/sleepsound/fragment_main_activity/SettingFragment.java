package com.example.sleepsound.fragment_main_activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sleepsound.R;
import com.example.sleepsound.databinding.FragmentAmbienceBinding;
import com.example.sleepsound.databinding.FragmentSettingBinding;
import com.example.sleepsound.shared_preferences.MySharedPreferences;
import com.example.sleepsound.viewmodel.AmbienceFragmentViewModel;
import com.example.sleepsound.viewmodel.SettingFragmentViewModel;

public class SettingFragment extends Fragment {
    private MySharedPreferences mySharedPreferences;
    private SettingFragmentViewModel settingFragmentViewModel;
    private FragmentSettingBinding fragmentSettingBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mySharedPreferences = new MySharedPreferences(requireActivity());
        fragmentSettingBinding = FragmentSettingBinding.inflate(inflater, container, false);
        initialize();
        clicking();
        return fragmentSettingBinding.getRoot();
    }

    private void initialize() {
        try{
            settingFragmentViewModel = new ViewModelProvider(requireActivity()).get(SettingFragmentViewModel.class);
        }catch (Exception e){
            Toast.makeText(requireActivity(), "Something went wrong, please try again later !", Toast.LENGTH_SHORT).show();
            System.err.println(e);
        }
    }

    private void clicking() {
        fragmentSettingBinding.bedtimeReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Bedtime Reminder clicked", Toast.LENGTH_SHORT).show();
            }
        });

        fragmentSettingBinding.sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Send Feedback clicked", Toast.LENGTH_SHORT).show();
            }
        });

        fragmentSettingBinding.shareThisApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Share This App clicked", Toast.LENGTH_SHORT).show();
            }
        });

        fragmentSettingBinding.privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Privacy Policy clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

}