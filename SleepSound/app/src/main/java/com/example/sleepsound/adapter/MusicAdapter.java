package com.example.sleepsound.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.sleepsound.databinding.ItemMusicBinding;
import com.example.sleepsound.model.MusicSoundItem;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder>{
    private Context context;
    private List<MusicSoundItem> musicList;
    private OnItemClickListener onItemClickListener;

    public MusicAdapter(Context context, List<MusicSoundItem> musicList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.musicList = musicList;
        this.onItemClickListener = onItemClickListener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        notifyDataSetChanged();
    }

    public List<MusicSoundItem> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<MusicSoundItem> musicList) {
        this.musicList = musicList;
        notifyDataSetChanged();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int position);
    }

    @NonNull
    @Override
    public MusicAdapter.MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMusicBinding itemMusicBinding = ItemMusicBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MusicViewHolder(itemMusicBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MusicViewHolder holder, int position) {
        MusicSoundItem musicSoundItem = musicList.get(position);
        holder.bind(musicSoundItem, onItemClickListener, position, context);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        private final ItemMusicBinding itemMusicBinding;
        public MusicViewHolder(@NonNull ItemMusicBinding itemMusicBinding) {
            super(itemMusicBinding.getRoot());
            this.itemMusicBinding = itemMusicBinding;
        }

        public void bind(MusicSoundItem musicSoundItem, OnItemClickListener onItemClickListener, int position, Context context){
            try{
                itemMusicBinding.tvMusicTitle.setText(musicSoundItem.getTitle());
                int cornerRadiusPx = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        8,
                        context.getResources().getDisplayMetrics()
                );

                Glide.with(context)
                        .load(musicSoundItem.getThumbnail())
                        .apply(new RequestOptions()
                                .transform(new CenterCrop(), new RoundedCornersTransformation(cornerRadiusPx, 0)))
                        .into(itemMusicBinding.ivMusicThumbnail);

                itemMusicBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null){
                            onItemClickListener.OnItemClickListener(position);
                        }
                    }
                });
            } catch (Exception e) {
                Toast.makeText(itemMusicBinding.getRoot().getContext(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }
        }
    }
}
