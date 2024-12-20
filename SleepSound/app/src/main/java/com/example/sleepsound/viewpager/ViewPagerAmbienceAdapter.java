package com.example.sleepsound.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.sleepsound.adapter.SoundAdapter;
import com.example.sleepsound.databinding.ItemViewPagerBinding;
import com.example.sleepsound.fragment_main_activity.AmbienceFragment;
import com.example.sleepsound.model.Sound;
import com.example.sleepsound.service.AmbienceService;
import com.example.sleepsound.viewmodel.AmbienceFragmentViewModel;

import java.util.List;

public class ViewPagerAmbienceAdapter extends RecyclerView.Adapter<ViewPagerAmbienceAdapter.PageViewHolder> {
    private final List<List<Sound>> paginatedSoundList;
    private final Context context;
    private final AmbienceFragmentViewModel ambienceFragmentViewModel;
    private final SoundAdapter soundAdapter;

    public ViewPagerAmbienceAdapter(Context context, List<List<Sound>> paginatedSoundList, AmbienceFragmentViewModel ambienceFragmentViewModel, SoundAdapter soundAdapter) {
        this.context = context;
        this.paginatedSoundList = paginatedSoundList;
        this.ambienceFragmentViewModel = ambienceFragmentViewModel;
        this.soundAdapter = soundAdapter;
    }

    @NonNull
    @Override
    public ViewPagerAmbienceAdapter.PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewPagerBinding itemViewPagerBinding = ItemViewPagerBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PageViewHolder(itemViewPagerBinding);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewPagerAmbienceAdapter.PageViewHolder holder, int position) {
        List<Sound> sounds = paginatedSoundList.get(position);
        holder.binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
        holder.binding.recyclerView.setAdapter(soundAdapter);
        soundAdapter.setSoundList(sounds);
        soundAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return paginatedSoundList.size();
    }

    public static class PageViewHolder extends RecyclerView.ViewHolder {
        private final ItemViewPagerBinding binding;

        public PageViewHolder(@NonNull ItemViewPagerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}