package com.example.collegeconnect.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.collegeconnect.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CovidFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    WebView webView;
    ImageView imageView;
    TextView textView;
    WebSettings webSettings;
    ProgressBar progressBar;
    private AdView mAdView;

    public CovidFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_covid_fragemnt, container, false);
        if(getActivity()!=null) {
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
            fab = getActivity().findViewById(R.id.fab);
        }
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        mAdView = view.findViewById(R.id.adViewcovid);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        webView = view.findViewById(R.id.webViewcovid);
        textView = view.findViewById(R.id.tv_errorcovid);
        imageView = view.findViewById(R.id.imageViewcovid);
        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.progcovid);
        progressBar.setVisibility(View.VISIBLE);

        ConnectivityManager manager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = manager.getActiveNetworkInfo();
        boolean hasConnect = (i!= null && i.isConnected() && i.isAvailable());

        if(hasConnect)
        {
            progressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            },500);
            // show the webview
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("https://www.covid19india.org/");
            webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
        }
        else
        {
            // do what ever you need when when no internet connection
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        bottomNavigationView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }
    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.setVisibility(View.GONE);
        fab.setVisibility(View.INVISIBLE);
        bottomNavigationView.getMenu().findItem(R.id.nav_tools).setChecked(true);
    }
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
