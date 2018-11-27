package com.fuego.moviepoint.Movies;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int tmdbId;
    private String title;
    private String imagePath;
    private String overview;
    private String date;
    private Boolean adult;

    public Movie(int id, int tmdbId, String title, String imagePath, String overview, String date, Boolean adult) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.title = title;
        this.imagePath = imagePath;
        this.overview = overview;
        this.date = date;
        this.adult = adult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie() {
    }

    public int getTmdbId() {
        return tmdbId;
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

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }
}
