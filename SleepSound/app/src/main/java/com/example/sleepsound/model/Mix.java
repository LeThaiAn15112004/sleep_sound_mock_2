package com.example.sleepsound.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Mix {
    private int mixSoundId;
    private int category;
    private String name;
    private Cover cover;
    private List<Sound> sounds;

    public Mix(int mixSoundId, int category, String name, Cover cover, List<Sound> sounds) {
        this.mixSoundId = mixSoundId;
        this.category = category;
        this.name = name;
        this.cover = cover;
        this.sounds = sounds;
    }

    @Override
    public String toString() {
        return "Mix{" +
                "mixSoundId=" + mixSoundId +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", cover=" + cover +
                ", sounds=" + sounds +
                '}';
    }

    public int getMixSoundId() {
        return mixSoundId;
    }

    public void setMixSoundId(int mixSoundId) {
        this.mixSoundId = mixSoundId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
    }

    // Class con `Cover`
    public static class Cover {
        private String thumbnail;
        private String background;

        public Cover(String thumbnail, String background) {
            this.thumbnail = thumbnail;
            this.background = background;
        }

        @Override
        public String toString() {
            return "Cover{" +
                    "thumbnail='" + thumbnail + '\'' +
                    ", background='" + background + '\'' +
                    '}';
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }
    }

    // Class con `Volume`
    public static class Sound implements Parcelable {
        private int id;
        private int volume;

        public Sound(int id, int volume) {
            this.id = id;
            this.volume = volume;
        }

        @Override
        public String toString() {
            return "Sound{" +
                    "id=" + id +
                    ", volume=" + volume +
                    '}';
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        protected Sound(Parcel in) {
            id = in.readInt();
            volume = in.readInt();
        }

        public static final Creator<Sound> CREATOR = new Creator<Sound>() {
            @Override
            public Sound createFromParcel(Parcel in) {
                return new Sound(in);
            }

            @Override
            public Sound[] newArray(int size) {
                return new Sound[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(volume);
        }
    }
}
