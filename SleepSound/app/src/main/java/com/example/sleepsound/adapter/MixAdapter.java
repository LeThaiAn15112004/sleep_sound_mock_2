package com.example.sleepsound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sleepsound.R;
import com.example.sleepsound.databinding.ItemMixBinding;
import com.example.sleepsound.model.Mix;
import com.example.sleepsound.model.Sound;

import java.util.List;

public class MixAdapter extends RecyclerView.Adapter<MixAdapter.MixViewHolder> {
    private Context context;
    private List<Mix> mixes;
    private OnItemClickListener onItemClickListener;

    public MixAdapter(List<Mix> mixes, Context context) {
        this.mixes = mixes;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        notifyDataSetChanged();
    }

    public List<Mix> getMixes() {
        return mixes;
    }

    public void setMixes(List<Mix> mixes) {
        this.mixes = mixes;
        notifyDataSetChanged();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MixViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMixBinding itemMixBinding = ItemMixBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MixViewHolder(itemMixBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MixAdapter.MixViewHolder holder, int position) {
        Mix mix = mixes.get(position);
        holder.bind(mix, onItemClickListener, position);
    }

    @Override
    public int getItemCount() {
        return mixes.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public static class MixViewHolder extends RecyclerView.ViewHolder {
        private final ItemMixBinding itemMixBinding;

        public MixViewHolder(@NonNull ItemMixBinding itemMixBinding) {
            super(itemMixBinding.getRoot());
            this.itemMixBinding = itemMixBinding;
        }

        public void bind(Mix mix, MixAdapter.OnItemClickListener onItemClickListener, int position) {
            try {
                String imageUrl = "file:///android_asset/images/" + mix.getCover().getThumbnail() + ".webp";
                itemMixBinding.tvMixName.setText(mix.getName());

                Glide.with(itemMixBinding.getRoot().getContext()).
                        load(imageUrl).placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(itemMixBinding.ivMixImage);
                itemMixBinding.getRoot().setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                });
            } catch (Exception e) {
                Toast.makeText(itemMixBinding.getRoot().getContext(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
                System.err.println(e);
            }
        }
    }
}
