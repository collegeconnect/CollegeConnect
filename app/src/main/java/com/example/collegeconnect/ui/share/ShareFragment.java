package com.example.collegeconnect.ui.share;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.example.collegeconnect.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShareFragment extends Fragment {

    BottomNavigationView bottomNavigationView;
    WebView webView;
    ImageView imageView;
    TextView textView;
    WebSettings webSettings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getActivity()!=null)
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
        View view=  inflater.inflate(R.layout.fragment_loc,null);

        webView = view.findViewById(R.id.webView);
        textView = view.findViewById(R.id.tv_error);
        imageView = view.findViewById(R.id.imageView);
        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

        ConnectivityManager manager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = manager.getActiveNetworkInfo();
        boolean hasConnect = (i!= null && i.isConnected() && i.isAvailable());

        if(hasConnect)
        {
            // show the webview
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("https://rooms.dscbvp.dev/");
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }
        else
        {
            // do what ever you need when when no internet connection
            webView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }


        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://rooms.dscbvp.dev/");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        return view;

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        ((navigation)getActivity()).setTitle("Room Locator");
//
//    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.getMenu().findItem(R.id.nav_loc).setChecked(true);
    }


}