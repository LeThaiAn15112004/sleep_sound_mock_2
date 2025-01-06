package com.example.sleepsound.model;

public class MusicSoundItem {
    private int id;
    private String title;
    private boolean premium;
    private String group;  // Add group field for genre
    private String url;
    private String thumbnail;
    private String background;
    private String badge;

    // Constructor mặc định (dành cho Gson)
    public MusicSoundItem() {}

    // Constructor tùy chỉnh cho Recently Played
    public MusicSoundItem(String title, String url, String thumbnail) {
        this.title = title;
        this.url = url;
        this.thumbnail = thumbnail;
    }

    // Constructor with group (used for filtering by genre)
    public MusicSoundItem(String title, String url, String thumbnail, String group) {
        this.title = title;
        this.url = url;
        this.thumbnail = thumbnail;
        this.group = group;  // Initialize group (genre)
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public String getBackground() {
        return background;
    }

    public String getBadge() {
        return badge;
    }

    public String getGroup() {
        return group;  // Getter for group
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setGroup(String group) {
        this.group = group;  // Setter for group
    }

    // Override equals to check for duplicates in Recently Played
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MusicSoundItem that = (MusicSoundItem) obj;
        return title.equals(that.title) && url.equals(that.url) && thumbnail.equals(that.thumbnail);
    }
}
