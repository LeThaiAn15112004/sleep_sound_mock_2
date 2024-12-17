package com.example.sleepsound.model;

public class Music {
    private int id;
    private String title;
    private boolean premium;
    private String group;
    private String url;
    private String thumbnail;
    private String background;
    private String badge;

    public Music(int id, String title, boolean premium, String group, String url, String thumbnail, String background, String badge) {
        this.id = id;
        this.title = title;
        this.premium = premium;
        this.group = group;
        this.url = url;
        this.thumbnail = thumbnail;
        this.background = background;
        this.badge = badge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", premium=" + premium +
                ", group='" + group + '\'' +
                ", url='" + url + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", background='" + background + '\'' +
                ", badge='" + badge + '\'' +
                '}';
    }
}
