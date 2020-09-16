package com.riwesta.whattowatch.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.riwesta.whattowatch.models.MoviesRepository;
import com.riwesta.whattowatch.R;
import com.riwesta.whattowatch.adapters.SearchResultAdapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.ArrayList;


public class SearchResultFragment extends Fragment {

    private ArrayList<MoviesRepository> mRepList = new ArrayList<>();
    private SearchResultAdapter srAdapter;
    private ProgressBar progressBar;
    private TextView textView;
    private boolean inProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            mRepList = (ArrayList<MoviesRepository>) savedInstanceState.getSerializable("srList");
        else {
            Bundle bundle = getArguments();
            assert bundle != null;
            String genre = (String) bundle.getSerializable("genre");
            inProgress = (boolean) bundle.getSerializable("visible") ;
            getMoviesList(genre);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_result_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.sr_list);
        progressBar = view.findViewById(R.id.progressbar);
        textView = view.findViewById(R.id.pleasewait);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (inProgress)
            setProgressVisible();

        srAdapter = new SearchResultAdapter(getActivity(), mRepList);
        recyclerView.setAdapter(srAdapter);

        return view;
    }

    void getMoviesList(final String urlName) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                mRepList.clear();
                String id, name, year, rate, logo, image, imdb;
                String query;

                try {
                    String url="https://goodmovieslist.com/best-movies/" + urlName + ".html";
                    Document doc = Jsoup.connect(url)
                            .header("Accept-Encoding", "gzip, deflate")
                            .userAgent("Mozilla/5.0 " +
                                    "(Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                            .maxBodySize(0)
                            .timeout(60000000)
                            .get();

                    int size = doc.select(
                            "span[class=list_movie_localized_name Undefined]").size();

                    for (int i = 0, j = 0; i < size; i++) {
                        id = doc.select("p[class=list_movie_name]").eq(i).text().split(
                                " ")[0];
                        name = doc.select(
                                "span[class=list_movie_localized_name Undefined]").eq(i).
                                text();
                        year = doc.select("span[itemprop=datePublished]").eq(i).text();

                        query = "a[onmousedown=_gaq.push(['_trackEvent', 'External', 'IMDb', '" +
                                name.replace("'", "") + "']);]";
                        imdb = doc.select(query).attr("href");

                        if (doc.select("span[itemprop=ratingValue]").eq(i).hasAttr(
                                "content"))
                            rate = doc.select("span[itemprop=ratingValue]").eq(i).attr(
                                    "content");
                        else
                            rate = "";

                        logo = doc.select("p[class=list_movie_tagline]").eq(j).text();
                        if (!doc.select("tr[itemtype=http://schema.org/Movie]").eq(i).
                                text().contains(logo))
                            logo = "";
                        else
                            j++;

                        image = "https://goodmovieslist.com/best-movies/" + doc.select(
                                "img[class=poster]").eq(i).attr("src");

                        mRepList.add(new MoviesRepository(id, name, year, rate, logo, image, imdb));
                    }
                }
                catch (Exception e) {
                    System.out.println("Error : " + e.getMessage() + "\n");
                }

                if(getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().setRequestedOrientation(ActivityInfo
                                .SCREEN_ORIENTATION_UNSPECIFIED);
                        setProgressGone();
                        srAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    void setProgressVisible() {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        inProgress = false;
    }

    void setProgressGone() {

        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putSerializable("srList", mRepList);
    }
}