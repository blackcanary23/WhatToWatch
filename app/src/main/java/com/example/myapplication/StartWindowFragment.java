package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import org.apache.commons.io.FilenameUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;


public class StartWindowFragment extends Fragment implements TextureView.SurfaceTextureListener {

    private OnFragmentInteractionListener mListener;
    private ArrayList<String> movieName = new ArrayList<>();
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.start_window, container, false);
        TextureView textureView = view.findViewById(R.id.videoPlayer);
        textureView.setSurfaceTextureListener(this);

        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeLeft() {

                mediaPlayer.stop();
                updateFragment();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " should implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateFragment() {

        mListener.onFirstFragmentInteraction();
    }

    public void getMoviesList() {

        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            movieName.add(FilenameUtils.removeExtension(field.getName()));
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setSurface(new Surface(surface));

        try {
            getMoviesList();
            SharedPreferences sPrefs = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
            int launchCtr = sPrefs.getInt("launch_ctr", 0);
            Uri myVideoUri= Uri.parse( "android.resource://" + Objects.requireNonNull(getContext()).getPackageName() + "/raw/" + movieName.get(launchCtr));
            mediaPlayer.setDataSource(Objects.requireNonNull(getActivity()), myVideoUri);
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    updateFragment();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {

        return false;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1, int arg2) {

    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture arg0) {

    }
}