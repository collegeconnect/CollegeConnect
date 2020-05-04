package com.example.collegeconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class EventWebView extends Fragment {

    WebView webView;
    ProgressBar progressBar;
    WebSettings webSettings;
    FloatingActionButton floatingActionButton;
    ImageView imageView;
    TextView textView;
    boolean hasConnect;
    private AdView mAdView;
    String finalUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_web_view, container, false);
        if(getActivity()!=null) {
            floatingActionButton = getActivity().findViewById(R.id.createEvent);
            TextView tv = getActivity().findViewById(R.id.tvtitle);
            tv.setText("Event Details");
        }
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        mAdView = view.findViewById(R.id.adViewevent);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        webView = view.findViewById(R.id.eventWebView);
        progressBar = view.findViewById(R.id.eventprog);
        textView = view.findViewById(R.id.tv_errorevent);
        imageView = view.findViewById(R.id.imageVieweve);
        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        Bundle arguments = getArguments();
        String desired_string = arguments.getString("Url");
        if (desired_string.contains("https://"))
            finalUrl = desired_string;
        else
            finalUrl = "https://"+desired_string;


        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        hasConnect = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        hasConnect = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        hasConnect = true;
                    }
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        hasConnect = true;
                    }
                } catch (Exception e) {
                    Log.i("update_status", "" + e.getMessage());
                }
            }
        }


        if (hasConnect) {
//            progressBar.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    progressBar.setVisibility(View.GONE);
//                }
//            }, 500);
            // show the webview
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                }
            });
            webView.loadUrl(finalUrl);
            webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        } else {
            // do what ever you need when when no internet connection
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }

        return view;
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        floatingActionButton.setVisibility(View.VISIBLE);
    }
    @Override
    public void onStart() {
        super.onStart();
        floatingActionButton.setVisibility(View.INVISIBLE);
    }
}
