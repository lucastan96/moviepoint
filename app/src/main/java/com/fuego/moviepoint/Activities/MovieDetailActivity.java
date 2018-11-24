package com.fuego.moviepoint.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.fuego.moviepoint.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.fuego.moviepoint.EXTRA_TITLE";
    public static final String EXTRA_IMAGE = "com.fuego.moviepoint.EXTRA_DESCRIPTION";
    public static final String EXTRA_OVERVIEW = "com.fuego.moviepoint.EXTRA_PRIORITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = new Intent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView = findViewById(R.id.textView);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        Log.d("Hello", "onCreate: " + intent.getStringExtra(EXTRA_TITLE));

        if (intent.hasExtra(EXTRA_TITLE)) {
            toolbar.setTitle(intent.getStringExtra(EXTRA_TITLE));
            textView.setText(intent.getStringExtra(EXTRA_OVERVIEW));
        }
    }
}
