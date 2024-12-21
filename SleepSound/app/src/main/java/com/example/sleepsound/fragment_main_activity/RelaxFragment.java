package com.example.sleepsound.fragment_main_activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.sleepsound.R;
import com.example.sleepsound.activity.RelaxSoundActivity;
import com.example.sleepsound.adapter.MixAdapter;
import com.example.sleepsound.databinding.FragmentRelaxBinding;
import com.example.sleepsound.databinding.FragmentSettingBinding;
import com.example.sleepsound.model.Mix;
import com.example.sleepsound.shared_preferences.MySharedPreferences;
import com.example.sleepsound.viewmodel.RelaxFragmentViewModel;

import java.util.List;

public class RelaxFragment extends Fragment implements MixAdapter.OnItemClickListener {
    private MySharedPreferences mySharedPreferences;
    private RelaxFragmentViewModel relaxFragmentViewModel;
    private FragmentRelaxBinding fragmentRelaxBinding;
    private MixAdapter mixAdapter;
    private Button selectedButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mySharedPreferences = new MySharedPreferences(requireActivity());
        fragmentRelaxBinding = FragmentRelaxBinding.inflate(inflater, container, false);
        relaxFragmentViewModel = new ViewModelProvider(requireActivity()).get(RelaxFragmentViewModel.class);
        relaxFragmentViewModel.setMySharedPreferences(mySharedPreferences);
        initialize();
        clicking();
        return fragmentRelaxBinding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initialize() {
        try {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 2);
            fragmentRelaxBinding.recyclerViewMixes.setLayoutManager(gridLayoutManager);
            relaxFragmentViewModel.getLiveMixList().observe(requireActivity(), new Observer<List<Mix>>() {
                @Override
                public void onChanged(List<Mix> mixes) {
                    mixAdapter = new MixAdapter(mixes, requireActivity());
                    fragmentRelaxBinding.recyclerViewMixes.setAdapter(mixAdapter);
                    mixAdapter.notifyDataSetChanged();
                    mixAdapter.setOnItemClickListener(RelaxFragment.this);
                }
            });
        }catch (Exception e) {
            Toast.makeText(requireActivity(), "Something went wrong, please try again later !", Toast.LENGTH_SHORT).show();
            System.err.println(e);
        }
    }

    private void clicking() {
        fragmentRelaxBinding.btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonSelection(fragmentRelaxBinding.btnAll, relaxFragmentViewModel.CATEGORY_ALL);
            }
        });

        fragmentRelaxBinding.btnPianoRelax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonSelection(fragmentRelaxBinding.btnPianoRelax, relaxFragmentViewModel.CATEGORY_PIANO_RELAX);
            }
        });

        fragmentRelaxBinding.btnRain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonSelection(fragmentRelaxBinding.btnRain, relaxFragmentViewModel.CATEGORY_RAIN);
            }
        });

        fragmentRelaxBinding.btnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonSelection(fragmentRelaxBinding.btnCity, relaxFragmentViewModel.CATEGORY_CITY);
            }
        });

        fragmentRelaxBinding.btnMeditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonSelection(fragmentRelaxBinding.btnMeditation, relaxFragmentViewModel.CATEGORY_MEDITATION);
            }
        });

        fragmentRelaxBinding.btnFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonSelection(fragmentRelaxBinding.btnFocus, relaxFragmentViewModel.CATEGORY_FOCUS);
            }
        });
    }

    private void handleButtonSelection(Button button, int category) {
        if (selectedButton != null) {
            selectedButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.button_background));
        }

        selectedButton = button;
        selectedButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.button_selected));

        relaxFragmentViewModel.filterMixesByCategory(category);
    }

    @Override
    public void onItemClick(int pos) {
        try {
            Mix clickedMix = mixAdapter.getMixes().get(pos);
            Intent intent = new Intent(requireActivity(), RelaxSoundActivity.class);
            // Truyền từng thuộc tính của `Mix`
            intent.putExtra("mixSoundId", clickedMix.getMixSoundId());
            intent.putExtra("category", clickedMix.getCategory());
            intent.putExtra("name", clickedMix.getName());

            // Truyền các giá trị của Cover
            Mix.Cover cover = clickedMix.getCover();
            if (cover != null) {
                intent.putExtra("coverThumbnail", cover.getThumbnail());
                intent.putExtra("coverBackground", cover.getBackground());
            }

            // Truyền danh sách Volumes dưới dạng chuỗi JSON (hoặc cách khác phù hợp)
            List<Mix.Volume> volumes = clickedMix.getVolumes();
            if (volumes != null && !volumes.isEmpty()) {
                StringBuilder volumesJson = new StringBuilder("[");
                for (Mix.Volume volume : volumes) {
                    volumesJson.append("{")
                            .append("\"id\":").append(volume.getId()).append(",")
                            .append("\"volume\":").append(volume.getVolume())
                            .append("},");
                }
                // Loại bỏ dấu phẩy cuối và thêm dấu ngoặc vuông đóng
                volumesJson.deleteCharAt(volumesJson.length() - 1).append("]");
                intent.putExtra("volumes", volumesJson.toString());
            }
            startActivity(intent);
        }catch (Exception e) {
            Toast.makeText(requireActivity(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            System.err.println(e.getMessage());
        }
    }
}
