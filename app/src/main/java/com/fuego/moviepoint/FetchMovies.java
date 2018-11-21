package com.fuego.moviepoint;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fuego.moviepoint.utilities.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

public class FetchMovies extends AsyncTask<Void, Void, Void> {

    final private String API_KEY = "8792d844a767cde129ca36235f60093c";

    String popularMovies;
    ArrayList<Movie> mPopularList;
    MovieRepository repository;

    @Override
    protected Void doInBackground(Void... voids) {

        popularMovies = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + API_KEY;

        mPopularList = new ArrayList<>();

        try {
            if (NetworkUtils.networkStatus(new Application())) {
                mPopularList = NetworkUtils.fetchData(popularMovies); //Get popular movies
            } else {
                Toast.makeText(new Application(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        for (Movie m : mPopularList) {
            repository.insert(m);
        }
        super.onPostExecute(aVoid);
    }
}
