package com.college.collegeconnect.ui.event;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.college.collegeconnect.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URISyntaxException;


public class EventWebView extends Fragment {

    WebView webView;
    ProgressBar progressBar;
    WebSettings webSettings;
    FloatingActionButton floatingActionButton;
    ImageView imageView;
    TextView textView, textslow;
    boolean hasConnect;
    private AdView mAdView;
    String finalUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        String desired_string = arguments.getString("Url");
        if (desired_string.contains("https://"))
            finalUrl = desired_string;
        else
            finalUrl = "https://" + desired_string;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_web_view, container, false);

        mAdView = view.findViewById(R.id.adViewevent);
        webView = view.findViewById(R.id.eventWebView);
        progressBar = view.findViewById(R.id.eventprog);
        textView = view.findViewById(R.id.tv_errorevent);
        imageView = view.findViewById(R.id.imageVieweve);
        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        textslow = view.findViewById(R.id.texterrorevent);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            floatingActionButton = getActivity().findViewById(R.id.createEvent);
            TextView tv = getActivity().findViewById(R.id.tvtitle);
            tv.setText("Event Details");
        }
        MobileAds.initialize(getContext());
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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
        textslow.setVisibility(View.GONE);
        if (hasConnect) {
//            progressBar.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    progressBar.setVisibility(View.GONE);
//                }
//            }, 500);
            // show the webview
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
//                    textslow.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            textslow.setVisibility(View.VISIBLE);
//                        }
//                    },3500);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                    textslow.setVisibility(View.GONE);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    Uri uri = request.getUrl();
                    if (uri.toString().startsWith("intent://")) {
                        Intent intent = null;
                        try {
                            intent = Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        if (intent != null) {
                            String fallbackurl = intent.getStringExtra("browser_fallback_url");
                            if (fallbackurl != null) {
                                webView.loadUrl(fallbackurl);
                                return true;
                            } else
                                return false;

                        }
                    }
                    return super.shouldOverrideUrlLoading(view, request);

                }

            });
            webView.loadUrl(finalUrl);
            webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webView.canGoBack();
            webView.setOnKeyListener(new View.OnKeyListener() {

                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && event.getAction() == MotionEvent.ACTION_UP
                            && webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                    return false;
                }
            });
        } else {
            // do what ever you need when when no internet connection
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        floatingActionButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        floatingActionButton.setVisibility(View.INVISIBLE);
    }
}

