package com.fuego.moviepoint.Activities;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fuego.moviepoint.Cast.Cast;
import com.fuego.moviepoint.Movies.MovieAdapter;
import com.fuego.moviepoint.R;
import com.fuego.moviepoint.Utilities.NetworkUtils;
import com.fuego.moviepoint.Watchlist.Watchlist;
import com.fuego.moviepoint.Watchlist.WatchlistViewModal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.fuego.moviepoint.App.CHANNEL_1_ID;
import static com.fuego.moviepoint.App.CHANNEL_2_ID;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_TMDBID = "com.fuego.moviepoint.Activities.extra.TMDBID";
    public static final String EXTRA_TITLE = "com.fuego.moviepoint.Activities.extra.TITLE";
    public static final String EXTRA_IMAGE = "com.fuego.moviepoint.Activities.extra.IMAGE";
    public static final String EXTRA_OVERVIEW = "com.fuego.moviepoint.Activities.extra.OVERVIEW";
    public static final String EXTRA_DATE = "com.fuego.moviepoint.Activities.extra.DATE";

    private WatchlistViewModal mWatchlistViewModal;
    private ScrollView scrollView;
    private TextView movieReleaseDate, movieRuntime, movieStatus, movieTagline, moviePlotTitle, moviePlot, movieGenreTitle, movieGenre, movieCastTitle;
    private RecyclerView movieCast;
    private List<Cast> cast = new ArrayList<>();
    Intent intent;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mWatchlistViewModal = ViewModelProviders.of(this).get(WatchlistViewModal.class);
        notificationManager = NotificationManagerCompat.from(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean notificationOn = prefs.getBoolean(getString(R.string.notifications), true);

        intent = getIntent();

        TextView movieTitle;
        movieTitle = findViewById(R.id.movie_title);
        movieReleaseDate = findViewById(R.id.movie_release_date);
        movieRuntime = findViewById(R.id.movie_runtime);
        movieStatus = findViewById(R.id.movie_status);
        movieTagline = findViewById(R.id.movie_tagline);
        moviePlotTitle = findViewById(R.id.movie_plot_title);
        moviePlot = findViewById(R.id.movie_plot);
        movieGenreTitle = findViewById(R.id.movie_genre_title);
        movieGenre = findViewById(R.id.movie_genre);
        movieCastTitle = findViewById(R.id.movie_cast_title);
        movieCast = findViewById(R.id.movie_cast);
        movieCast.setLayoutManager(new LinearLayoutManager(this));
        movieCast.setHasFixedSize(false);
        movieCast.setNestedScrollingEnabled(false);

        ImageView imageView = findViewById(R.id.movie_poster);
        FloatingActionButton watchlistFab = findViewById(R.id.fab_watchlist);
        FloatingActionButton historyFab = findViewById(R.id.fab_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        if (intent.hasExtra(EXTRA_TITLE)) {
            new FetchMovieById(Objects.requireNonNull(intent.getExtras()).getInt(EXTRA_TMDBID)).execute();

            Objects.requireNonNull(getSupportActionBar()).setTitle(intent.getStringExtra(EXTRA_TITLE));
            movieTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            moviePlot.setText(intent.getStringExtra(EXTRA_OVERVIEW));
            setReleaseDate();

            ConstraintLayout layout = findViewById(R.id.movie_details_layout);
            Picasso.get().load(MovieAdapter.MOVIE_BASE_URL + intent.getStringExtra(EXTRA_IMAGE))
                    .placeholder(R.drawable.ic_film_placeholder)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Drawable drawable = imageView.getDrawable();
                            BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
                            Bitmap bitmap = bitmapDrawable.getBitmap();
                            Palette.from(bitmap).maximumColorCount(16).generate(palette -> {
                                if (palette != null) {
                                    Palette.Swatch darkMuted = palette.getDarkMutedSwatch();
                                    if (darkMuted != null) {
                                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(darkMuted.getRgb()));
                                        layout.setBackgroundColor(darkMuted.getRgb());
                                        getWindow().setStatusBarColor(darkMuted.getRgb());
                                    }
                                    Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
                                    if (lightVibrant != null) {
                                        watchlistFab.setBackgroundTintList(ColorStateList.valueOf(lightVibrant.getRgb()));
                                        historyFab.setBackgroundTintList(ColorStateList.valueOf(lightVibrant.getRgb()));
                                        movieStatus.setBackgroundColor(lightVibrant.getRgb());
                                    }
                                }
                            });
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        }

        scrollView = findViewById(R.id.movie_details);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = scrollView.getScrollY();
            if (scrollY > 0) {
                toolbar.setElevation(20);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
            } else {
                toolbar.setElevation(0);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        });

        watchlistFab.setOnClickListener(v -> {
            Watchlist watchlist = new Watchlist();
            watchlist.setTmdbId(intent.getExtras().getInt(EXTRA_TMDBID));
            watchlist.setTitle(intent.getStringExtra(EXTRA_TITLE));
            watchlist.setOverview(intent.getStringExtra(EXTRA_OVERVIEW));
            watchlist.setImagePath(intent.getStringExtra(EXTRA_IMAGE));
            watchlist.setDate(intent.getStringExtra(EXTRA_DATE));
            watchlist.setWatched(false);
            mWatchlistViewModal.insert(watchlist);
            if (notificationOn) {
                watchlistNotification();
            } else {
                Toast.makeText(this, "Added to watchlist", Toast.LENGTH_SHORT).show();
            }
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
            if (notificationOn) {
                watchedNotification();
            } else {
                Toast.makeText(this, "Added to watch history", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setReleaseDate() {
        String releaseDate = intent.getStringExtra(EXTRA_DATE);
        if (releaseDate.length() != 0) {
            int year = Integer.parseInt(intent.getStringExtra(EXTRA_DATE).substring(0, 4));
            String month = new DateFormatSymbols().getMonths()[Integer.parseInt(intent.getStringExtra(EXTRA_DATE).substring(5, 7)) - 1];
            int day = Integer.parseInt(intent.getStringExtra(EXTRA_DATE).substring(8, 10));
            movieReleaseDate.setText(new StringBuilder().append(month).append(" ").append(day).append(", ").append(year).toString());
        } else {
            movieReleaseDate.setText(getString(R.string.movie_release_tba));
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
        String url, castUrl;
        private int tmdbId;

        JSONObject movieDetails;

        private int runtime;
        private String status, tagline, genre = "";
        private StringBuilder stringBuilder;
        private JSONArray genreArray;

        FetchMovieById(int tmdbId) {
            this.tmdbId = tmdbId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            url = "https://api.themoviedb.org/3/movie/" + tmdbId + "?api_key=" + API_KEY + "&language=en-US";
            castUrl = "https://api.themoviedb.org/3/movie/" + tmdbId + "/credits?api_key=" + API_KEY + "&language=en-US";
            movieDetails = NetworkUtils.fetchMovieDetails(url);
            cast = NetworkUtils.fetchCastData(castUrl);
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

                stringBuilder = new StringBuilder();
                for (int i = 0; i < genreArray.length(); i++) {
                    stringBuilder.append(genreArray.getJSONObject(i).getString("name"));
                    if (i + 1 != genreArray.length()) {
                        stringBuilder.append(", ");
                    }
                }
                genre = stringBuilder.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (runtime != 0) {
                movieRuntime.setVisibility(View.VISIBLE);
                movieRuntime.setText(new StringBuilder().append(runtime).append(" Minutes").toString());
            }
            if (status != null) {
                movieStatus.setVisibility(View.VISIBLE);
                movieStatus.setText(status);
            }
            if (intent.getStringExtra(EXTRA_OVERVIEW).equals("")) {
                moviePlotTitle.setVisibility(View.GONE);
                moviePlot.setVisibility(View.GONE);
            } else {
                if (tagline != null) {
                    if (!tagline.equals("")) {
                        movieTagline.setVisibility(View.VISIBLE);
                        movieTagline.setText(tagline);
                    }
                }
            }
            if (genre.length() != 0) {
                movieGenreTitle.setVisibility(View.VISIBLE);
                movieGenre.setVisibility(View.VISIBLE);
                movieGenre.setText(genre);
            }
            Log.d("cast", "onPostExecute: " + cast.size());
//            if (cast.size() != 0) {
//                movieCastTitle.setVisibility(View.VISIBLE);
//                movieCast.setVisibility(View.VISIBLE);
//                CastAdapter castAdapter = new CastAdapter(cast);
//                movieCast.setAdapter(castAdapter);
//            }
        }
    }
}
