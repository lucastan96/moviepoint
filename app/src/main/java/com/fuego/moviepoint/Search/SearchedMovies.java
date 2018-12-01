package com.fuego.moviepoint.Search;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "search_table")

public class SearchedMovies {

    @PrimaryKey()
    private int tmdbId;

    private String title;
    private String imagePath;
    private String overview;
    private String date;

    public SearchedMovies() {
    }

    public SearchedMovies(int tmdbId, String title, String imagePath, String overview, String date) {
        this.tmdbId = tmdbId;
        this.title = title;
        this.imagePath = imagePath;
        this.overview = overview;
        this.date = date;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
