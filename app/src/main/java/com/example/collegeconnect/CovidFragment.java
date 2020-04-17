package com.example.collegeconnect;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_covid_fragemnt, container, false);
        if(getActivity()!=null) {
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
            fab = getActivity().findViewById(R.id.fab);
        }
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
}
