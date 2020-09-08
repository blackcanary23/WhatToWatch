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
import com.riwesta.whattowatch.repositories.GenreRepository;
import com.riwesta.whattowatch.R;
import com.riwesta.whattowatch.adapters.FilmSearchAdapter;
import java.util.ArrayList;
import java.util.Objects;


public class FilmSearchFragment extends Fragment {

    private ArrayList<GenreRepository> gRepList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            gRepList = (ArrayList<GenreRepository>) savedInstanceState.getSerializable("fsList");
            //Log.d("MyLogs", gRepList.size() + "notnullonCreate");
        }
        else {
            getCriterion();
            //Log.d("MyLogs", gRepList.size() + "onCreate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Log.d("MyLogs", gRepList.size() + "onCreateView");
        View view = inflater.inflate(R.layout.film_search_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.list);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FilmSearchAdapter fsAdapter = new FilmSearchAdapter(getActivity(), gRepList);
        recyclerView.setAdapter(fsAdapter);

        return view;
    }

    void getCriterion() {

        gRepList.clear();
        Field[] fields = R.drawable.class.getFields();
        String name;
        int image;

        for (Field field : fields) {
            name = field.getName();

            if (name.contains("best") || name.contains("cannes") || name.contains("new")) {
                image = getResources().getIdentifier(name, "drawable",
                        Objects.requireNonNull(getActivity()).getPackageName());
                gRepList.add(new GenreRepository(name.replace("_", "-"), image));
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putSerializable("fsList", gRepList);
        //Log.d("MyLogs", gRepList.size() + "onSave");
    }
}