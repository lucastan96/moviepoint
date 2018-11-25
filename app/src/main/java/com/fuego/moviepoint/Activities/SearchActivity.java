package com.fuego.moviepoint.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.fuego.moviepoint.Movies.Movie;
import com.fuego.moviepoint.Movies.MovieAdapter;
import com.fuego.moviepoint.Movies.MovieViewModel;
import com.fuego.moviepoint.R;
import com.fuego.moviepoint.Utilities.NetworkUtils;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private MovieViewModel movieViewModel;
    private EditText searchField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        recyclerView = findViewById(R.id.recycler_view);
        searchField = findViewById(R.id.search_edit_text);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(false);

        MovieAdapter adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getAllMovies().observe(this, adapter::setMovies);
        movieViewModel.deleteAllMovies();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(v -> {
            movieViewModel.deleteAllMovies();
            finish();
        });

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    new FetchMovies(searchField.getText().toString()).execute();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        adapter.setOnItemClickListener(movie -> {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_TITLE, movie.getTitle());
            intent.putExtra(MovieDetailActivity.EXTRA_IMAGE, movie.getImagePath());
            intent.putExtra(MovieDetailActivity.EXTRA_OVERVIEW, movie.getOverview());
            startActivity(intent);
        });
    }

    public class FetchMovies extends AsyncTask<Void, Void, Void> {
        final private String API_KEY = "8792d844a767cde129ca36235f60093c";
        ArrayList<Movie> searchedMovies = new ArrayList<>();
        String searchQuery;
        private String query;

        public FetchMovies(String query) {
            this.query = query;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            searchQuery = " https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + query + "&page=1&include_adult=false";
            searchedMovies = new ArrayList<>();
            searchedMovies = NetworkUtils.fetchData(searchQuery);
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
            for (Movie m : searchedMovies) {
                movieViewModel.insert(m);
            }
        }
    }
}
