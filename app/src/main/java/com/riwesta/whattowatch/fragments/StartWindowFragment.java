package com.riwesta.whattowatch.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.riwesta.whattowatch.listeners.SwipeTouchListener;
import com.riwesta.whattowatch.R;
import com.riwesta.whattowatch.listeners.PlayerEventListener;
import org.apache.commons.io.FilenameUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;


public class StartWindowFragment extends Fragment {

    private StartWindowListener swListener;
    private PlayerView playerView;
    private SimpleExoPlayer sePlayer;
    private ExtractorMediaSource meSource;
    private ArrayList<String> trackNameList = new ArrayList<>();
    private SharedPreferences sPrefs;
    private int launchCtr;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.start_window, container, false);
        playerView = view.findViewById(R.id.video_view);

        playerView.setOnTouchListener(new SwipeTouchListener(getActivity()) {
            public void onSwipeLeft() {
                releasePlayer();
                updateFragment();
                saveCounter();
                Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo
                        .SCREEN_ORIENTATION_UNSPECIFIED);
            }
        });

        return view;
    }

    public interface StartWindowListener {

        void onStartWindowListener();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            swListener = (StartWindowListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " should implement StartWindowListener");
        }
    }

    public void updateFragment() {

        swListener.onStartWindowListener();
    }

    @Override
    public void onStart() {

        super.onStart();
        initializePlayer();
    }

    @Override
    public void onStop() {

        super.onStop();
        releasePlayer();
    }

    private void initializePlayer() {

        getTrackNameList();
        loadCounter();

        if (sePlayer == null) {
            sePlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            sePlayer.addListener(new PlayerEventListener() {
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == Player.STATE_ENDED) {
                        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo
                                .SCREEN_ORIENTATION_UNSPECIFIED);
                        updateFragment();
                        saveCounter();
                    }
                }
            });

            sePlayer.setPlayWhenReady(true);
            playerView.setPlayer(sePlayer);
        }

        Uri uri = RawResourceDataSource.buildRawResourceUri(Objects.requireNonNull(getContext())
                .getResources().getIdentifier(trackNameList.get(launchCtr), "raw",
                        getContext().getPackageName()));


        meSource = new ExtractorMediaSource.Factory(new
                DefaultDataSourceFactory(Objects.requireNonNull(getActivity()),
                "MyApplication")).createMediaSource(uri);

        sePlayer.prepare(meSource);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
    }

    private void releasePlayer() {

        if (sePlayer != null) {
            sePlayer.release();
            sePlayer = null;
            meSource = null;
        }
    }

    private void getTrackNameList() {

        Field[] tracks = R.raw.class.getFields();
        for (Field track : tracks) {
            trackNameList.add(FilenameUtils.removeExtension(track.getName()));
        }
    }

    private void loadCounter() {

        sPrefs = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        launchCtr = sPrefs.getInt("launch_ctr",0);

        if (launchCtr >= 17)
            launchCtr = 0;
        else
            launchCtr++;
    }

    private void saveCounter() {

        sPrefs = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sPrefs.edit();
        spEditor.putInt("launch_ctr", launchCtr);
        spEditor.apply();
    }
}