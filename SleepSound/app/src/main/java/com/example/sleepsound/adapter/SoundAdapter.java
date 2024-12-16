package com.example.sleepsound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sleepsound.R;
import com.example.sleepsound.model.Sound;

import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundViewHolder> {
    private Context context;
    private List<Sound> soundList;

    public SoundAdapter(Context context, List<Sound> soundList) {
        this.context = context;
        this.soundList = soundList;
    }

    @NonNull
    @Override
    public SoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ambience, parent, false);
        return new SoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundViewHolder holder, int position) {
        Sound sound = soundList.get(position);
        try {
            // Loại bỏ phần mở rộng nếu có
            String resourceName = sound.getIcon().replace(".svg", "");

            // Lấy ID của tài nguyên drawable
            int resId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());

            // Kiểm tra và đặt ảnh
            if (resId != 0) {
                holder.imageBg.setImageResource(resId);
            } else {
                // Hình ảnh mặc định khi không tìm thấy tài nguyên
                holder.imageBg.setImageResource(R.drawable.ic_launcher_foreground);
            }

            // Đặt tên âm thanh
            holder.soundName.setText(sound.getName());
        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            System.err.println(e);
        }
    }

    @Override
    public int getItemCount() {
        return soundList.size();
    }

    public static class SoundViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBg;
        TextView soundName;

        public SoundViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBg = itemView.findViewById(R.id.image_bg_ambience);
            soundName = itemView.findViewById(R.id.text_sound_name);
        }
    }
}
