package com.riwesta.whattowatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements StartWindowListener,
        SearchResultAdapter.MovieClicked, FilmSearchAdapter.GenreClicked {

    private FragmentTransaction fTrans;
    private SearchResultFragment srFrag;
    private FilmSearchFragment fsFrag;
    private FragmentManager fMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fMan = getSupportFragmentManager();

        if (fMan.findFragmentByTag("swFrag") == null) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Toast.makeText(this, "Swipe Left to Skip", Toast.LENGTH_LONG).show();
            StartWindowFragment swFrag = new StartWindowFragment();
            fTrans = fMan.beginTransaction();
            fTrans.add(R.id.container, swFrag, "swFrag");
            fTrans.commitAllowingStateLoss();
        }
    }

    @Override
    public void onStartWindowListener() {

        fsFrag = new FilmSearchFragment();
        fTrans = fMan.beginTransaction();
        fTrans.replace(R.id.container, fsFrag, "fsFrag");
        fTrans.addToBackStack(null);
        fTrans.commitAllowingStateLoss();
    }

    @Override
    public void onMovieClicked(MoviesRepository moviesRepository) {

        FilmDescriptionFragment fdFrag = new FilmDescriptionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("imdb", moviesRepository.getImdb());
        fdFrag.setArguments(bundle);

        fTrans = fMan.beginTransaction();

        if (fdFrag.isAdded()) {
            fTrans.remove(fdFrag);
        }

        fTrans.add(fdFrag, "fdFrag");
        fTrans.commitAllowingStateLoss();
    }

    @Override
    public void onGenreClicked(GenreRepository genreRepository) {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        srFrag = new SearchResultFragment();
        fTrans = fMan.beginTransaction();
        srFrag.getMoviesList(genreRepository.getName());
        fTrans.replace(R.id.container, srFrag, "srFrag");
        fTrans.addToBackStack(null);
        fTrans.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {

        int count = fMan.getBackStackEntryCount();

        if (count == 1) {
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    private void instantiateFragments(Bundle inState) {

        fTrans = fMan.beginTransaction();

        if (inState.get("SearchResultFragment") != null) {
            srFrag = (SearchResultFragment) fMan.getFragment(inState, "SearchResultFragment");
            assert srFrag != null;
            srFrag.loadmRepList();
        }
        else if (inState.get("FilmSearchFragment") != null) {
            fsFrag = (FilmSearchFragment) fMan.getFragment(inState, "FilmSearchFragment");
            assert fsFrag != null;
            fsFrag.loadgRepList();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        srFrag = (SearchResultFragment) fMan.findFragmentByTag("srFrag");
        fsFrag = (FilmSearchFragment) fMan.findFragmentByTag("fsFrag");
        if (srFrag != null)
            fMan.putFragment(outState, "SearchResultFragment", srFrag);
        else if (fsFrag != null)
            fMan.putFragment(outState, "FilmSearchFragment", fsFrag);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle inState) {

        if (fMan.findFragmentByTag("srFrag") != null)
            instantiateFragments(inState);
        else if (fMan.findFragmentByTag("fsFrag") != null)
            instantiateFragments(inState);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (fMan.findFragmentByTag("fdFrag") != null) {
            fMan.beginTransaction().remove(Objects.requireNonNull(fMan.findFragmentByTag("fdFrag"))).
                    commit();
        }
    }
}