package com.example.sleepsound.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sleepsound.model.Relax;

import java.util.List;

public class RelaxAdapter extends RecyclerView.Adapter<RelaxAdapter.RelaxViewHolder>{
    private Context context;
    private List<Relax> relaxes;
    private OnItemClickListener onItemClickListener;

    public RelaxAdapter(List<Relax> relaxes, Context context) {
        this.relaxes = relaxes;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Relax> getRelaxes() {
        return relaxes;
    }

    public void setRelaxes(List<Relax> relaxes) {
        this.relaxes = relaxes;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RelaxAdapter.RelaxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RelaxAdapter.RelaxViewHolder holder, int position) {

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
