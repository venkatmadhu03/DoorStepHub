package appsnova.com.doorstephub;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class AboutUsActivity extends AppCompatActivity {
    WebView aboutus_webview;
    ProgressBar aboutus_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        setTitle("AboutUS");

        aboutus_progressBar = findViewById(R.id.aboutus_progressbar);
        aboutus_webview = findViewById(R.id.webview_aboutus);

        aboutus_webview.loadUrl("https://doorstephub.com/");
        aboutus_webview.getSettings().setJavaScriptEnabled(true);
        aboutus_webview.getSettings().setDisplayZoomControls(true);
        aboutus_webview.getSettings().setSupportZoom(true);
        aboutus_webview.setWebViewClient(new MyBrowser());
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("url", "onPageStarted: "+url);
            aboutus_progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
          //  url = "http://www.google.com";
            Log.d("url", "onPageStarted: "+url);
            view.loadUrl(url);
            aboutus_progressBar.setVisibility(View.GONE);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            aboutus_progressBar.setVisibility(View.GONE);
        }
    }
}
