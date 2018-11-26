package com.fuego.moviepoint.Search;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class SearchedMovieRepository {

    private SearchedMoviesDao movieDao;
    private LiveData<List<SearchedMovies>> allMovies;

    public SearchedMovieRepository(Application application) {
        SearchedMoviesDatabase database = SearchedMoviesDatabase.getInstance(application);
        movieDao = database.movieDao();
        allMovies = movieDao.getAllMovies();
    }

    public void insert(SearchedMovies movie) {
        new SearchedMovieRepository.InsertMovieAsyncTask(movieDao).execute(movie);
    }

    public void update(SearchedMovies movie) {
        new SearchedMovieRepository.UpdateMovieAsyncTask(movieDao).execute(movie);
    }

    public void delete(SearchedMovies movie) {
        new SearchedMovieRepository.DeleteMovieAsyncTask(movieDao).execute(movie);
    }

    public void deleteAllMovies() {
        new SearchedMovieRepository.DeleteAllMoviesAsyncTask(movieDao).execute();
    }

    public LiveData<List<SearchedMovies>> getAllMovies() {
        return allMovies;
    }

    private static class InsertMovieAsyncTask extends AsyncTask<SearchedMovies, Void, Void> {
        private SearchedMoviesDao movieDao;

        private InsertMovieAsyncTask(SearchedMoviesDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(SearchedMovies... movies) {
            movieDao.insert(movies[0]);
            return null;
        }
    }

    private static class UpdateMovieAsyncTask extends AsyncTask<SearchedMovies, Void, Void> {
        private SearchedMoviesDao movieDao;

        private UpdateMovieAsyncTask(SearchedMoviesDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(SearchedMovies... movies) {
            movieDao.update(movies[0]);
            return null;
        }
    }

    private static class DeleteMovieAsyncTask extends AsyncTask<SearchedMovies, Void, Void> {
        private SearchedMoviesDao movieDao;

        private DeleteMovieAsyncTask(SearchedMoviesDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(SearchedMovies... movies) {
            movieDao.delete(movies[0]);
            return null;
        }
    }

    private static class DeleteAllMoviesAsyncTask extends AsyncTask<SearchedMovies, Void, Void> {
        private SearchedMoviesDao movieDao;

        private DeleteAllMoviesAsyncTask(SearchedMoviesDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(SearchedMovies... movies) {
            movieDao.deleteAllMovies();
            return null;
        }
    }
}
