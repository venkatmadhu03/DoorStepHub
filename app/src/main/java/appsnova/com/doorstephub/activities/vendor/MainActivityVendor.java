package appsnova.com.doorstephub.activities.vendor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import appsnova.com.doorstephub.MainActivity;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.HomeActivity;
import appsnova.com.doorstephub.activities.ThankYouPage;
import appsnova.com.doorstephub.adapters.vendor.CategoryListRecyclerViewAdapter;
import appsnova.com.doorstephub.adapters.vendor.ViewPagerAdapter;
import appsnova.com.doorstephub.models.vendor.CategoryListPOJO;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

public class MainActivityVendor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    NetworkUtils networkUtils;
    GridView gridView;
    RecyclerView category_recyclerView;
    ProgressDialog progressDialog;
    TextView walletBalance_valueTV,security_depositvalueTV,walletBalanceTV,securityDepositTV;
    String statusCode,statusMessage;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    List<CategoryListPOJO> categoryListPOJOList;
    CategoryListPOJO categoryListPOJO;
    CategoryListRecyclerViewAdapter categoryListRecyclerViewAdapter;
    /*int images[] = {R.drawable.background, R.drawable.background, R.drawable.background,
            R.drawable.background, R.drawable.background, R.drawable.background};
    String imagetext[] = {"Computer Services and Repairs","TV Service & Repairs",
            "Pest Control Services","AC Service & Repairs",
            "Refrigarator Service & Repairs",
            "Washing Machine Service & Repairs"};
    CustomAdapter ca;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_vendor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_vendor);
        toolbar.setTitleMargin(90,10,0,5);
        setSupportActionBar(toolbar);

    //    gridView = findViewById(R.id.grid_view);
        category_recyclerView = findViewById(R.id.category_RV);
        categoryListPOJOList = new ArrayList<>();
        categoryListRecyclerViewAdapter = new CategoryListRecyclerViewAdapter(MainActivityVendor.this,categoryListPOJOList);
    //   ca = new CustomAdapter();

    //   gridView.setAdapter(ca);
        networkUtils = new NetworkUtils(MainActivityVendor.this);
        progressDialog = UrlUtility.showProgressDialog(MainActivityVendor.this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        walletBalance_valueTV= headerView.findViewById(R.id.wallet_balance_valueTV);
        security_depositvalueTV= headerView.findViewById(R.id.securityDeposit_valueTV);
        walletBalanceTV= headerView.findViewById(R.id.wallet_balance_TV);
        securityDepositTV = headerView.findViewById(R.id.securityDeposit_TV);
        if (networkUtils.checkConnection()){
            getCategoryListFromServer();
        }
        getProfileDetailsFromServer();

        navigationView.setNavigationItemSelectedListener(this);

        viewPager = findViewById(R.id.image_viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getApplicationContext());
        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        NUM_PAGES = viewPagerAdapter.getCount();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);


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
        category_recyclerView.setAdapter(categoryListRecyclerViewAdapter);
        category_recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivityVendor.this,2);
        category_recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void getCategoryListFromServer() {
        categoryListPOJO = new CategoryListPOJO("Computer Services and Repairs",R.drawable.vendor_upload_image);
        categoryListPOJOList.add(categoryListPOJO);
        categoryListPOJO = new CategoryListPOJO("Hardware Repairs",R.drawable.vendor_upload_image);
        categoryListPOJOList.add(categoryListPOJO);
        categoryListPOJO = new CategoryListPOJO("Software Repairs",R.drawable.vendor_upload_image);
        categoryListPOJOList.add(categoryListPOJO);
        categoryListPOJO = new CategoryListPOJO("Refrigirator Repairs",R.drawable.vendor_upload_image);
        categoryListPOJOList.add(categoryListPOJO);
        categoryListPOJO = new CategoryListPOJO("AC Repairs",R.drawable.vendor_upload_image);
        categoryListPOJOList.add(categoryListPOJO);
        categoryListPOJO = new CategoryListPOJO("Tv Repairs",R.drawable.vendor_upload_image);
        categoryListPOJOList.add(categoryListPOJO);

        categoryListRecyclerViewAdapter.notifyDataSetChanged();

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
        if (id == R.id.nav_myprofile) {
            Intent intent = new Intent(MainActivityVendor.this, VendorMyProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus) {
            if (networkUtils.checkConnection()) {
                startActivity(new Intent(this, AboutUsVendorActivity.class));
            } else {
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }
        }
        else if (id == R.id.nav_myleads) {
            Intent intent = new Intent(MainActivityVendor.this, MyBookingsVendorActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_wallet) {

        }
        else if(id == R.id.nav_ref_earn){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        else if (id == R.id.nav_share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /*private class CustomAdapter extends BaseAdapter {
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
            convertView = getLayoutInflater().inflate(R.layout.main_activity_grid_row_vendor,parent,false);
            ImageView grid_image_view = convertView.findViewById(R.id.grid_image_view);
            TextView grid_textview = convertView.findViewById(R.id.gridview_text);
            grid_image_view.setImageResource(images[position]);
            grid_textview.setText(imagetext[position]);
            return convertView;
        }
    }*/
    private void getProfileDetailsFromServer() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_GETPROFILE_URL, new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                Log.d("VendorGetProfileResponse", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getString("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if(statusCode.equalsIgnoreCase("200")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                        String role_id = jsonObject1.getString("role_id");
                        if(role_id.equalsIgnoreCase("5")){
                            securityDepositTV.setVisibility(View.GONE);
                            security_depositvalueTV.setVisibility(View.GONE);
                            walletBalance_valueTV.setText(jsonObject1.getString("wallet_balance"));
                        }else if(role_id.equalsIgnoreCase("4")){
                            walletBalanceTV.setVisibility(View.GONE);
                            walletBalance_valueTV.setVisibility(View.GONE);
                            security_depositvalueTV.setText(jsonObject1.getString("security_deposit"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("VendorGetProfileError", "onErrorResponse: "+error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("User_ID","21");
                return params;
            }
        };

        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }



}
