package com.fuego.moviepoint.Watchlist;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "watched_table")
public class Watched {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String imagePath;
    private String overview;
    private Boolean watched;

    public Watched() {
    }

    public Watched(int id, String title, String imagePath, String overview, Boolean watched) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.overview = overview;
        this.watched = watched;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Boolean getWatched() {
        return watched;
    }

    public void setWatched(Boolean watched) {
        this.watched = watched;
    }
}
