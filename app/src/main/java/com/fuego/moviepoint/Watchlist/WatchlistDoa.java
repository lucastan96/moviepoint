package com.fuego.moviepoint.Watchlist;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WatchlistDoa {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Watchlist movie);

    @Update
    void update(Watchlist movie);

    @Delete
    void delete(Watchlist movie);

    @Query("DELETE FROM watchlist_table")
    void deleteAllWatchedMovies();

    @Query("SELECT * FROM watchlist_table WHERE watched = :viewed")
    LiveData<List<Watchlist>> getAllWatchedMovies(Boolean viewed);
}
