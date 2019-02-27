package com.example.saideepthi.doorstephubmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.saideepthi.doorstephubmerchant.activities.AboutUsActivity;
import com.example.saideepthi.doorstephubmerchant.activities.HomeActivity;
import com.example.saideepthi.doorstephubmerchant.activities.MyBookingsActivity;
import com.example.saideepthi.doorstephubmerchant.adapters.ViewPagerAdapter;
import com.example.saideepthi.doorstephubmerchant.utils.NetworkUtils;
import com.example.saideepthi.doorstephubmerchant.utils.UrlUtility;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    NetworkUtils networkUtils;

    GridView gridView;


    int images[] = {R.drawable.background, R.drawable.background, R.drawable.background,
            R.drawable.background, R.drawable.background, R.drawable.background};
    String imagetext[] = {"Computer Services and Repairs","TV Service & Repairs","Pest Control Services","AC Service & Repairs","Refrigarator Service & Repairs","Washing Machine Service & Repairs"};
    CustomAdapter ca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setTitle("ServicePartnersActivity");

        gridView = findViewById(R.id.grid_view);

        ca = new CustomAdapter();

        gridView.setAdapter(ca);

        networkUtils = new NetworkUtils(MainActivity.this);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_myprofile) {

        } else if (id == R.id.nav_vendorship) {

        } else if (id == R.id.nav_aboutus) {
            if (networkUtils.checkConnection()){
                startActivity(new Intent(this, AboutUsActivity.class));
            }else{
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }


        }else if (id == R.id.nav_mytransactions) {
            Intent intent = new Intent(MainActivity.this, MyBookingsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_myleads) {
            /*Intent intent = new Intent(MainActivity.this,ServicePartnersActivity.class);
            startActivity(intent);*/

        }
        else if (id == R.id.nav_notifications) {
            /*Intent intent = new Intent(MainActivity.this, ServicePartnersActivity.class);
            startActivity(intent);*/

        }
        else if (id == R.id.nav_cancelleads) {

        }
        else if (id == R.id.nav_wallet) {

        }
        else if (id == R.id.nav_create_a_lead) {

        }
        else if (id == R.id.nav_share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } /*else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.main_activity_grid_row,parent,false);
            ImageView grid_image_view = convertView.findViewById(R.id.grid_image_view);
            TextView grid_textview = convertView.findViewById(R.id.gridview_text);

            grid_image_view.setImageResource(images[position]);
            grid_textview.setText(imagetext[position]);

            return convertView;
        }
    }

}
