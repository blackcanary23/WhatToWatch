package com.riwesta.whattowatch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import com.riwesta.whattowatch.R;
import com.riwesta.whattowatch.adapters.FilmSearchAdapter;
import com.riwesta.whattowatch.adapters.SearchResultAdapter;
import com.riwesta.whattowatch.fragments.FilmSearchFragment;
import com.riwesta.whattowatch.fragments.SearchResultFragment;
import com.riwesta.whattowatch.fragments.StartWindowFragment;
import com.riwesta.whattowatch.models.GenreRepository;
import com.riwesta.whattowatch.models.MoviesRepository;


public class MainActivity extends AppCompatActivity implements
        StartWindowFragment.StartWindowListener,
        SearchResultAdapter.MovieClicked,
        FilmSearchAdapter.GenreClicked {

    private FragmentManager fMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fMan = getSupportFragmentManager();

        if (savedInstanceState == null) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Toast.makeText(this, "Swipe Left to Skip", Toast.LENGTH_LONG).show();
            StartWindowFragment swFrag = new StartWindowFragment();
            fMan.beginTransaction()
                    .add(R.id.container, swFrag, "swFrag")
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onStartWindowListener() {

        FilmSearchFragment fsFrag = new FilmSearchFragment();
        fMan.beginTransaction()
            .replace(R.id.container, fsFrag, "fsFrag")
            .commitAllowingStateLoss();
    }

    @Override
    public void onGenreClicked(GenreRepository genreRepository) {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        SearchResultFragment srFrag = new SearchResultFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("genre", genreRepository.getName());
        bundle.putSerializable("visible", true);
        srFrag.setArguments(bundle);

        fMan.beginTransaction()
            .replace(R.id.container, srFrag, "srFrag")
            .addToBackStack(null)
            .commitAllowingStateLoss();
    }

    @Override
    public void onMovieClicked(MoviesRepository moviesRepository) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(moviesRepository.getImdb()));
        startActivity(browserIntent);
    }
}