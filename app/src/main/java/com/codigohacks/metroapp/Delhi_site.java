package com.codigohacks.metroapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Delhi_site extends AppCompatActivity {

    WebView route_page;
    ProgressBar progressBar;

    public class MyJavaScriptInterface {

        private Context ctx;
        public String html;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String _html) {
            html = _html;
        }
    }
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delhi_site);

        route_page = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar)findViewById(R.id.webprogress);
        route_page.getSettings().setJavaScriptEnabled(true);
        route_page.getSettings().setSupportMultipleWindows(true);
        route_page.loadUrl("https://www.google.com");

        route_page.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                }
            }
        });
        route_page.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }
}

