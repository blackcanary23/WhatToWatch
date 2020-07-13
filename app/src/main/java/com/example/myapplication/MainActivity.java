package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, DataAdapter.ItemClicked, FilmSearchAdapter.ItemClicked {

    private FragmentTransaction fTrans;
    private SearchResultFragment srFrag;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        if (fm.findFragmentByTag("swFrag") == null) {
            Toast.makeText(this, "Swipe Left to Skip", Toast.LENGTH_LONG).show();
            StartWindowFragment swFrag = new StartWindowFragment();
            fTrans = getSupportFragmentManager().beginTransaction();
            resetSeek();
            fTrans.add(R.id.container, swFrag, "swFrag");
            fTrans.commitAllowingStateLoss();
        }

        //fsFrag = new FilmSearchFragment();
        //fdFrag = new FilmDescriptionFragment();
    }

    @Override
    public void onFirstFragmentInteraction() {

        FilmSearchFragment fsFrag = new FilmSearchFragment();
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.container, fsFrag);
        fTrans.addToBackStack(null);
        fTrans.commitAllowingStateLoss();
    }

    @Override
    public void onItemClicked(MoviesRepository moviesRepository) {

        FilmDescriptionFragment fdFrag = new FilmDescriptionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", moviesRepository.getImdb());
        fdFrag.setArguments(bundle);

        fTrans = getSupportFragmentManager().beginTransaction();

        if (fdFrag.isAdded()) {
            fTrans.remove(fdFrag);
        }

        fTrans.add(fdFrag, "fdFrag");

        fTrans.commitAllowingStateLoss();
    }

    @Override
    public void onSecondItemClicked(FilmSearchRepository fsRepository) {

        srFrag = new SearchResultFragment();
        fTrans = fm.beginTransaction();
        srFrag.getMoviesList(fsRepository.getName());
        fTrans.replace(R.id.container, srFrag, "srFrag");
        fTrans.addToBackStack(null);
        fTrans.commitAllowingStateLoss();
    }

    void resetSeek() {

        SharedPreferences sPrefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sPrefs.edit();
        spEditor.putInt("seek", 0);
        spEditor.apply();
    }

    @Override
    public void onBackPressed() {

        int count = fm.getBackStackEntryCount();

        if (count == 1) {
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    private void instantiateFragments(Bundle inState) {

        fTrans = fm.beginTransaction();

        if (inState != null) {
            srFrag = (SearchResultFragment) fm.getFragment(inState, "SearchResultFragment");
            srFrag.loadmRepList();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        if (fm.findFragmentByTag("srFrag") != null)
            fm.putFragment(outState, "SearchResultFragment", srFrag);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {

        if (fm.findFragmentByTag("srFrag") != null)
            instantiateFragments(inState);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (fm.findFragmentByTag("fdFrag") != null) {
            getSupportFragmentManager().beginTransaction().remove(fm.findFragmentByTag("fdFrag")).commit();
        }
    }
}