package com.fuego.moviepoint;

import android.os.AsyncTask;
import android.util.Log;

import com.fuego.moviepoint.utilities.NetworkUtils;

import java.util.ArrayList;

public class FetchMovies extends AsyncTask<Void, Void, Void> {

    private static final String TAG = FetchMovies.class.getSimpleName();

    final private String API_KEY = "8792d844a767cde129ca36235f60093c";

    String popularMovies;
    ArrayList<Movie> mPopularList;

    @Override
    protected Void doInBackground(Void... voids) {
        popularMovies = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + API_KEY;
        mPopularList = new ArrayList<>();

        mPopularList = NetworkUtils.fetchData(popularMovies);
        Log.d(TAG, "doInBackground: Getting Movies ");

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}
