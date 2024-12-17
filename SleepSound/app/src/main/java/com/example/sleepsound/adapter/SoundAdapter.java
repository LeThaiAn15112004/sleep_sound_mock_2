package com.example.sleepsound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sleepsound.R;
import com.example.sleepsound.databinding.ItemAmbienceBinding;
import com.example.sleepsound.model.Sound;

import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundViewHolder> {
    private Context context;
    private List<Sound> soundList;
    private OnItemClickListener onItemClickListener;

    public SoundAdapter(Context context, List<Sound> soundList) {
        this.context = context;
        this.soundList = soundList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Sound> getSoundList() {
        return soundList;
    }

    public void setSoundList(List<Sound> soundList) {
        this.soundList = soundList;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    @NonNull
    @Override
    public SoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAmbienceBinding binding = ItemAmbienceBinding.inflate(LayoutInflater.from(context), parent, false);
        return new SoundViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundViewHolder holder, int position) {
        Sound sound = soundList.get(position);
        holder.bind(sound, onItemClickListener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return soundList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public static class SoundViewHolder extends RecyclerView.ViewHolder {
        private final ItemAmbienceBinding binding;

        public SoundViewHolder(@NonNull ItemAmbienceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Sound sound, OnItemClickListener onItemClickListener) {
            try {
                // Set icon image
                String resourceName = sound.getIcon().replace(".svg", "");
                int resId = binding.getRoot().getContext().getResources()
                        .getIdentifier(resourceName, "drawable", binding.getRoot().getContext().getPackageName());
                if (resId != 0) {
                    binding.imageBgAmbience.setImageResource(resId);
                } else {
                    binding.imageBgAmbience.setImageResource(R.drawable.ic_launcher_foreground);
                }

                if (sound.isPlaying()) {
                    binding.imageBgAmbience.setBackgroundResource(R.color.purple);
                } else {
                    binding.imageBgAmbience.setBackgroundResource(R.color.blue_dark);
                }

                // Set sound name
                binding.textSoundName.setText(sound.getName());

                // Set click listener
                binding.getRoot().setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                });
            } catch (Exception e) {
                Toast.makeText(binding.getRoot().getContext(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
                System.err.println(e);
            }
        }
    }
}