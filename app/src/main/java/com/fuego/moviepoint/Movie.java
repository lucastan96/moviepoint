package com.fuego.moviepoint;

import java.util.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String imagePath;

    public Movie() {
    }

    public Movie(String title, String imagePath) {
        this.title = title;
        this.imagePath = imagePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return getId() == movie.getId() &&
                Objects.equals(getTitle(), movie.getTitle()) &&
                Objects.equals(getImagePath(), movie.getImagePath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getImagePath());
    }
}
