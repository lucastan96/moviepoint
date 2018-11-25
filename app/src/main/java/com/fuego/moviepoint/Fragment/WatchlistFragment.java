package com.fuego.moviepoint.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuego.moviepoint.R;
import com.fuego.moviepoint.Watchlist.WatchedAdapter;
import com.fuego.moviepoint.Watchlist.WatchedViewModal;

import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WatchlistFragment extends Fragment {

    RecyclerView recyclerView;
    private WatchedViewModal watchedViewModal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setHasFixedSize(false);

        WatchedAdapter adapter = new WatchedAdapter();
        recyclerView.setAdapter(adapter);

        watchedViewModal = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(WatchedViewModal.class);
        watchedViewModal.getAllMovies().observe(this, adapter::setMovies);

        return view;
    }
}
