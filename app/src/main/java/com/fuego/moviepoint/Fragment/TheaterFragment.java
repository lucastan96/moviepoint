package com.fuego.moviepoint.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuego.moviepoint.Activities.MovieDetailActivity;
import com.fuego.moviepoint.Movies.MovieAdapter;
import com.fuego.moviepoint.Movies.MovieViewModel;
import com.fuego.moviepoint.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TheaterFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theater, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        }
        recyclerView.setHasFixedSize(false);

        MovieAdapter adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);

        if (getActivity() != null) {
            MovieViewModel movieViewModel = ViewModelProviders.of(getActivity()).get(MovieViewModel.class);
            movieViewModel.getAllMovies().observe(this, adapter::setMovies);
        }

        adapter.setOnItemClickListener(movie -> {
            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_TMDBID, movie.getTmdbId());
            intent.putExtra(MovieDetailActivity.EXTRA_TITLE, movie.getTitle());
            intent.putExtra(MovieDetailActivity.EXTRA_IMAGE, movie.getImagePath());
            intent.putExtra(MovieDetailActivity.EXTRA_OVERVIEW, movie.getOverview());
            intent.putExtra(MovieDetailActivity.EXTRA_DATE, movie.getDate());
            startActivity(intent);
        });

        return view;
    }
}
