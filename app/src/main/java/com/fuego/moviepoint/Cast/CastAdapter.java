package com.fuego.moviepoint.Cast;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuego.moviepoint.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastHolder> {

    public static final String MOVIE_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private List<Cast> cast;

    public CastAdapter(List<Cast> cast) {
        this.cast = cast;
    }

    @NonNull
    @Override
    public CastAdapter.CastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cast_item, parent, false);
        return new CastAdapter.CastHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.CastHolder holder, int position) {
        Cast currentCast = cast.get(position);
        holder.textViewName.setText(currentCast.getName());
        holder.textViewRole.setText(currentCast.getRole());

        Picasso.get().load(MOVIE_BASE_URL + currentCast.getImage())
                .resize(500, 500)
                .centerCrop()
                .placeholder(R.drawable.ic_film_placeholder)
                .into(holder.imageViewImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.imageViewImage.setAlpha(0f);
                        holder.imageViewImage.animate().setDuration(200).alpha(1f).start();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class CastHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewRole;
        private ImageView imageViewImage;

        public CastHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.cast_name);
            textViewRole = itemView.findViewById(R.id.cast_role);
            imageViewImage = itemView.findViewById(R.id.cast_photo);
        }
    }
}
