package com.riwesta.whattowatch.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.reflect.Field;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.riwesta.whattowatch.models.Genre;
import com.riwesta.whattowatch.R;
import com.riwesta.whattowatch.adapters.FilmSearchAdapter;
import java.util.ArrayList;
import java.util.Objects;


public class FilmSearchFragment extends Fragment {

    private ArrayList<Genre> genreList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            genreList = (ArrayList<Genre>) savedInstanceState
                        .getSerializable("genreList");
            //Log.d("MyLogs", genreList.size() + "notnullonCreate");
        }
        else {
            getGenreList();
            //Log.d("MyLogs", genreList.size() + "onCreate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Log.d("MyLogs", genreList.size() + "onCreateView");
        View view = inflater.inflate(R.layout.film_search_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.list);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FilmSearchAdapter fsAdapter = new FilmSearchAdapter(getActivity(), genreList);
        recyclerView.setAdapter(fsAdapter);

        return view;
    }

    private void getGenreList() {

        genreList.clear();
        Field[] genres = R.drawable.class.getFields();
        String name;
        int image;

        for (Field genre : genres) {
            name = genre.getName();

            if (name.contains("best") || name.contains("cannes") || name.contains("new")) {
                image = getResources().getIdentifier(name, "drawable",
                        Objects.requireNonNull(getActivity()).getPackageName());
                genreList.add(new Genre(name.replace("_", "-"), image));
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putSerializable("genreList", genreList);
        //Log.d("MyLogs", genreList.size() + "onSave");
    }
}