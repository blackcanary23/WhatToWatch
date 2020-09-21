package com.riwesta.whattowatch.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.riwesta.whattowatch.R;
import com.riwesta.whattowatch.models.Movie;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private ArrayList<Movie> movieList;
    private MovieClicked activity;

    public interface MovieClicked {

        void onMovieClicked(Movie moviesRepository);
    }

    public SearchResultAdapter(Context context, ArrayList<Movie> movieList) {

        this.movieList = movieList;
        activity = (MovieClicked) context;
    }

    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, int position) {

        holder.id.setText(movieList.get(position).getId() + " ");
        holder.name.setText(movieList.get(position).getName() + " ");
        holder.year.setText(movieList.get(position).getYear() + " ");
        holder.rate.setText(movieList.get(position).getRate());
        holder.logo.setText(movieList.get(position).getLogo());
        Picasso.get().load(movieList.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {

        return movieList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView id;
        final TextView name;
        final TextView year;
        final TextView rate;
        final TextView logo;
        final ImageView image;

        ViewHolder(@NonNull View itemView) {

            super(itemView);

            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.movie_name);
            year = itemView.findViewById(R.id.year);
            rate = itemView.findViewById(R.id.rated);
            logo = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    activity.onMovieClicked(movieList.get(getAdapterPosition()));

                }
            });

        }
    }
}