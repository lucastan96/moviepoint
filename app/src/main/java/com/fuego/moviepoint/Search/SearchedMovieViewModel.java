package com.fuego.moviepoint.Search;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SearchedMovieViewModel extends AndroidViewModel {
    private SearchedMovieRepository repository;
    private LiveData<List<SearchedMovies>> allMovies;

    public SearchedMovieViewModel(@NonNull Application application) {
        super(application);
        repository = new SearchedMovieRepository(application);
        allMovies = repository.getAllMovies();
    }

    public void insert(SearchedMovies movie) {
        repository.insert(movie);
    }

    public void update(SearchedMovies movie) {
        repository.insert(movie);
    }

    public void delete(SearchedMovies movie) {
        repository.insert(movie);
    }

    public void deleteAllMovies() {
        repository.deleteAllMovies();
    }

    public LiveData<List<SearchedMovies>> getAllMovies() {
        return allMovies;
    }

}
