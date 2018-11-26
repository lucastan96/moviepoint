package com.fuego.moviepoint.Watchlist;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class WatchedViewModal extends AndroidViewModel {

    private WatchedRepository repository;
    private LiveData<List<Watched>> allMovies;
    private LiveData<List<Watched>> viewedMovies;

    public WatchedViewModal(@NonNull Application application) {
        super(application);
        repository = new WatchedRepository(application);
        allMovies = repository.getAllMovies();
        viewedMovies = repository.getAllViewedMovies();
    }

    public void insert(Watched movie) {
        repository.insert(movie);
    }

    public void update(Watched movie) {
        repository.insert(movie);
    }

    public void delete(Watched movie) {
        repository.insert(movie);
    }

    public void deleteAllMovies() {
        repository.deleteAllMovies();
    }

    public LiveData<List<Watched>> getAllMovies() {
        return allMovies;
    }

    public LiveData<List<Watched>> getAllWatchedMovies() {
        return viewedMovies;
    }
}
