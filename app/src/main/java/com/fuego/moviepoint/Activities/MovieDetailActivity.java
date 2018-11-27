package com.fuego.moviepoint.Activities;

import android.app.Notification;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuego.moviepoint.Movies.MovieAdapter;
import com.fuego.moviepoint.R;
import com.fuego.moviepoint.Utilities.NetworkUtils;
import com.fuego.moviepoint.Watchlist.Watchlist;
import com.fuego.moviepoint.Watchlist.WatchlistViewModal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;

import static com.fuego.moviepoint.App.CHANNEL_1_ID;
import static com.fuego.moviepoint.App.CHANNEL_2_ID;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    public static final String EXTRA_TMDBID = "com.fuego.moviepoint.Activities.extra.TMDBID";
    public static final String EXTRA_TITLE = "com.fuego.moviepoint.Activities.extra.TITLE";
    public static final String EXTRA_IMAGE = "com.fuego.moviepoint.Activities.extra.IMAGE";
    public static final String EXTRA_OVERVIEW = "com.fuego.moviepoint.Activities.extra.OVERVIEW";
    public static final String EXTRA_DATE = "com.fuego.moviepoint.Activities.extra.DATE";

    private WatchlistViewModal mWatchlistViewModal;
    private FloatingActionButton watchlistFab, historyFab;
    private TextView movieTitle, movieReleaseDate, movieRuntime, movieStatus, movieTagline, moviePlot, movieGenreTitle, movieGenre;
    private ImageView imageView;
    Intent intent;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mWatchlistViewModal = ViewModelProviders.of(this).get(WatchlistViewModal.class);
        notificationManager = NotificationManagerCompat.from(this);

        intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);

        movieTitle = findViewById(R.id.movie_title);
        movieReleaseDate = findViewById(R.id.movie_release_date);
        movieRuntime = findViewById(R.id.movie_runtime);
        movieStatus = findViewById(R.id.movie_status);
        movieTagline = findViewById(R.id.movie_tagline);
        moviePlot = findViewById(R.id.movie_plot);
        movieGenreTitle = findViewById(R.id.movie_genre_title);
        movieGenre = findViewById(R.id.movie_genre);

        imageView = findViewById(R.id.movie_poster);
        watchlistFab = findViewById(R.id.fab_watchlist);
        historyFab = findViewById(R.id.fab_history);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        if (intent.hasExtra(EXTRA_TITLE)) {
            new FetchMovieById(intent.getExtras().getInt(EXTRA_TMDBID)).execute();

            Objects.requireNonNull(getSupportActionBar()).setTitle("");
            movieTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            moviePlot.setText(intent.getStringExtra(EXTRA_OVERVIEW));
            setReleaseDate();

            Picasso.get().load(MovieAdapter.MOVIE_BASE_URL + intent.getStringExtra(EXTRA_IMAGE))
                    .placeholder(R.drawable.ic_film_placeholder)
                    .into(imageView);
        }

        watchlistFab.setOnClickListener(v -> {
            Watchlist watchlist = new Watchlist();
            watchlist.setTmdbId(intent.getExtras().getInt(EXTRA_TMDBID));
            watchlist.setTitle(intent.getStringExtra(EXTRA_TITLE));
            watchlist.setOverview(intent.getStringExtra(EXTRA_OVERVIEW));
            watchlist.setImagePath(intent.getStringExtra(EXTRA_IMAGE));
            watchlist.setDate(intent.getStringExtra(EXTRA_DATE));
            watchlist.setWatched(false);
            mWatchlistViewModal.insert(watchlist);
            watchlistNotification();
        });

        historyFab.setOnClickListener(v -> {
            Watchlist watchlist = new Watchlist();
            watchlist.setTmdbId(intent.getExtras().getInt(EXTRA_TMDBID));
            watchlist.setTitle(intent.getStringExtra(EXTRA_TITLE));
            watchlist.setOverview(intent.getStringExtra(EXTRA_OVERVIEW));
            watchlist.setImagePath(intent.getStringExtra(EXTRA_IMAGE));
            watchlist.setDate(intent.getStringExtra(EXTRA_DATE));
            watchlist.setWatched(true);
            mWatchlistViewModal.insert(watchlist);
            watchedNotification();
        });
    }

    private void setReleaseDate() {
        String releaseDate = intent.getStringExtra(EXTRA_DATE);
        if (releaseDate.length() != 0) {
            int year = Integer.parseInt(intent.getStringExtra(EXTRA_DATE).substring(0, 4));
            String month = new DateFormatSymbols().getMonths()[Integer.parseInt(intent.getStringExtra(EXTRA_DATE).substring(5, 7)) - 1];
            int day = Integer.parseInt(intent.getStringExtra(EXTRA_DATE).substring(8, 10));
            movieReleaseDate.setText(month + " " + day + ", " + year);
        } else {
            movieReleaseDate.setText("Release Date TBA");
        }
    }

    public void watchlistNotification() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_film)
                .setContentTitle("Added new movie to watchlist")
                .setContentText(intent.getStringExtra(EXTRA_TITLE))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }

    public void watchedNotification() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_film)
                .setContentTitle("Added new movie to history")
                .setContentText(intent.getStringExtra(EXTRA_TITLE))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManager.notify(2, notification);
    }

    public class FetchMovieById extends AsyncTask<Void, Void, Void> {
        final private String API_KEY = "8792d844a767cde129ca36235f60093c";
        String url;
        private int tmdbId;

        JSONObject movieDetails;

        private int runtime;
        private String status, tagline, genre = "";
        private JSONArray genreArray;

        public FetchMovieById(int tmdbId) {
            this.tmdbId = tmdbId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            url = "https://api.themoviedb.org/3/movie/" + tmdbId + "?api_key=" + API_KEY + "&language=en-US";
            movieDetails = NetworkUtils.fetchMovieDetails(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
            try {
                runtime = movieDetails.getInt("runtime");
                status = movieDetails.getString("status");
                tagline = movieDetails.getString("tagline");
                genreArray = movieDetails.getJSONArray("genres");
                for (int i = 0; i < genreArray.length(); i++) {
                    genre += genreArray.getJSONObject(i).getString("name");
                    if (i + 1 != genreArray.length()) {
                        genre += ", ";
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (runtime != 0) {
                movieRuntime.setVisibility(View.VISIBLE);
                movieRuntime.setText(runtime + " Minutes");
            }
            if (status != null) {
                movieStatus.setVisibility(View.VISIBLE);
                movieStatus.setText(status);
            }
            if (tagline != null) {
                movieTagline.setVisibility(View.VISIBLE);
                movieTagline.setText(tagline);
            }
            if (genre.length() != 0) {
                movieGenreTitle.setVisibility(View.VISIBLE);
                movieGenre.setVisibility(View.VISIBLE);
                movieGenre.setText(genre);
            }
        }
    }
}
