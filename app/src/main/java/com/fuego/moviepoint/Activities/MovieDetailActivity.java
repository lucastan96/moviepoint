package com.fuego.moviepoint.Activities;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fuego.moviepoint.Movies.MovieAdapter;
import com.fuego.moviepoint.R;
import com.fuego.moviepoint.Watchlist.Watched;
import com.fuego.moviepoint.Watchlist.WatchedViewModal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;

import static com.fuego.moviepoint.App.CHANNEL_1_ID;
import static com.fuego.moviepoint.App.CHANNEL_2_ID;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.fuego.moviepoint.Activities.extra.TITLE";
    public static final String EXTRA_IMAGE = "com.fuego.moviepoint.Activities.extra.IMAGE";
    public static final String EXTRA_OVERVIEW = "com.fuego.moviepoint.Activities.extra.OVERVIEW";

    private WatchedViewModal watchedViewModal;
    private FloatingActionButton floatingActionButton;
    private TextView moviePlot, movieTitle;
    private ImageView imageView;
    Intent intent;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        watchedViewModal = ViewModelProviders.of(this).get(WatchedViewModal.class);
        notificationManager = NotificationManagerCompat.from(this);

        intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        moviePlot = findViewById(R.id.movie_plot);
        movieTitle = findViewById(R.id.movie_title);
        imageView = findViewById(R.id.movie_poster);
        floatingActionButton = findViewById(R.id.watch_movie);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        if (intent.hasExtra(EXTRA_TITLE)) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(intent.getStringExtra(EXTRA_TITLE));
            movieTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            moviePlot.setText(intent.getStringExtra(EXTRA_OVERVIEW));

            Picasso.get().load(MovieAdapter.MOVIE_BASE_URL + intent.getStringExtra(EXTRA_IMAGE))
                    .placeholder(R.drawable.ic_film)
                    .into(imageView);
        }

        floatingActionButton.setOnClickListener(v -> {
            Watched watched = new Watched();
            watched.setTitle(intent.getStringExtra(EXTRA_TITLE));
            watched.setOverview(intent.getStringExtra(EXTRA_OVERVIEW));
            watched.setImagePath(intent.getStringExtra(EXTRA_IMAGE));
            watchedViewModal.insert(watched);
            Toast.makeText(this, "Added to watchlist", Toast.LENGTH_SHORT).show();
            watchlistNotification();
        });
    }

    public void watchlistNotification() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_film)
                .setContentTitle("Added new movie to watch list")
                .setContentText(intent.getStringExtra(EXTRA_TITLE))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

    public void watchedNotification() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_film)
                .setContentTitle("Added new movie to watched")
                .setContentText(intent.getStringExtra(EXTRA_TITLE))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        notificationManager.notify(2, notification);
    }
}
