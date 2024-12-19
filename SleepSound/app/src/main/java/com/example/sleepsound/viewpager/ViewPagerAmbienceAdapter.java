package com.example.sleepsound.viewpager;

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

public class ViewPagerAmbienceAdapter extends RecyclerView.Adapter<ViewPagerAmbienceAdapter.PageViewHolder>  {
    private final List<List<Sound>> paginatedSoundList;
    private final Context context;
    private final AmbienceFragmentViewModel ambienceFragmentViewModel;

    public ViewPagerAmbienceAdapter(Context context, List<List<Sound>> paginatedSoundList, AmbienceFragmentViewModel ambienceFragmentViewModel) {
        this.context = context;
        this.paginatedSoundList = paginatedSoundList;
        this.ambienceFragmentViewModel = ambienceFragmentViewModel;
    }

    @NonNull
    @Override
    public ViewPagerAmbienceAdapter.PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewPagerBinding itemViewPagerBinding = ItemViewPagerBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PageViewHolder(itemViewPagerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAmbienceAdapter.PageViewHolder holder, int position) {
        List<Sound> sounds = paginatedSoundList.get(position);

        // Thiết lập RecyclerView trong từng trang của ViewPager
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        holder.binding.recyclerView.setLayoutManager(layoutManager);
        SoundAdapter soundAdapter = new SoundAdapter(context, sounds);
        holder.binding.recyclerView.setAdapter(soundAdapter);
        soundAdapter.setOnItemClickListener(new SoundAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                try{
                    Sound clickedSound = soundAdapter.getSoundList().get(pos);
                    String soundFileName = clickedSound.getFileName();
                    int resId = ambienceFragmentViewModel.getResIdSong(soundFileName, context);
                    soundAdapter.notifyDataSetChanged();
                    ambienceFragmentViewModel.toggleSound(clickedSound, context);
                    Intent intent = ambienceFragmentViewModel.createPlaySoundIntent(resId);
                    intent.setClass(context, AmbienceService.class);
                    context.startService(intent);
                }catch (Exception e){
                    Toast.makeText(context, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
                    System.err.println(e.getMessage());
                }
            }
        });
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
