package com.fuego.moviepoint.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.fuego.moviepoint.Fragment.HistoryFragment;
import com.fuego.moviepoint.Fragment.TheaterFragment;
import com.fuego.moviepoint.Fragment.WatchlistFragment;
import com.fuego.moviepoint.Movie;
import com.fuego.moviepoint.MovieViewModel;
import com.fuego.moviepoint.R;
import com.fuego.moviepoint.utilities.NetworkUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private MovieViewModel movieViewModel;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        progressBar = findViewById(R.id.progressBar);

        BottomNavigationView navigation = findViewById(R.id.nav_bottom);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new TheaterFragment());
        new FetchMovies().execute();
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_bottom_1:
                fragment = new TheaterFragment();
                break;
            case R.id.nav_bottom_2:
                fragment = new WatchlistFragment();
                break;
            case R.id.nav_bottom_3:
                fragment = new HistoryFragment();
                break;
        }
        return loadFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_action, menu);
        return true;
    }

    public void openSettings(MenuItem menuItem) {
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
    }

    public class FetchMovies extends AsyncTask<Void, Void, Void> {
        final private String API_KEY = "8792d844a767cde129ca36235f60093c";

        String popularMovies;
        ArrayList<Movie> mPopularList;

        @Override
        protected Void doInBackground(Void... voids) {
            popularMovies = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + API_KEY;
            mPopularList = new ArrayList<>();
            mPopularList = NetworkUtils.fetchData(popularMovies);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
            for (Movie m : mPopularList) {
                movieViewModel.insert(m);
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}


