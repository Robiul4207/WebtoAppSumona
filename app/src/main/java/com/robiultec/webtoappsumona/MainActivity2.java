package com.robiultec.webtoappsumona;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity2 extends AppCompatActivity {
    WebView webViewDomain;
    private static String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        webViewDomain=findViewById(R.id.webViewDomainId);
        webUrl=getIntent().getStringExtra("domain");
        webViewDomain.loadUrl(webUrl);
        webViewDomain.getSettings().setJavaScriptEnabled(true);
        webViewDomain.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}