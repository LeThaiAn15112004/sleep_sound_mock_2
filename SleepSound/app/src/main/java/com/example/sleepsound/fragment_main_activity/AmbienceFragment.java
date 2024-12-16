package com.example.sleepsound.fragment_main_activity;

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
import com.example.sleepsound.spaceitem.SpaceItem;
import com.example.sleepsound.viewmodel.AmbienceFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class AmbienceFragment extends Fragment {
    private SoundAdapter soundAdapter;
    private FragmentAmbienceBinding fragmentAmbienceBinding;
    private AmbienceFragmentViewModel ambienceFragmentViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAmbienceBinding = FragmentAmbienceBinding.inflate(inflater, container, false);
        initializeRecyclerView();
        return fragmentAmbienceBinding.getRoot();
    }

    private void initializeRecyclerView() {
        try{
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
            fragmentAmbienceBinding.ambienceRecycleView.setLayoutManager(layoutManager);
            fragmentAmbienceBinding.ambienceRecycleView.setHasFixedSize(true);
            ambienceFragmentViewModel = new ViewModelProvider(requireActivity()).get(AmbienceFragmentViewModel.class);
            ambienceFragmentViewModel.getLiveSoundList().observe(requireActivity(), new Observer<List<Sound>>() {
                @Override
                public void onChanged(List<Sound> sounds) {
                    soundAdapter = new SoundAdapter(requireActivity(), sounds);
                    fragmentAmbienceBinding.ambienceRecycleView.setAdapter(soundAdapter);
                }
            });
        }catch (Exception e){
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


}