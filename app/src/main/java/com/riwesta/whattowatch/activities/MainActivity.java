package com.riwesta.whattowatch.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;
import com.riwesta.whattowatch.R;
import com.riwesta.whattowatch.StartWindowListener;
import com.riwesta.whattowatch.adapters.FilmSearchAdapter;
import com.riwesta.whattowatch.adapters.SearchResultAdapter;
import com.riwesta.whattowatch.fragments.FilmDescriptionFragment;
import com.riwesta.whattowatch.fragments.FilmSearchFragment;
import com.riwesta.whattowatch.fragments.SearchResultFragment;
import com.riwesta.whattowatch.fragments.StartWindowFragment;
import com.riwesta.whattowatch.repositories.GenreRepository;
import com.riwesta.whattowatch.repositories.MoviesRepository;
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

        if (savedInstanceState != null) {

            if (fMan.getFragment(savedInstanceState,"SearchResultFragment") != null) {
                srFrag = (SearchResultFragment) fMan.getFragment(savedInstanceState,
                        "SearchResultFragment");
            }

            else if (fMan.getFragment(savedInstanceState,"FilmSearchFragment") != null) {
                fsFrag = (FilmSearchFragment) fMan.getFragment(savedInstanceState,
                        "FilmSearchFragment");
            }
        }

        else {
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

        Bundle bundle = new Bundle();
        bundle.putSerializable("genre", genreRepository.getName());
        srFrag.setArguments(bundle);

        fTrans = fMan.beginTransaction();
        fTrans.replace(R.id.container, srFrag, "srFrag");
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
        fTrans.replace(R.id.container, fdFrag, "fdFrag");
        fTrans.addToBackStack(null);
        fTrans.commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);

        Fragment frag = fMan.findFragmentById(R.id.container);
        if (frag instanceof FilmSearchFragment)
            fMan.putFragment(outState, "FilmSearchFragment", Objects.requireNonNull(frag));
        else if (frag instanceof SearchResultFragment)
            fMan.putFragment(outState, "SearchResultFragment", Objects.requireNonNull(frag));
    }
}