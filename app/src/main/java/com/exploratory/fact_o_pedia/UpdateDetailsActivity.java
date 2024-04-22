package com.exploratory.fact_o_pedia;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatDelegate;

//import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//import com.exploratory.fact_o_pedia.Models.Claims;

public class UpdateDetailsActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);
        webView = findViewById(R.id.webView);
        getSupportActionBar().hide();
        String updateURL = (String) getIntent().getSerializableExtra("data");
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(updateURL);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}