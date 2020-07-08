package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, DataAdapter.ItemClicked, FilmSearchAdapter.ItemClicked {

    private FilmSearchFragment fsFrag;
    private FilmDescriptionFragment fdFrag;
    private FragmentTransaction fTrans;
    private SharedPreferences sPrefs;
    protected int launchCtr;
    private ImageFragment iFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadCounter();

        StartWindowFragment swFrag = new StartWindowFragment();
        fsFrag = new FilmSearchFragment();
        fdFrag = new FilmDescriptionFragment();
        iFrag = new ImageFragment();

        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.container, swFrag);
        fTrans.commitAllowingStateLoss();
    }

    @Override
    public void onFirstFragmentInteraction() {

        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.container, fsFrag);
        fTrans.addToBackStack(null);
        fTrans.commitAllowingStateLoss();
    }

    @Override
    public void onItemClicked(MoviesRepository moviesRepository) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", moviesRepository.getImdb());
        fdFrag.setArguments(bundle);
        fTrans = getSupportFragmentManager().beginTransaction();

        if (fdFrag.isAdded()) {
            fTrans.remove(fdFrag);
        }

        fTrans.add(fdFrag, "fdFrag");

        fTrans.commitAllowingStateLoss();

        System.out.println("HELLOOOOOO");
    }

    @Override
    public void onSecondItemClicked(FilmSearchRepository fsRepository) {

        SearchResultFragment srFrag = new SearchResultFragment();
        fTrans = getSupportFragmentManager().beginTransaction();
        srFrag.getMoviesList(fsRepository.getName());
        fTrans.replace(R.id.container, srFrag);
        fTrans.addToBackStack(null);
        fTrans.commitAllowingStateLoss();
    }

    void loadCounter() {

        sPrefs = getPreferences(MODE_PRIVATE);
        launchCtr = sPrefs.getInt("launch_ctr",0);
        if (launchCtr >= 17)
            launchCtr = 0;
        else
            launchCtr++;
        Toast.makeText(this, "Swipe Left to Skip", Toast.LENGTH_LONG).show();
    }

    void saveCounter() {

        sPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sPrefs.edit();
        spEditor.putInt("launch_ctr", launchCtr);
        spEditor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveCounter();
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {
            getSupportFragmentManager().popBackStack();
            fTrans = getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.container, iFrag);
            fTrans.commitAllowingStateLoss();
        }
        else {
            super.onBackPressed();
        }
    }
}
