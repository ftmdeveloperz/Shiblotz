package com.ftmdeveloperz.shiboltz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private WebView webView;
    private ProgressBar progressBar;
    private EditText passwordInput;
    private Button submitButton;
    private boolean adBlockEnabled = false; // Initially disabled

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        passwordInput = findViewById(R.id.passwordInput);
        submitButton = findViewById(R.id.submitButton);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Show progress bar while loading
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        // Load the website
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (adBlockEnabled) {
                    removeAds();
                }
            }
        });

        webView.loadUrl("https://cricktv.site");

        // Set up password input to enable ad blocking
        submitButton.setOnClickListener(view -> {
            String password = passwordInput.getText().toString();
            if (password.equals("ftmdev")) {
                adBlockEnabled = true;
                Toast.makeText(MainActivity.this, "Ad blocking enabled!", Toast.LENGTH_SHORT).show();
                removeAds();
            } else {
                Toast.makeText(MainActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to remove ads
    private void removeAds() {
        String adBlockScript = "javascript:(function() { " +
                "var elements = document.querySelectorAll('iframe, .ads, .ad-banner, .ad-container');" +
                "for(var i = 0; i < elements.length; i++) {" +
                "    elements[i].parentNode.removeChild(elements[i]);" +
                "}" +
                "})()";
        webView.loadUrl(adBlockScript);
    }
}
