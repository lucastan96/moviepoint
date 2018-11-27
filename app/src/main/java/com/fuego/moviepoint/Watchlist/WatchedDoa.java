package com.fuego.moviepoint.Watchlist;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WatchedDoa {

    @Insert
    void insert(Watched movie);

    @Update
    void update(Watched movie);

    @Delete
    void delete(Watched movie);

    @Query("DELETE FROM watched_table")
    void deleteAllWatchedMovies();

    @Query("SELECT * FROM watched_table WHERE watched = :viewed")
    LiveData<List<Watched>> getAllWatchedMovies(Boolean viewed);
}
