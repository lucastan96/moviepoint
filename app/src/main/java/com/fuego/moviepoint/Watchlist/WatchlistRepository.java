package com.fuego.moviepoint.Watchlist;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class WatchlistRepository {
    private WatchlistDoa mWatchlistDoa;
    private LiveData<List<Watchlist>> allMovies;
    private LiveData<List<Watchlist>> viewedMovies;

    public WatchlistRepository(Application application) {
        WatchlistDatabase database = WatchlistDatabase.getInstance(application);
        mWatchlistDoa = database.watchedDoa();
        allMovies = mWatchlistDoa.getAllWatchedMovies(false);
        viewedMovies = mWatchlistDoa.getAllWatchedMovies(true);
    }

    public void insert(Watchlist movie) {
        new InsertMovieAsyncTask(mWatchlistDoa).execute(movie);
    }

    public void update(Watchlist movie) {
        new UpdateMovieAsyncTask(mWatchlistDoa).execute(movie);
    }

    public void delete(Watchlist movie) {
        new DeleteMovieAsyncTask(mWatchlistDoa).execute(movie);
    }

    public void deleteAllMovies() {
        new DeleteAllMoviesAsyncTask(mWatchlistDoa).execute();
    }

    public LiveData<List<Watchlist>> getAllMovies() {
        return allMovies;
    }

    public LiveData<List<Watchlist>> getAllViewedMovies() {
        return viewedMovies;
    }

    private static class InsertMovieAsyncTask extends AsyncTask<Watchlist, Void, Void> {
        private WatchlistDoa mWatchlistDoa;

        private InsertMovieAsyncTask(WatchlistDoa watchlistDoa) {
            this.mWatchlistDoa = watchlistDoa;
        }

        @Override
        protected Void doInBackground(Watchlist... movies) {
            mWatchlistDoa.insert(movies[0]);
            return null;
        }
    }

    private static class UpdateMovieAsyncTask extends AsyncTask<Watchlist, Void, Void> {
        private WatchlistDoa mWatchlistDoa;

        private UpdateMovieAsyncTask(WatchlistDoa watchlistDoa) {
            this.mWatchlistDoa = watchlistDoa;
        }

        @Override
        protected Void doInBackground(Watchlist... movies) {
            mWatchlistDoa.update(movies[0]);
            return null;
        }
    }

    private static class DeleteMovieAsyncTask extends AsyncTask<Watchlist, Void, Void> {
        private WatchlistDoa mWatchlistDoa;

        private DeleteMovieAsyncTask(WatchlistDoa watchlistDoa) {
            this.mWatchlistDoa = watchlistDoa;
        }

        @Override
        protected Void doInBackground(Watchlist... movies) {
            mWatchlistDoa.delete(movies[0]);
            return null;
        }
    }

    private static class DeleteAllMoviesAsyncTask extends AsyncTask<Watchlist, Void, Void> {
        private WatchlistDoa mWatchlistDoa;

        private DeleteAllMoviesAsyncTask(WatchlistDoa watchlistDoa) {
            this.mWatchlistDoa = watchlistDoa;
        }

        @Override
        protected Void doInBackground(Watchlist... movies) {
            mWatchlistDoa.deleteAllWatchedMovies();
            return null;
        }
    }
}
