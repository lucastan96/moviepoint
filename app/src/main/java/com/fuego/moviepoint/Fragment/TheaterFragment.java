package com.fuego.moviepoint.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fuego.moviepoint.Movie;
import com.fuego.moviepoint.MovieViewModel;
import com.fuego.moviepoint.R;

import java.util.List;

public class TheaterFragment extends Fragment {
    private MovieViewModel movieViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_theater, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getAllMovies().observe(this, movies -> {
            //Update Recycler view
            Toast.makeText(getContext(), "Changed", Toast.LENGTH_SHORT).show();
        });

    }
}
