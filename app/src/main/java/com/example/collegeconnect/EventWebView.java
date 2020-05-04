package com.example.collegeconnect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

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


public class EventWebView extends Fragment {

    WebView webView;
    ProgressBar progressBar;
    WebSettings webSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_web_view, container, false);

        webView = view.findViewById(R.id.eventWebView);
        progressBar = view.findViewById(R.id.eventprog);

        Bundle arguments = getArguments();
        String desired_string = arguments.getString("Url");

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
            webView.loadUrl(desired_string);
            webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }
        else
        {
            // do what ever you need when when no internet connection
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
        }

        return view;
    }


}
