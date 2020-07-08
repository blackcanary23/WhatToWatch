package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class FilmDescriptionFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.film_description, container, false);
        WebView browser = view.findViewById(R.id.webBrowser);
        browser.getSettings().setJavaScriptEnabled(true);

        //browser.getSettings().setDomStorageEnabled(true);
        browser.getSettings().setSupportZoom(true);
        browser.getSettings().setBuiltInZoomControls(true);


        Bundle bundle = getArguments();
        assert bundle != null;
        String imdb = (String) bundle.getSerializable("data");

        //browser.setWebViewClient(new WebViewClient());
        //WebView.setWebContentsDebuggingEnabled(true);

        browser.loadUrl(imdb);

        return view;
    }
}