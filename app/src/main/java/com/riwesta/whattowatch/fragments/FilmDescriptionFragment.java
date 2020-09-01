package com.riwesta.whattowatch.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.riwesta.whattowatch.R;


public class FilmDescriptionFragment extends Fragment {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.film_description, container, false);
        WebView webView = view.findViewById(R.id.webBrowser);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);


        Bundle bundle = getArguments();
        assert bundle != null;
        String imdb = (String) bundle.getSerializable("imdb");

        webView.loadUrl(imdb);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}