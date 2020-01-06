package com.example.firebasechatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ThemeActivity extends AppCompatActivity {
    private WebView mWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.stackoverflow.com")));

        mWebview = (WebView) findViewById(R.id.webView1);
        mWebview.setWebViewClient(new WebViewClient());
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        mWebview.loadUrl("https://themeforest.net/");


           // mWebview.findAllAsync("Your String");

    }
}
