package com.fuego.moviepoint.Watchlist;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class WatchlistViewModal extends AndroidViewModel {

    private WatchlistRepository repository;
    private LiveData<List<Watchlist>> allMovies;
    private LiveData<List<Watchlist>> viewedMovies;

    public WatchlistViewModal(@NonNull Application application) {
        super(application);
        repository = new WatchlistRepository(application);
        allMovies = repository.getAllMovies();
        viewedMovies = repository.getAllViewedMovies();
    }

    public void insert(Watchlist movie) {
        repository.insert(movie);
    }

    public void update(Watchlist movie) {
        repository.insert(movie);
    }

    public void delete(Watchlist movie) {
        repository.insert(movie);
    }

    public void deleteAllMovies() {
        repository.deleteAllMovies();
    }

    public LiveData<List<Watchlist>> getAllMovies() {
        return allMovies;
    }

    public LiveData<List<Watchlist>> getAllWatchedMovies() {
        return viewedMovies;
    }
}
