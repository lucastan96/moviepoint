package com.fuego.moviepoint.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuego.moviepoint.Activities.MovieDetailActivity;
import com.fuego.moviepoint.MovieAdapter;
import com.fuego.moviepoint.MovieViewModel;
import com.fuego.moviepoint.R;

import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TheaterFragment extends Fragment {
    private MovieViewModel movieViewModel;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theater, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setHasFixedSize(false);

        MovieAdapter adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);

        movieViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MovieViewModel.class);
        movieViewModel.getAllMovies().observe(this, adapter::setMovies);

        adapter.setOnItemClickListener(movie -> {
            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_TITLE, movie.getTitle());
            intent.putExtra(MovieDetailActivity.EXTRA_IMAGE, movie.getImagePath());
            intent.putExtra(MovieDetailActivity.EXTRA_OVERVIEW, movie.getOverview());
            startActivityForResult(intent, 1);
        });
        return view;
    }
}
