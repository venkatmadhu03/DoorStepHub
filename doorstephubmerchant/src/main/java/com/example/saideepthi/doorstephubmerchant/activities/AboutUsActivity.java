package com.example.saideepthi.doorstephubmerchant.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.saideepthi.doorstephubmerchant.R;
import com.example.saideepthi.doorstephubmerchant.adapters.ViewPagerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class AboutUsActivity extends AppCompatActivity {
   /* WebView aboutus_webview;
    ProgressBar aboutus_progressBar;*/



    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
       setTitle("AboutUs");

      /*  aboutus_progressBar = findViewById(R.id.aboutus_progressbar);
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
        }*/

      /*getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">About US </font>"));

        //custom back imageview
        final Drawable upArrow = getResources().getDrawable(R.drawable.back_arow);
        upArrow.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        viewPager = findViewById(R.id.image_viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getApplicationContext());
        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
}