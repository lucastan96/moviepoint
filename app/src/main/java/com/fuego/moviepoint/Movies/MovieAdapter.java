package com.fuego.moviepoint.Movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuego.moviepoint.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private List<Movie> movies = new ArrayList<>();
    public static final String MOVIE_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private OnItemClickListener listener;

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        Movie currentMovie = movies.get(position);
        holder.textViewTitle.setText(currentMovie.getTitle());

        Picasso.get().load(MOVIE_BASE_URL + currentMovie.getImagePath())
                .placeholder(R.drawable.ic_film_placeholder)
                .into(holder.textViewImagePath, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.textViewImagePath.setAlpha(0f);
                        holder.textViewImagePath.animate().setDuration(200).alpha(1f).start();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    class MovieHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private ImageView textViewImagePath;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.movie_title);
            textViewImagePath = itemView.findViewById(R.id.image_path);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(movies.get(position));
                }
            });
        }
    }
}