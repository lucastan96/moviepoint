package com.fuego.moviepoint.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.fuego.moviepoint.R;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.fuego.moviepoint.Activities.extra.TITLE";
    public static final String EXTRA_IMAGE = "com.fuego.moviepoint.Activities.extra.IMAGE";
    public static final String EXTRA_OVERVIEW = "com.fuego.moviepoint.Activities.extra.OVERVIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView = findViewById(R.id.movie_plot);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        if (intent.hasExtra(EXTRA_TITLE)) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(intent.getStringExtra(EXTRA_TITLE));
            textView.setText(intent.getStringExtra(EXTRA_OVERVIEW));
        }
    }
}
