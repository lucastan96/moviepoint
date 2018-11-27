package com.fuego.moviepoint.Activities;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuego.moviepoint.Movies.MovieAdapter;
import com.fuego.moviepoint.R;
import com.fuego.moviepoint.Watchlist.Watchlist;
import com.fuego.moviepoint.Watchlist.WatchlistViewModal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

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
    public static final String EXTRA_ADULT = "com.fuego.moviepoint.Activities.extra.ADULT";

    private WatchlistViewModal mWatchlistViewModal;
    private FloatingActionButton watchlistFab, historyFab;
    private TextView moviePlot, movieTitle, movieReleaseDate, movieAdult;
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
        moviePlot = findViewById(R.id.movie_plot);
        movieTitle = findViewById(R.id.movie_title);
        movieReleaseDate = findViewById(R.id.movie_release_date);
        movieAdult = findViewById(R.id.movie_adult);
        imageView = findViewById(R.id.movie_poster);
        watchlistFab = findViewById(R.id.fab_watchlist);
        historyFab = findViewById(R.id.fab_history);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        if (intent.hasExtra(EXTRA_TITLE)) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("");
            movieTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            moviePlot.setText(intent.getStringExtra(EXTRA_OVERVIEW));
            setReleaseDate();
            if (!intent.getExtras().getBoolean(EXTRA_ADULT)) {
                movieAdult.setVisibility(View.GONE);
            }

            Picasso.get().load(MovieAdapter.MOVIE_BASE_URL + intent.getStringExtra(EXTRA_IMAGE))
                    .placeholder(R.drawable.ic_film_placeholder)
                    .into(imageView);
        }

        watchlistFab.setOnClickListener(v -> {
            Watchlist watchlist = new Watchlist();
            watchlist.setTitle(intent.getStringExtra(EXTRA_TITLE));
            watchlist.setOverview(intent.getStringExtra(EXTRA_OVERVIEW));
            watchlist.setImagePath(intent.getStringExtra(EXTRA_IMAGE));
            watchlist.setDate(intent.getStringExtra(EXTRA_DATE));
            watchlist.setAdult(intent.getExtras().getBoolean(EXTRA_ADULT));
            watchlist.setWatched(false);
            mWatchlistViewModal.insert(watchlist);
            watchlistNotification();
        });

        historyFab.setOnClickListener(v -> {
            Watchlist watchlist = new Watchlist();
            watchlist.setTitle(intent.getStringExtra(EXTRA_TITLE));
            watchlist.setOverview(intent.getStringExtra(EXTRA_OVERVIEW));
            watchlist.setImagePath(intent.getStringExtra(EXTRA_IMAGE));
            watchlist.setDate(intent.getStringExtra(EXTRA_DATE));
            watchlist.setAdult(intent.getExtras().getBoolean(EXTRA_ADULT));
            watchlist.setWatched(true);
            mWatchlistViewModal.insert(watchlist);
            watchedNotification();
        });
    }

    private void setReleaseDate() {
        int year = Integer.parseInt(intent.getStringExtra(EXTRA_DATE).substring(0, 4));
        String month = new DateFormatSymbols().getMonths()[Integer.parseInt(intent.getStringExtra(EXTRA_DATE).substring(5, 7))];
        int day = Integer.parseInt(intent.getStringExtra(EXTRA_DATE).substring(8, 10));
        movieReleaseDate.setText(month + " " + day + ", " + year);
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
}
