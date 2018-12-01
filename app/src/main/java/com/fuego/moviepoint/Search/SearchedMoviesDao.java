package com.fuego.moviepoint.Search;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SearchedMoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchedMovies movie);

    @Update
    void update(SearchedMovies movie);

    @Delete
    void delete(SearchedMovies movie);

    @Query("DELETE FROM search_table")
    void deleteAllMovies();

    @Query("SELECT * FROM search_table")
    LiveData<List<SearchedMovies>> getAllMovies();
}
