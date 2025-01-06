package com.example.sleepsound.fragment_main_activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sleepsound.R;
import com.example.sleepsound.activity.MusicActivity;
import com.example.sleepsound.adapter.GenreAdapter;
import com.example.sleepsound.adapter.MusicAdapter;
import com.example.sleepsound.databinding.FragmentMusicBinding;
import com.example.sleepsound.model.MusicSoundItem;
import com.example.sleepsound.viewmodel.MusicFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends Fragment implements
        GenreAdapter.OnGenreClickListener,
        MusicAdapter.OnItemClickListener{

    private FragmentMusicBinding fragmentMusicBinding;
    private MusicFragmentViewModel musicFragmentViewModel;
    private GenreAdapter genreAdapter;
    private MusicAdapter musicAdapter;
    private List<String> genresList = new ArrayList<>();
    private List<MusicSoundItem> musicSoundItems = new ArrayList<>();

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
            fragmentMusicBinding.recyclerViewGenres.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            musicFragmentViewModel.getLiveGenres().observe(requireActivity(), genres ->{
                for (String s : genres){
                    System.out.println(s);
                }
                genresList = genres;
                genreAdapter = new GenreAdapter(requireActivity(), genres, this);
                fragmentMusicBinding.recyclerViewGenres.setAdapter(genreAdapter);
                musicFragmentViewModel.getLiveSelectedGenre().observe(requireActivity(), selected ->{
                    int selectedIndex = genres.indexOf(selected); // Lấy index của selected trong genres
                    if (selectedIndex != -1) { // Nếu tìm thấy
                        genreAdapter.setPosition(selectedIndex); // Cập nhật vị trí
                        genreAdapter.notifyDataSetChanged(); // Thông báo thay đổi dữ liệu
                    }
                    genreAdapter.notifyDataSetChanged();
                });
            });

            fragmentMusicBinding.recyclerViewMusic.setLayoutManager(new GridLayoutManager(getContext(), 2));
            musicFragmentViewModel.getLiveFullMusicItem().observe(requireActivity(), musics ->{
                if (musics != null && !musics.isEmpty()){
                    musicSoundItems = musics;
                    musicAdapter = new MusicAdapter(requireActivity(), musics, this);
                    fragmentMusicBinding.recyclerViewMusic.setAdapter(musicAdapter);
                    fragmentMusicBinding.progressBar.setVisibility(View.GONE);
                }else{
                    fragmentMusicBinding.progressBar.setVisibility(View.VISIBLE);
                }
            });

            fragmentMusicBinding.recyclerViewRecentlyPlayed.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            musicFragmentViewModel.getLiveRecentlyPlayed().observe(requireActivity(), recentlyPlayedMusics ->{

            });
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Something went wrong, please try again later !", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }

    private void clicking() {

    }

    @Override
    public void onGenreClick(int position) {
        try{
            genreAdapter.setPosition(position); // Cập nhật vị trí được chọn
            musicFragmentViewModel.filterMusicByGenre(genresList.get(position));
            genreAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void OnItemClickListener(int position) {
        try{
            Intent intent = new Intent(requireActivity(), MusicActivity.class);
            MusicSoundItem item = musicSoundItems.get(position);
            intent.putExtra("id", item.getId());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("premium", item.isPremium());
            intent.putExtra("group", item.getGroup());
            intent.putExtra("url", item.getUrl());
            intent.putExtra("thumbnail", item.getThumbnail());
            intent.putExtra("background", item.getBackground());
            intent.putExtra("badge", item.getBadge());
            requireActivity().startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }
}