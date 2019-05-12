package com.project.group18.limberup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Button Functionality
        setContentView(R.layout.feed_web_view);
        Intent intent = getIntent();
        String url = intent.getExtras().getString("url");


        browser =findViewById(R.id.feed_activity_web_view);
        browser.loadUrl(url);
    }
}
