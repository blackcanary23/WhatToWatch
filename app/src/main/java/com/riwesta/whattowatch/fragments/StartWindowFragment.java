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
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.riwesta.whattowatch.OnSwipeTouchListener;
import com.riwesta.whattowatch.R;
import com.riwesta.whattowatch.StartWindowListener;
import org.apache.commons.io.FilenameUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;


public class StartWindowFragment extends Fragment {

    private StartWindowListener swListener;
    private ArrayList<String> movieNameList = new ArrayList<>();
    private SharedPreferences sPrefs;
    private int launchCtr;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    //private boolean playWhenReady = true;
    private int currentWindow;
    private long playbackPosition;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.start_window, container, false);
        playerView = view.findViewById(R.id.video_view);
        initializePlayer();

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups,
                                        TrackSelectionArray trackSelections) {
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo
                            .SCREEN_ORIENTATION_UNSPECIFIED);
                    updateFragment();
                    saveCounter();
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onSeekProcessed() {
            }
        });

        playerView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
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

    @Override
    public void onDetach() {
        super.onDetach();
        swListener = null;
    }

    public void updateFragment() {

        swListener.onStartWindowListener();
    }

    public void getMoviesList() {

        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            movieNameList.add(FilenameUtils.removeExtension(field.getName()));
        }
    }

    private void initializePlayer() {

        getMoviesList();
        loadCounter();

        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(player);
            player.setPlayWhenReady(true);
            player.seekTo(currentWindow, playbackPosition);
        }

        Uri uri = RawResourceDataSource.buildRawResourceUri(Objects.requireNonNull(getContext())
                .getResources().getIdentifier(movieNameList.get(launchCtr), "raw",
                        getContext().getPackageName()));



        ExtractorMediaSource audioSource = new ExtractorMediaSource.Factory(new
                DefaultDataSourceFactory(Objects.requireNonNull(getActivity()),
                "MyApplication")).createMediaSource(uri);

        player.prepare(audioSource);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

    }

    private void releasePlayer() {

        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            //playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onStop() {

        releasePlayer();
        super.onStop();
    }

    void loadCounter() {

        sPrefs = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        launchCtr = sPrefs.getInt("launch_ctr",0);

        if (launchCtr >= 17)
            launchCtr = 0;
        else
            launchCtr++;
    }

    void saveCounter() {

        sPrefs = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sPrefs.edit();
        spEditor.putInt("launch_ctr", launchCtr);
        spEditor.apply();
    }
}