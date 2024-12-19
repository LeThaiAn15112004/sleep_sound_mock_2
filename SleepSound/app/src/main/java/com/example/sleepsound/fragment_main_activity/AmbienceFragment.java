package com.example.sleepsound.fragment_main_activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sleepsound.R;
import com.example.sleepsound.adapter.SoundAdapter;
import com.example.sleepsound.adapter.SoundPlayingAdapter;
import com.example.sleepsound.databinding.DialogPlayingSoundListBinding;
import com.example.sleepsound.databinding.DialogTimePickerBinding;
import com.example.sleepsound.databinding.FragmentAmbienceBinding;
import com.example.sleepsound.model.Sound;
import com.example.sleepsound.service.AmbienceService;
import com.example.sleepsound.shared_preferences.MySharedPreferences;
import com.example.sleepsound.viewmodel.AmbienceFragmentViewModel;
import com.example.sleepsound.viewpager.ViewPagerAdapter;
import com.example.sleepsound.viewpager.ViewPagerAmbienceAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AmbienceFragment extends Fragment implements
        SoundAdapter.OnItemClickListener,
        SoundPlayingAdapter.OnItemCancelClickListener,
        SoundPlayingAdapter.OnSeekBarChangeListener {
    private SoundAdapter soundAdapter;
    private SoundPlayingAdapter soundPlayingAdapter;
    private FragmentAmbienceBinding fragmentAmbienceBinding;
    private AmbienceFragmentViewModel ambienceFragmentViewModel;
    private MySharedPreferences mySharedPreferences;

    private int currentPage = 0;
    private int itemsPerPage = 9;
    private List<List<Sound>> paginatedSoundList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mySharedPreferences = new MySharedPreferences(requireActivity());
        fragmentAmbienceBinding = FragmentAmbienceBinding.inflate(inflater, container, false);
        initialize();
        clicking();
        return fragmentAmbienceBinding.getRoot();
    }

    private void initialize() {
        try {
//            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
//          fragmentAmbienceBinding.ambienceRecycleView.setLayoutManager(layoutManager);
//          fragmentAmbienceBinding.ambienceRecycleView.setHasFixedSize(true);
            ambienceFragmentViewModel = new ViewModelProvider(requireActivity()).get(AmbienceFragmentViewModel.class);

//            ambienceFragmentViewModel.getLiveSoundList().observe(requireActivity(), sounds -> {
//                if (soundAdapter == null) {
//                    soundAdapter = new SoundAdapter(requireActivity(), sounds);
//                    fragmentAmbienceBinding.ambienceRecycleView.setAdapter(soundAdapter);
//                    initializePagination(sounds);
//                    soundAdapter.setOnItemClickListener(AmbienceFragment.this);
//                } else {
//                    soundAdapter.setSoundList(sounds); // Assuming you have this method to refresh the adapter
//                }
//            });

            ambienceFragmentViewModel.getLiveSoundList().observe(requireActivity(), new Observer<List<Sound>>() {
                @Override
                public void onChanged(List<Sound> sounds) {
                    if (sounds != null && !sounds.isEmpty()) {
                        initializePagination(sounds); // Truyền danh sách âm thanh thực sự vào đây
                    } else {
                        Toast.makeText(requireActivity(), "No sounds available!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ambienceFragmentViewModel.getLiveNumberPlaying().observe(requireActivity(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer > 0) {
                        fragmentAmbienceBinding.tabBadgeCount.setVisibility(View.VISIBLE);
                        fragmentAmbienceBinding.tabBadgeCount.setText(String.valueOf(integer));
                    } else {
                        fragmentAmbienceBinding.tabBadgeCount.setVisibility(View.GONE);
                    }
                }
            });

            ambienceFragmentViewModel.getLiveHavingPlayingSound().observe(requireActivity(), isPlaying -> {
                int iconRes = isPlaying ? R.drawable.ic_pause : R.drawable.ic_play;
                fragmentAmbienceBinding.btnPlay.setIconResource(iconRes);
            });
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Something went wrong, please try again later !", Toast.LENGTH_SHORT).show();
            System.err.println(e);
        }
    }

    private List<List<Sound>> paginateList(List<Sound> originalList, int itemsPerPage) {
        List<List<Sound>> paginatedList = new ArrayList<>();
        for (int i = 0; i < originalList.size(); i += itemsPerPage) {
            int end = Math.min(originalList.size(), i + itemsPerPage);
            paginatedList.add(originalList.subList(i, end));
        }
        return paginatedList;
    }


    private void initializePagination(List<Sound> sounds) {
        paginatedSoundList = paginateList(sounds, itemsPerPage); // Chuyển đổi danh sách âm thanh thành phân trang
        initializeViewPager(paginatedSoundList); // Gán vào ViewPager
    }

    private void initializeViewPager(List<List<Sound>> paginatedSoundList) {
        ViewPager2 viewPager = fragmentAmbienceBinding.viewPager;
        TabLayout tabLayout = fragmentAmbienceBinding.tabLayout;

        // Gán adapter cho ViewPager2
        viewPager.setAdapter(new ViewPagerAmbienceAdapter(requireActivity(), paginatedSoundList, ambienceFragmentViewModel));

        // Kết nối TabLayout và ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Tùy chỉnh hiển thị chấm cho mỗi tab
            tab.setIcon(position == 0 ? R.drawable.ic_dot_active : R.drawable.ic_dot_inactive);
        }).attach();

        // Đăng ký sự kiện khi người dùng vuốt qua các trang
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Khi người dùng vuốt sang trang mới, có thể thực hiện hành động tùy chỉnh
                // Ví dụ: thay đổi màu sắc của tab hoặc thông báo cho người dùng
                Log.d("PageSelected", "Page: " + position);
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tabItem = tabLayout.getTabAt(i);
                    if (tabItem != null) {
                        tabItem.setIcon(i == position ? R.drawable.ic_dot_active : R.drawable.ic_dot_inactive);
                    }
                }
            }
        });
    }

    private void clicking() {
        fragmentAmbienceBinding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                ambienceFragmentViewModel.stopAllSounds(requireActivity());
                soundAdapter.notifyDataSetChanged();
            }
        });

        fragmentAmbienceBinding.timerLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        fragmentAmbienceBinding.selectedLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoundPlayingList();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void showTimePickerDialog() {
        BottomSheetDialog bottomTimerDialog = new BottomSheetDialog(requireActivity());
        DialogTimePickerBinding dialogTimePickerBinding = DialogTimePickerBinding.inflate(getLayoutInflater());
        bottomTimerDialog.setContentView(dialogTimePickerBinding.getRoot());

        setupNumberPicker(dialogTimePickerBinding.hourPicker, 23);
        setupNumberPicker(dialogTimePickerBinding.minutePicker, 59);
        setupNumberPicker(dialogTimePickerBinding.secondPicker, 59);

        dialogTimePickerBinding.hourPicker.setValue(mySharedPreferences.getIntValue("hourStopSound"));
        dialogTimePickerBinding.minutePicker.setValue(mySharedPreferences.getIntValue("minuteStopSound"));
        dialogTimePickerBinding.secondPicker.setValue(mySharedPreferences.getIntValue("secondStopSound"));

        dialogTimePickerBinding.btnDone.setOnClickListener(v -> {
            int hour = dialogTimePickerBinding.hourPicker.getValue();
            int minute = dialogTimePickerBinding.minutePicker.getValue();
            int second = dialogTimePickerBinding.secondPicker.getValue();
            System.out.println(hour + ":" + minute + ":" + second);
            ambienceFragmentViewModel.startTimer(hour, minute, second, requireActivity());
            ambienceFragmentViewModel.setTimeStopSound(hour, minute, second, mySharedPreferences);
            bottomTimerDialog.dismiss();
        });

        bottomTimerDialog.show();
    }

    @SuppressLint("DefaultLocale")
    private void setupNumberPicker(NumberPicker numberPicker, int maxValue) {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(maxValue);
        numberPicker.setFormatter(i -> String.format("%02d", i));
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            System.out.println("NumberPicker updated: " + newVal);
        });
        setNumberPickerTextColor(numberPicker);
    }

    private void setNumberPickerTextColor(NumberPicker numberPicker) {
        try {
            // Duyệt qua tất cả các field của NumberPicker
            Field[] fields = NumberPicker.class.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("mSelectorWheelPaint")) {
                    field.setAccessible(true);
                    Paint paint = (Paint) field.get(numberPicker);
                    assert paint != null;
                    paint.setColor(Color.WHITE);
                    numberPicker.invalidate();
                }
            }

            // Duyệt qua các TextView con bên trong NumberPicker
            for (int i = 0; i < numberPicker.getChildCount(); i++) {
                View child = numberPicker.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setTextColor(Color.WHITE);
                    child.invalidate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSoundPlayingList() {
        BottomSheetDialog bottomListPlayingDialog = new BottomSheetDialog(requireActivity());
        DialogPlayingSoundListBinding dialogPlayingSoundListBinding = DialogPlayingSoundListBinding.inflate(getLayoutInflater());
        bottomListPlayingDialog.setContentView(dialogPlayingSoundListBinding.getRoot());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        dialogPlayingSoundListBinding.ambiencePlayingRecycleView.setLayoutManager(layoutManager);
        if (soundPlayingAdapter == null) {
            soundPlayingAdapter = new SoundPlayingAdapter(requireActivity(), new ArrayList<>());
            soundPlayingAdapter.setOnItemCancelClickListener(this);
            soundPlayingAdapter.setOnSeekBarChangeListener(this);

        }
        dialogPlayingSoundListBinding.ambiencePlayingRecycleView.setAdapter(soundPlayingAdapter);

        ambienceFragmentViewModel.getLivePlayingSoundList().observe(requireActivity(), playingSounds -> {
            if (playingSounds != null && !playingSounds.isEmpty()) {
                soundPlayingAdapter.setSoundPlayingList(playingSounds);
                dialogPlayingSoundListBinding.ambiencePlayingRecycleView.setVisibility(View.VISIBLE);
                dialogPlayingSoundListBinding.noSoundTextView.setVisibility(View.GONE);
            } else {
                dialogPlayingSoundListBinding.ambiencePlayingRecycleView.setVisibility(View.GONE);
                dialogPlayingSoundListBinding.noSoundTextView.setVisibility(View.VISIBLE);
            }
        });

//        dialogPlayingSoundListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomListPlayingDialog.dismiss();
//            }
//        });
        dialogPlayingSoundListBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomListPlayingDialog.dismiss();
            }
        });
        bottomListPlayingDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClick(int pos) {
        try{
            Sound clickedSound = soundAdapter.getSoundList().get(pos);
            String soundFileName = clickedSound.getFileName();
            int resId = ambienceFragmentViewModel.getResIdSong(soundFileName, requireActivity());
            soundAdapter.notifyDataSetChanged();
            ambienceFragmentViewModel.toggleSound(clickedSound, requireActivity());
            Intent intent = ambienceFragmentViewModel.createPlaySoundIntent(resId);
            intent.setClass(requireActivity(), AmbienceService.class);
            requireContext().startService(intent);
        }catch (Exception e){
            Toast.makeText(requireActivity(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            System.err.println(e.getMessage());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCancelClick(int pos) {
        try{
            Sound clickedSound = soundPlayingAdapter.getSoundPlayingList().get(pos);
            String soundFileName = clickedSound.getFileName();
            int resId = ambienceFragmentViewModel.getResIdSong(soundFileName, requireActivity());
            soundPlayingAdapter.notifyDataSetChanged();
            soundAdapter.notifyDataSetChanged();
            ambienceFragmentViewModel.toggleSound(clickedSound, requireActivity());
            Intent intent = ambienceFragmentViewModel.createPlaySoundIntent(resId);
            intent.setClass(requireActivity(), AmbienceService.class);
            requireContext().startService(intent);
        }catch (Exception e){
            Toast.makeText(requireActivity(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            System.err.println(e.getMessage());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSeekBarChange(int pos) {
        try{
            Sound clickedSound = soundPlayingAdapter.getSoundPlayingList().get(pos);
            String soundFileName = clickedSound.getFileName();
            int resId = ambienceFragmentViewModel.getResIdSong(soundFileName, requireActivity());
            Intent intent = ambienceFragmentViewModel.createChangeVolumeSoundIntent(resId, clickedSound.getVolume());
            intent.setClass(requireActivity(), AmbienceService.class);
            requireContext().startService(intent);
        }catch (Exception e){
            Toast.makeText(requireActivity(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            System.err.println(e.getMessage());
        }
    }
}