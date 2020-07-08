package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class FilmSearchAdapter extends RecyclerView.Adapter<FilmSearchAdapter.ViewHolder> {

    private ArrayList<FilmSearchRepository> movie;
    private FilmSearchAdapter.ItemClicked activity;

    public interface ItemClicked {

        void onSecondItemClicked(FilmSearchRepository fsRepository);
    }

    public FilmSearchAdapter(Context context, ArrayList<FilmSearchRepository> movie) {

        this.movie = movie;
        activity = (FilmSearchAdapter.ItemClicked) context;
    }

    @NonNull
    @Override
    public FilmSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_search, parent, false);
        return new FilmSearchAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FilmSearchAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(movie.get(position));
        holder.name.setText(movie.get(position).getName() + " ");
        holder.image.setImageResource(movie.get(position).getImage());
    }

    @Override
    public int getItemCount() {

        return movie.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView image;

        ViewHolder(@NonNull View itemView) {

            super(itemView);

            name = itemView.findViewById(R.id.top_movie);
            image = itemView.findViewById(R.id.top250);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    activity.onSecondItemClicked(movie.get(getAdapterPosition()));

                }
            });

        }
    }
}
