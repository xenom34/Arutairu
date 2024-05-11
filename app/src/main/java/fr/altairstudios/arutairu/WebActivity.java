package fr.altairstudios.arutairu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * WebActivity
 */
public class WebActivity extends AppCompatActivity {

    /**
     * LIFE-CYCLE -CREATE-
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        WebView webView = findViewById(R.id.web);
        webView.loadUrl(getIntent().getStringExtra("Arutairu"));//Set the URL
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.canGoBackOrForward(5);
        webView.setWebViewClient(new WebViewClient());
    }
}