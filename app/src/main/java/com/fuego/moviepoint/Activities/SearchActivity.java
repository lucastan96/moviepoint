package com.fuego.moviepoint.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.fuego.moviepoint.R;
import com.fuego.moviepoint.Search.SearchedMovieViewModel;
import com.fuego.moviepoint.Search.SearchedMovies;
import com.fuego.moviepoint.Search.SearchedMoviesAdapter;
import com.fuego.moviepoint.Utilities.NetworkUtils;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private SearchedMovieViewModel movieViewModel;
    private EditText searchField;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recycler_view);
        searchField = findViewById(R.id.search_edit_text);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 6));
        }
        recyclerView.setHasFixedSize(false);

        SearchedMoviesAdapter adapter = new SearchedMoviesAdapter();
        recyclerView.setAdapter(adapter);

        movieViewModel = ViewModelProviders.of(this).get(SearchedMovieViewModel.class);
        movieViewModel.getAllMovies().observe(this, adapter::setMovies);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    new FetchMovies(searchField.getText().toString().trim()).execute();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        adapter.setOnItemClickListener(movie -> {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_TMDBID, movie.getTmdbId());
            intent.putExtra(MovieDetailActivity.EXTRA_TITLE, movie.getTitle());
            intent.putExtra(MovieDetailActivity.EXTRA_IMAGE, movie.getImagePath());
            intent.putExtra(MovieDetailActivity.EXTRA_OVERVIEW, movie.getOverview());
            intent.putExtra(MovieDetailActivity.EXTRA_DATE, movie.getDate());
            startActivity(intent);
        });
    }

    public class FetchMovies extends AsyncTask<Void, Void, Void> {
        final private String API_KEY = "8792d844a767cde129ca36235f60093c";
        private String defaultRegion = getResources().getString(R.string.region_default);
        private String savedRegion = mPrefs.getString(getString(R.string.region), defaultRegion);
        ArrayList<SearchedMovies> searchedMovies = new ArrayList<>();
        String searchQuery;
        private String query;


        FetchMovies(String query) {
            this.query = query;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            searchQuery = " https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + query + "&page=1&include_adult=false&region=" + savedRegion;
            searchedMovies = NetworkUtils.fetchSearchData(searchQuery);
            return null;
        }

        @Override
        protected void onPreExecute() {
            movieViewModel.deleteAllMovies();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
            for (SearchedMovies m : searchedMovies) {
                movieViewModel.insert(m);
            }
        }
    }
}
