package com.akshatjain.codepath.news.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.akshatjain.codepath.news.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ButterKnife.bind(this);
        mWebView.setWebViewClient(new WebViewClient());
        String url = getIntent().getStringExtra("URL");
        if(url != null){
            mWebView.loadUrl(url);
        }
    }
}
