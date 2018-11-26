package com.fuego.moviepoint.Watchlist;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class WatchedRepository {
    private WatchedDoa watchedDoa;
    private LiveData<List<Watched>> allMovies;
    private LiveData<List<Watched>> viewedMovies;

    public WatchedRepository(Application application) {
        WatchedDatabase database = WatchedDatabase.getInstance(application);
        watchedDoa = database.watchedDoa();
        allMovies = watchedDoa.getAllWatchedMovies(false);
        viewedMovies = watchedDoa.getAllWatchedMovies(true);
    }

    public void insert(Watched movie) {
        new InsertMovieAsyncTask(watchedDoa).execute(movie);
    }

    public void update(Watched movie) {
        new UpdateMovieAsyncTask(watchedDoa).execute(movie);
    }

    public void delete(Watched movie) {
        new DeleteMovieAsyncTask(watchedDoa).execute(movie);
    }

    public void deleteAllMovies() {
        new DeleteAllMoviesAsyncTask(watchedDoa).execute();
    }

    public LiveData<List<Watched>> getAllMovies() {
        return allMovies;
    }

    public LiveData<List<Watched>> getAllViewedMovies() {
        return viewedMovies;
    }

    private static class InsertMovieAsyncTask extends AsyncTask<Watched, Void, Void> {
        private WatchedDoa watchedDoa;

        private InsertMovieAsyncTask(WatchedDoa watchedDoa) {
            this.watchedDoa = watchedDoa;
        }

        @Override
        protected Void doInBackground(Watched... movies) {
            watchedDoa.insert(movies[0]);
            return null;
        }
    }

    private static class UpdateMovieAsyncTask extends AsyncTask<Watched, Void, Void> {
        private WatchedDoa watchedDoa;

        private UpdateMovieAsyncTask(WatchedDoa watchedDoa) {
            this.watchedDoa = watchedDoa;
        }

        @Override
        protected Void doInBackground(Watched... movies) {
            watchedDoa.update(movies[0]);
            return null;
        }
    }

    private static class DeleteMovieAsyncTask extends AsyncTask<Watched, Void, Void> {
        private WatchedDoa watchedDoa;

        private DeleteMovieAsyncTask(WatchedDoa watchedDoa) {
            this.watchedDoa = watchedDoa;
        }

        @Override
        protected Void doInBackground(Watched... movies) {
            watchedDoa.delete(movies[0]);
            return null;
        }
    }

    private static class DeleteAllMoviesAsyncTask extends AsyncTask<Watched, Void, Void> {
        private WatchedDoa watchedDoa;

        private DeleteAllMoviesAsyncTask(WatchedDoa watchedDoa) {
            this.watchedDoa = watchedDoa;
        }

        @Override
        protected Void doInBackground(Watched... movies) {
            watchedDoa.deleteAllWatchedMovies();
            return null;
        }
    }
}
