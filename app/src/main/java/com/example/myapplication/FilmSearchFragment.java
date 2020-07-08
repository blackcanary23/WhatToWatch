package com.example.myapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.reflect.Field;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;


public class FilmSearchFragment extends Fragment {

    private ArrayList<FilmSearchRepository> mRepList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.list);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FilmSearchAdapter fsAdapter = new FilmSearchAdapter(getActivity(), mRepList);
        recyclerView.setAdapter(fsAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        getCriterion();
    }

    public void getCriterion() {

        Field[] fields = R.drawable.class.getFields();
        String name;
        int image;

        for (Field field : fields) {
            name = field.getName();

            if (name.contains("best") || name.contains("cannes") || name.contains("new")) {
                image = getResources().getIdentifier(name, "drawable", "com.example.myapplication");
                mRepList.add(new FilmSearchRepository(name.replace("_", "-"), image));
            }
        }
    }
}