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
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<MoviesRepository> movie;
    private ItemClicked activity;

    public interface ItemClicked {

        void onItemClicked(MoviesRepository moviesRepository);
    }

    public DataAdapter(Context context, ArrayList<MoviesRepository> movie) {

        this.movie = movie;
        activity = (ItemClicked) context;
    }

    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(movie.get(position));
        holder.id.setText(movie.get(position).getId() + " ");
        holder.name.setText(movie.get(position).getName() + " ");
        holder.year.setText(movie.get(position).getYear() + " ");
        holder.rate.setText(movie.get(position).getRate());
        holder.logo.setText(movie.get(position).getLogo());
        Picasso.get().load(movie.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {

        return movie.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView id;
        private TextView name;
        private TextView year;
        private TextView rate;
        private TextView logo;
        private ImageView image;

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

                    activity.onItemClicked(movie.get(getAdapterPosition()));

                }
            });

        }
    }
}