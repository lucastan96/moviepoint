package com.fuego.moviepoint.Watchlist;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WatchlistDoa {

    @Insert
    void insert(Watchlist movie);

    @Update
    void update(Watchlist movie);

    @Delete
    void delete(Watchlist movie);

    @Query("DELETE FROM Watchlist")
    void deleteAllWatchedMovies();

    @Query("SELECT * FROM Watchlist WHERE watched = :viewed")
    LiveData<List<Watchlist>> getAllWatchedMovies(Boolean viewed);
}
