package com.fuego.moviepoint.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.fuego.moviepoint.Fragment.HistoryFragment;
import com.fuego.moviepoint.Fragment.TheaterFragment;
import com.fuego.moviepoint.Fragment.WatchlistFragment;
import com.fuego.moviepoint.Movies.Movie;
import com.fuego.moviepoint.Movies.MovieViewModel;
import com.fuego.moviepoint.R;
import com.fuego.moviepoint.Utilities.NetworkUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private MovieViewModel movieViewModel;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout = findViewById(R.id.swipeContainer);

        BottomNavigationView navigation = findViewById(R.id.nav_bottom);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new TheaterFragment());

        movieViewModel.deleteAllMovies();
        new FetchMovies().execute();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            swipeRefreshLayout.setRefreshing(true);
            if (currentFragment instanceof TheaterFragment) {
                movieViewModel.deleteAllMovies();
                new FetchMovies().execute();
            } else if (currentFragment instanceof WatchlistFragment) {

            } else if (currentFragment instanceof HistoryFragment) {

            }
            swipeRefreshLayout.setRefreshing(false);
        });
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

    public void openSearch(MenuItem menuItem) {
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(i);
    }

    public class FetchMovies extends AsyncTask<Void, Void, Void> {
        final private String API_KEY = String.valueOf(R.string.api_key);
        private String savedRegion = mPrefs.getString(getString(R.string.region), getResources().getString(R.string.default_region));

        String popularMovies;
        ArrayList<Movie> mPopularList;

        @Override
        protected Void doInBackground(Void... voids) {
            popularMovies = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + API_KEY + "&language=en-US&page=1&region=" + savedRegion;
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