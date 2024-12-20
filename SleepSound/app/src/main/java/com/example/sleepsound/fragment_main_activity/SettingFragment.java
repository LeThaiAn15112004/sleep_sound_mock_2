package com.example.sleepsound.fragment_main_activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sleepsound.R;
import com.example.sleepsound.databinding.DialogTimePickerSettingBinding;
import com.example.sleepsound.databinding.FragmentAmbienceBinding;
import com.example.sleepsound.databinding.FragmentSettingBinding;
import com.example.sleepsound.shared_preferences.MySharedPreferences;
import com.example.sleepsound.viewmodel.AmbienceFragmentViewModel;
import com.example.sleepsound.viewmodel.SettingFragmentViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
            settingFragmentViewModel.setMySharedPreferences(mySharedPreferences);

            settingFragmentViewModel.updateFormattedReminderTime();

            settingFragmentViewModel.getFormattedReminderTime().observe(requireActivity(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    fragmentSettingBinding.bedtimeReminderTime.setText(s);
                }
            });
        }catch (Exception e){
            Toast.makeText(requireActivity(), "Something went wrong, please try again later !", Toast.LENGTH_SHORT).show();
            System.err.println(e);
        }
    }

    private void clicking() {
        fragmentSettingBinding.bedtimeReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClockDialog();
            }
        });

        fragmentSettingBinding.sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Feedback clicked", Toast.LENGTH_SHORT).show();
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

    private void showClockDialog(){
        BottomSheetDialog bottomClockDialog = new BottomSheetDialog(requireActivity());
        DialogTimePickerSettingBinding dialogTimePickerSettingBinding = DialogTimePickerSettingBinding.inflate(getLayoutInflater());
        bottomClockDialog.setContentView(dialogTimePickerSettingBinding.getRoot());

        settingFragmentViewModel.setupNumberPicker(dialogTimePickerSettingBinding.hourPicker, 23);
        settingFragmentViewModel.setupNumberPicker(dialogTimePickerSettingBinding.minutePicker, 59);

        dialogTimePickerSettingBinding.switchToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dialogTimePickerSettingBinding.switchText.setText(isChecked ? "Turn On" : "Turn Off");
        });

        dialogTimePickerSettingBinding.hourPicker.setValue(mySharedPreferences.getIntValue("hourReminderSleep", 0));
        dialogTimePickerSettingBinding.minutePicker.setValue(mySharedPreferences.getIntValue("minuteReminderSleep", 0));
        dialogTimePickerSettingBinding.switchText.setText(mySharedPreferences.getBooleanValue("turnOnReminder", false) ? "Turn On":"Turn Off");
        dialogTimePickerSettingBinding.switchToggle.setChecked(mySharedPreferences.getBooleanValue("turnOnReminder", false));

        dialogTimePickerSettingBinding.btnDone.setOnClickListener(v -> {
            int hour = dialogTimePickerSettingBinding.hourPicker.getValue();
            int minute = dialogTimePickerSettingBinding.minutePicker.getValue();
            boolean isTurnOn = dialogTimePickerSettingBinding.switchToggle.isChecked();
            settingFragmentViewModel.setTimeReminder(hour, minute, isTurnOn, mySharedPreferences);
            System.out.println(hour + ":" + minute + " | " + (isTurnOn ? "On" : "Off"));
            bottomClockDialog.dismiss();
        });
        bottomClockDialog.show();
    }

}