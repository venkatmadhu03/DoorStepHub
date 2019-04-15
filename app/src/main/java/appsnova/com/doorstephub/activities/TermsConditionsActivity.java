package appsnova.com.doorstephub.activities;

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

import androidx.appcompat.app.AppCompatActivity;
import appsnova.com.doorstephub.R;

public class TermsConditionsActivity extends AppCompatActivity {
    WebView  termsConditions_webview;
    ProgressBar termsconditions_progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode enter_transition = new Explode();
            enter_transition.setDuration(400);
            enter_transition.setInterpolator(new AccelerateInterpolator());
            getWindow().setEnterTransition(enter_transition);
        }

        setTitle("Terms & Conditions");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        termsConditions_webview = findViewById(R.id.webview_termsconditions);
        termsconditions_progressbar = findViewById(R.id.termsconditions_progressbar);

        termsConditions_webview.loadUrl("https://www.doorstephub.com/Terms_conditions");
        termsConditions_webview.getSettings().setJavaScriptEnabled(true);
        termsConditions_webview.getSettings().setDisplayZoomControls(true);
        termsConditions_webview.getSettings().setSupportZoom(true);
        termsConditions_webview.setWebViewClient(new TermsAndConditionsBrowser());
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return super.onSupportNavigateUp();
    }

    private class TermsAndConditionsBrowser extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("url", "onPageStarted: "+url);
            termsconditions_progressbar.setVisibility(View.VISIBLE);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //  url = "http://www.google.com";
            Log.d("url", "onPageStarted: "+url);
            view.loadUrl(url);
            termsconditions_progressbar.setVisibility(View.GONE);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            termsconditions_progressbar.setVisibility(View.GONE);
        }
    }
}
