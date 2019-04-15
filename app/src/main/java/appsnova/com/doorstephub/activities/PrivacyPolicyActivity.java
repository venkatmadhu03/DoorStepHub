package appsnova.com.doorstephub.activities;

import androidx.appcompat.app.AppCompatActivity;
import appsnova.com.doorstephub.R;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class PrivacyPolicyActivity extends AppCompatActivity {
    WebView privacyPolicy_webview;
    ProgressBar privacyPolicy_progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypolicy);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode enter_transition = new Explode();
            enter_transition.setDuration(400);
            enter_transition.setInterpolator(new AccelerateInterpolator());
            getWindow().setEnterTransition(enter_transition);
        }

        setTitle("Privacy Policy");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        privacyPolicy_webview = findViewById(R.id.webview_privacypolicy);
        privacyPolicy_progressBar = findViewById(R.id.privacypolicy_progressbar);

        privacyPolicy_webview.loadUrl("https://www.doorstephub.com/privacy_policy");
        privacyPolicy_webview.getSettings().setJavaScriptEnabled(true);
        privacyPolicy_webview.getSettings().setDisplayZoomControls(true);
        privacyPolicy_webview.getSettings().setSupportZoom(true);
        privacyPolicy_webview.setWebViewClient(new PrivacyPolicyBrowser());
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return super.onSupportNavigateUp();
    }

    private class PrivacyPolicyBrowser extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("url", "onPageStarted: "+url);
            privacyPolicy_progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //  url = "http://www.google.com";
            Log.d("url", "onPageStarted: "+url);
            view.loadUrl(url);
            privacyPolicy_progressBar.setVisibility(View.GONE);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            privacyPolicy_progressBar.setVisibility(View.GONE);
        }
    }
}
