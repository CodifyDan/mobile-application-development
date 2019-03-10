package com.moschd002.studentportal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        final PortalItem portalItem = (PortalItem) getIntent().getExtras().get(MainActivity.PORTAL_SELECT);

        WebView mPortalWebView = findViewById(R.id.portalWebView);
        mPortalWebView.setWebViewClient(new WebViewClient());
        String link = portalItem.getmPortalLink();

        // force https infront of url if user for example does: "klm.com".
        if (!link.contains("http")) {
            link = "https://" + link;
        }

        mPortalWebView.loadUrl(link);

        // javascript is enabled because some sites need javascript.
        mPortalWebView.getSettings().setJavaScriptEnabled(true);
        mPortalWebView.reload();
    }
}
