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
import com.riwesta.whattowatch.repositories.GenreRepository;

import java.util.ArrayList;


public class FilmSearchAdapter extends RecyclerView.Adapter<FilmSearchAdapter.ViewHolder> {

    private ArrayList<GenreRepository> gRepList;
    private GenreClicked activity;
    private String showName;

    public interface GenreClicked {

        void onGenreClicked(GenreRepository fsRepository);
    }

    public FilmSearchAdapter(Context context, ArrayList<GenreRepository> gRepList) {

        this.gRepList = gRepList;
        activity = (GenreClicked) context;
    }

    @NonNull
    @Override
    public FilmSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film_search, parent, false);
        return new FilmSearchAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FilmSearchAdapter.ViewHolder holder, int position) {

        editMovieName(position);
        holder.itemView.setTag(gRepList.get(position));
        holder.name.setText(showName + " ");
        holder.image.setImageResource(gRepList.get(position).getImage());
    }

    @Override
    public int getItemCount() {

        return gRepList.size();
    }

    void editMovieName(int position) {

        showName = gRepList.get(position).getName().replace("-", " ");
        if (showName.contains("1970 1979"))
            showName = showName.replace("1970 1979", "70's");
        else if (showName.contains("1980 1989"))
            showName = showName.replace("1980 1989", "80's");
        else if (showName.contains("1990 1999"))
            showName = showName.replace("1990 1999", "90's");
        else if (showName.contains("2000 2009"))
            showName = showName.replace("2000 2009", "2000's");
        else if (showName.contains("2010 2019"))
            showName = showName.replace("2010 2019", "2010's");
        else if (showName.contains("sci fi"))
            showName = showName.replace("sci fi", "sci-fi");
        else if (showName.contains("cannes"))
            showName = showName.replace(showName, "cannes winners");
        else if (showName.contains("oscar"))
            showName = showName.replace(showName, "oscar winners");
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

                    activity.onGenreClicked(gRepList.get(getAdapterPosition()));

                }
            });

        }
    }
}
