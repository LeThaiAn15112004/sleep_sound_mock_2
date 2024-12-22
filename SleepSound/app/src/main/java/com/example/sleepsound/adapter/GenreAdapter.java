package com.example.sleepsound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sleepsound.databinding.ItemGenreBinding;
import com.example.sleepsound.model.Mix;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder>{
    private Context context;
    private List<String> genres;
    private OnGenreClickListener onGenreClickListener;

    public GenreAdapter(Context context, List<String> genres, OnGenreClickListener onGenreClickListener) {
        this.context = context;
        this.genres = genres;
        this.onGenreClickListener = onGenreClickListener;
    }

    public interface OnGenreClickListener {
        void onGenreClick(int position);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        notifyDataSetChanged();
    }

    public List<String> getGenres() {
        return genres;
    }

    public OnGenreClickListener getOnGenreClickListener() {
        return onGenreClickListener;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
        notifyDataSetChanged();
    }

    public void setOnGenreClickListener(OnGenreClickListener onGenreClickListener) {
        this.onGenreClickListener = onGenreClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenreAdapter.GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGenreBinding itemGenreBinding = ItemGenreBinding.inflate(LayoutInflater.from(context), parent, false);
        return new GenreViewHolder(itemGenreBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.GenreViewHolder holder, int position) {
        String genre = genres.get(position);
        holder.bind(genre, onGenreClickListener, position);
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        private final ItemGenreBinding itemGenreBinding;
        public GenreViewHolder(@NonNull ItemGenreBinding itemGenreBinding) {
            super(itemGenreBinding.getRoot());
            this.itemGenreBinding = itemGenreBinding;
        }

        public void bind(String genre, GenreAdapter.OnGenreClickListener onGenreClickListener, int position) {
            try{
                itemGenreBinding.tvGenreName.setText(genre);
                itemGenreBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onGenreClickListener != null){
                            onGenreClickListener.onGenreClick(position);
                        }
                    }
                });
            } catch (Exception e) {
                Toast.makeText(itemGenreBinding.getRoot().getContext(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
                System.out.println(e);
            }
        }
    }
}
