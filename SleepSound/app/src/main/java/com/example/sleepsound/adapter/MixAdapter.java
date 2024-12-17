package com.example.sleepsound.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sleepsound.model.Mix;

import java.util.List;

public class MixAdapter extends RecyclerView.Adapter<MixAdapter.RelaxViewHolder>{
    private Context context;
    private List<Mix> mixs;
    private OnItemClickListener onItemClickListener;

    public MixAdapter(List<Mix> mixs, Context context) {
        this.mixs = mixs;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Mix> getMixs() {
        return mixs;
    }

    public void setMixs(List<Mix> mixs) {
        this.mixs = mixs;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MixAdapter.RelaxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MixAdapter.RelaxViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public static class RelaxViewHolder extends RecyclerView.ViewHolder {

        public RelaxViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
