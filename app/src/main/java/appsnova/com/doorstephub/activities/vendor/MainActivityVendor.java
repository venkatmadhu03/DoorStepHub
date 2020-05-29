package appsnova.com.doorstephub.activities.vendor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;
import com.instamojo.android.models.Wallet;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.Vendor_MerchantActivity;
import appsnova.com.doorstephub.adapters.vendor.CategoryListRecyclerViewAdapter;
import appsnova.com.doorstephub.adapters.vendor.ViewPagerAdapter;
import appsnova.com.doorstephub.models.vendor.CategoryListPOJO;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivityVendor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    NetworkUtils networkUtils;
    Dialog progressDialog;
    SharedPref sharedPref;

    TextView walletBalance_valueTV, security_depositvalueTV, walletBalanceTV, securityDepositTV;
    NavigationView navigationView;
    CircleImageView navheader_imageview;

    String statusCode, statusMessage;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    List<CategoryListPOJO> categoryListPOJOList;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    private static final int IMAGE_REQUEST_1 = 1;
    private static final int CHOOSE_REQUEST_1 = 2;


    CategoryListRecyclerViewAdapter categoryListRecyclerViewAdapter;

    int latest_bookings_statusCode;
    String latest_bookings_statusMessage,file_name="", base64file="", enquiry_id="", booking_id="", statusName="";

    TextView latest_completed_Name_TV, latest_completed_City_TV, latest_completed_Description_TV, latest_completed_statusTV, latest_completed_serviceTV,
            latest_pending_Name_TV, latest_pending_City_TV, latest_pending_Description_TV, latest_pending_statusTV, latest_pending_serviceTV,
            latest_cancelled_Name_TV, latest_cancelled_City_TV, latest_cancelled_Description_TV, latest_cancelled_statusTV, latest_cancelled_serviceTV, noLatestCancelledTV,
            noLatestPendingTV, noLatestCompletedTV, latest_followup_Name_TV,
            latest_followup_Description_TV, latest_followup_statusTV, latest_followup_serviceTV, noLatestFollowUpTV;
    LinearLayout latest_completed_LL, latest_pending_LL, latest_cancelled_LL, latest_followup_LL;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkUtils = new NetworkUtils(MainActivityVendor.this);
        progressDialog = UrlUtility.showCustomDialog(MainActivityVendor.this);
        sharedPref = new SharedPref(MainActivityVendor.this);

        setContentView(R.layout.activity_main_vendor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_vendor);
        toolbar.setTitleMargin(90, 10, 0, 5);
        setSupportActionBar(toolbar);


        //hideItem();

        categoryListPOJOList = new ArrayList<>();
        categoryListRecyclerViewAdapter = new CategoryListRecyclerViewAdapter(MainActivityVendor.this, categoryListPOJOList);

        latest_completed_Name_TV = findViewById(R.id.latest_completed_textview_name);
        //    latest_completed_City_TV = findViewById(R.id.latest_completed_textview_city);
        latest_completed_Description_TV = findViewById(R.id.latest_completed_descriptionTV);
        latest_completed_statusTV = findViewById(R.id.latest_completed_statusTV);
        latest_completed_serviceTV = findViewById(R.id.latest_completed_serviceTV);
        noLatestCompletedTV = findViewById(R.id.noLatestCompletedTV);
        latest_completed_LL = findViewById(R.id.latest_completed_LL);

        latest_pending_Name_TV = findViewById(R.id.latest_pending_textview_name);
        //  latest_pending_City_TV = findViewById(R.id.latest_pending_textview_city);
        latest_pending_Description_TV = findViewById(R.id.latest_pending_descriptionTV);
        latest_pending_statusTV = findViewById(R.id.latest_pending_statusTV);
        latest_pending_serviceTV = findViewById(R.id.latest_pending_serviceTV);
        noLatestPendingTV = findViewById(R.id.noLatestPendingTV);
        latest_pending_LL = findViewById(R.id.latest_pending_LL);


        latest_cancelled_Name_TV = findViewById(R.id.latest_cancelled_textview_name);
        //  latest_cancelled_City_TV = findViewById(R.id.latest_cancelled_textview_city);
        latest_cancelled_Description_TV = findViewById(R.id.latest_cancelled_descriptionTV);
        latest_cancelled_statusTV = findViewById(R.id.latest_cancelled_statusTV);
        latest_cancelled_serviceTV = findViewById(R.id.latest_cancelled_serviceTV);
        noLatestCancelledTV = findViewById(R.id.noLatestCancelledTV);
        latest_cancelled_LL = findViewById(R.id.latest_cancelled_LL);

        //latest Followup
        latest_followup_Name_TV = findViewById(R.id.latest_followup_textview_name);
        latest_followup_Description_TV = findViewById(R.id.latest_followup_descriptionTV);
        latest_followup_statusTV = findViewById(R.id.latest_followup_statusTV);
        latest_followup_serviceTV = findViewById(R.id.latest_followup_serviceTV);
        noLatestFollowUpTV = findViewById(R.id.noLatestFollowUpTV);
        latest_followup_LL = findViewById(R.id.latest_followup_LL);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        walletBalance_valueTV = headerView.findViewById(R.id.wallet_balance_valueTV);
        security_depositvalueTV = headerView.findViewById(R.id.securityDeposit_valueTV);
        walletBalanceTV = headerView.findViewById(R.id.wallet_balance_TV);
        securityDepositTV = headerView.findViewById(R.id.securityDeposit_TV);
        navheader_imageview = headerView.findViewById(R.id.navheader_imageview);

        navigationView.setNavigationItemSelectedListener(this);

        viewPager = findViewById(R.id.image_viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.sliderDots);
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


        for (int i = 0; i < dotscount; i++) {
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

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navheader_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_dialog = new AlertDialog.Builder(MainActivityVendor.this);
                alert_dialog.setTitle("Choose Mode:");
                alert_dialog.setItems(new CharSequence[]{"Capture Image", "Choose Files"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            takePhotoFromCamera();
                        } else {
                            choosePhotoFromGallery();
                        }
                    }
                });
                alert_dialog.show();
            }
        });

        latest_completed_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkUtils.checkConnection()) {
                    Intent intent = new Intent(MainActivityVendor.this, MyBookingsVendorActivity.class);
                    intent.putExtra("intentFrom", "VendorHome");
                    intent.putExtra("leadStatus", "completed");
                    startActivity(intent);
                } else {
                    UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), MainActivityVendor.this);
                }

            }
        });

        latest_cancelled_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (networkUtils.checkConnection()) {
                    Intent intent = new Intent(MainActivityVendor.this, MyBookingsVendorActivity.class);
                    intent.putExtra("intentFrom", "VendorHome");
                    intent.putExtra("leadStatus", "cancelled");
                    startActivity(intent);
                } else {
                    UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), MainActivityVendor.this);
                }

            }
        });


        latest_pending_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkUtils.checkConnection()) {
                    Intent intent = new Intent(MainActivityVendor.this, MyBookingsVendorActivity.class);
                    intent.putExtra("intentFrom", "VendorHome");
                    intent.putExtra("leadStatus", "pending");
                    startActivity(intent);
                } else {
                    UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), MainActivityVendor.this);
                }

            }
        });
    }

    private void takePhotoFromCamera() {
        Intent cameraphoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraphoto, IMAGE_REQUEST_1);

    }

    private void choosePhotoFromGallery() {
        Intent galleryPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryPhotoIntent, CHOOSE_REQUEST_1);

    }

    private File getImagePathFromCamera(File dir, Intent myCamIntent) {
        File IMAGE_PATH = new File(dir, getApplicationContext().getResources().getString(R.string.app_name) + System.currentTimeMillis() + ".png");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myCamIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", IMAGE_PATH));
        } else {
            myCamIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(IMAGE_PATH));
        }
        return IMAGE_PATH;
    }//end of getImagePathFromCamera

    private void hideItem() {
        if (sharedPref.getStringValue("role_id").equals("4")) {
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_wallet).setVisible(false);
        }

    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();

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
        } else if (id == R.id.nav_myleads) {
            Intent intent = new Intent(MainActivityVendor.this, MyBookingsVendorActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_wallet) {

            if (networkUtils.checkConnection()) {
                Intent intent = new Intent(this, WalletActivity.class);
                startActivity(intent);
            } else {
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }
        } else if (id == R.id.nav_logout) {
            sharedPref.removeSession("mobile");
            sharedPref.removeSession("Vendor_User_id");
            Intent intent = new Intent(MainActivityVendor.this, Vendor_MerchantActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_ref_earn) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //start of profileDetails
    private void getProfileDetailsFromServer() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_GETPROFILE_URL, new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                Log.d("VendorGetProfileResponse", "onResponse: " + response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getString("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if (statusCode.equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                        String role_id = jsonObject1.getString("role_id");
                        if(jsonObject1.getString("attachment") !=null || jsonObject1.getString("attachment").isEmpty()){
                            Picasso
                                .get()
                                .load(jsonObject1.getString("attachment"))
                                .error(R.drawable.user_profile)
                                .placeholder(R.drawable.user_profile).into(navheader_imageview);
                        }else{
                            Picasso
                                    .get()
                                    .load(R.drawable.user_profile)
                                    .error(R.drawable.user_profile)
                                    .placeholder(R.drawable.user_profile).into(navheader_imageview);
                        }

                        if (role_id.equalsIgnoreCase("5") || role_id.equalsIgnoreCase("6") || role_id.equalsIgnoreCase("7")) {
                            walletBalanceTV.setText("Wallet balance");
                            walletBalance_valueTV.setText(jsonObject1.getString("wallet_balance"));
                            Log.d("MainActivityVendor", "onResponse: wallet_balance" + jsonObject1.getString("wallet_balance"));
                        } else if (role_id.equalsIgnoreCase("4")) {
                            walletBalanceTV.setText("Security deposit");
                            walletBalance_valueTV.setText(jsonObject1.getString("security_deposit"));
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
                Log.d("VendorGetProfileError", "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("User_ID", sharedPref.getStringValue("Vendor_User_id"));
                params.put("user_role", sharedPref.getStringValue("role_id"));
                Log.d("VendorParams", "getParams: " + params.toString());
                return params;
            }
        };

        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

    //end of Profile details
//start of Latest Completed Bookings
    private void getLatestCompletedBookingStatusesFromServer() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_LATEST_BOOKINGS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LatestBookingsStatus", "onResponse: " + response);
                progressDialog.dismiss();
//                main_SwipeRL.setRefreshing(false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    latest_bookings_statusCode = jsonObject.getInt("statusCode");
                    latest_bookings_statusMessage = jsonObject.getString("statusMessage");
                    if (latest_bookings_statusCode == 200) {
                        latest_completed_LL.setVisibility(View.VISIBLE);
                        noLatestCompletedTV.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                        latest_completed_Name_TV.setText("Name:" + jsonObject1.getString("user_name"));
                        latest_completed_statusTV.setText("Status:" + jsonObject1.getString("status_name"));
                        latest_completed_Description_TV.setText("Problem:" + jsonObject1.getString("requirement"));
                        latest_completed_serviceTV.setText("Service:" + jsonObject1.getString("name"));
                    } else {
                        latest_completed_LL.setVisibility(View.GONE);
                        noLatestCompletedTV.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("User_ID", sharedPref.getStringValue("Vendor_User_id"));
                params.put("status_id", "4");
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

    //end of LatestCompletedBookings
//start of latestPEnding Bookings
    private void getLatestPendingBookingStatusesFromServer() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_LATEST_BOOKINGS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
//                main_SwipeRL.setRefreshing(false);
                try {
                    Log.d("MainActivity", "onResponse:LatetPending " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    latest_bookings_statusCode = jsonObject.getInt("statusCode");
                    latest_bookings_statusMessage = jsonObject.getString("statusMessage");
                    if (latest_bookings_statusCode == 200) {
                        latest_pending_LL.setVisibility(View.VISIBLE);
                        noLatestPendingTV.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        latest_pending_Name_TV.setText("Name:" + jsonObject1.getString("user_name"));
                        latest_pending_Description_TV.setText("Problem:" + jsonObject1.getString("requirement"));
                        latest_pending_statusTV.setText("Status:" + jsonObject1.getString("status_name"));
                        latest_pending_serviceTV.setText("Service:" + jsonObject1.getString("name"));
                    } else {
                        latest_pending_LL.setVisibility(View.GONE);
                        noLatestPendingTV.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("User_ID", sharedPref.getStringValue("Vendor_User_id"));
                params.put("status_id", "1");
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

    //end of LatestPending Bookings
//start of latestCancelled bookings
    private void getLatestCancelledBookingStatusesFromServer() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_LATEST_BOOKINGS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LatestBookingsStatus", "onResponse:Cancelled " + response);
                progressDialog.dismiss();
//                main_SwipeRL.setRefreshing(false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    latest_bookings_statusCode = jsonObject.getInt("statusCode");
                    latest_bookings_statusMessage = jsonObject.getString("statusMessage");
                    if (latest_bookings_statusCode == 200) {
                        latest_cancelled_LL.setVisibility(View.VISIBLE);
                        noLatestCancelledTV.setVisibility(View.GONE);

                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        latest_cancelled_Name_TV.setText("Name:" + jsonObject1.getString("user_name"));
                        latest_cancelled_Description_TV.setText("Problem:" + jsonObject1.getString("requirement"));
                        latest_cancelled_statusTV.setText("Status:" + jsonObject1.getString("status_name"));
                        latest_cancelled_serviceTV.setText("Service:" + jsonObject1.getString("name"));
                    } else {
                        latest_cancelled_LL.setVisibility(View.GONE);
                        noLatestCancelledTV.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("User_ID", sharedPref.getStringValue("Vendor_User_id"));
                params.put("status_id", "5");
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

    private void getLatestFollowUpBookingStatusesFromServer() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_LATEST_BOOKINGS_URL, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                Log.d("LatestBookingsStatus", "onResponse: " + response);
                progressDialog.dismiss();
//                main_SwipeRL.setRefreshing(false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    latest_bookings_statusCode = jsonObject.getInt("statusCode");
                    latest_bookings_statusMessage = jsonObject.getString("statusMessage");

                    //accepted_date

                    if (latest_bookings_statusCode == 200) {
                        latest_completed_LL.setVisibility(View.VISIBLE);
                        noLatestCompletedTV.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                        latest_followup_Name_TV.setText("Name:" + jsonObject1.getString("user_name"));
                        latest_followup_statusTV.setText("Status:" + jsonObject1.getString("status_name"));
                        latest_followup_Description_TV.setText("Problem:" + jsonObject1.getString("requirement"));
                        latest_followup_serviceTV.setText("Service:" + jsonObject1.getString("name"));

                        String accepted_date = jsonObject1.getString("accepted_date");
                        enquiry_id = jsonObject1.getString("enquiry_id");
                        booking_id = jsonObject1.getString("booking_id");
                        statusName = jsonObject1.getString("status_name");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date mDate = sdf.parse(accepted_date);
                            long timeInMilliseconds = (mDate.getTime())/1000;
                            long currentTimeInMillis = System.currentTimeMillis() / 1000L;
                            System.out.println("Date in milli : " + timeInMilliseconds+","+currentTimeInMillis+","+(timeInMilliseconds+720));

                            if(currentTimeInMillis > (timeInMilliseconds+720) &&  (currentTimeInMillis) < (timeInMilliseconds+1440))
                            {
                                buildAlertDialog();
                            }else if(currentTimeInMillis > (timeInMilliseconds+1440)){
                                deductAmountFromServer(enquiry_id, statusName, booking_id);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    } else {
                        latest_followup_LL.setVisibility(View.GONE);
                        noLatestFollowUpTV.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("User_ID", sharedPref.getStringValue("Vendor_User_id"));
                params.put("status_id", "3");
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }



    private void buildAlertDialog(){
        androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Alert!!!");
        builder.setMessage("It's been 12 hrs you accepted the lead but not completed. If Completed Kindly upload the invoice bill..");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void deductAmountFromServer(final String enquiry_id, final String statusName, final String bookingId) {
        //progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_BOOKINGS_DEDUCTBALANCE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DeductBalanceResponse", "onResponse: "+response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(mcontext, error.toString(), Toast.LENGTH_SHORT).show();
               // progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("User_ID",sharedPref.getStringValue("Vendor_User_id"));
                params.put("user_role",sharedPref.getStringValue("role_id"));
                params.put("amount", String.valueOf(75));
                params.put("status",statusName);
                params.put("booking_id", bookingId);
                params.put("enquiry_id", enquiry_id);
                Log.d("deductparams", "getParams:AnsweredFragment "+params.toString());
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == IMAGE_REQUEST_1 && resultCode == RESULT_OK) {
                Uri contentURI1 = data.getData();
                String selectedImagePath  = getImagePath(contentURI1);
                File apFrontfile = new File(selectedImagePath);
                base64file=convertFileToBase64(Uri.fromFile(apFrontfile));
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //navheader_imageview.setImageBitmap(photo);
                file_name = System.currentTimeMillis()+".jpg";
            }
            else if (requestCode == CHOOSE_REQUEST_1) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    String selectedImagePath = getImagePath(contentURI);
                    File apFrontfile = new File(selectedImagePath);
                    base64file=convertFileToBase64(Uri.fromFile(apFrontfile));
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                   // navheader_imageview.setImageBitmap(bitmap);
                    file_name = System.currentTimeMillis()+".jpg";
                }
            }

            if (!base64file.isEmpty()){
                if (networkUtils.checkConnection()){
                    sendProfileImageToServer(base64file, file_name);
                }
            }

        }

    }

    private void sendProfileImageToServer(final String base64file, final String file_name){
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.UPDATE_PROFILE_IMAGE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ProfilePhoto", "onResponse: "+response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("statusCode") ==  200){
                        progressDialog.dismiss();
                        UrlUtility.showCustomToast(jsonObject.getString("statusMessage"), MainActivityVendor.this);
                        getProfileDetailsFromServer();
                    }else{
                        progressDialog.dismiss();
                        UrlUtility.showCustomToast(jsonObject.getString("statusMessage"), MainActivityVendor.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Hello", "onErrorResponse: "+error.toString());
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", sharedPref.getStringValue("Vendor_User_id"));
                params.put("profile_photo", base64file);
                params.put("file_name", file_name);
                params.put("user_role", "merchant");

                Log.d("ProfileParams", "getParams: "+new JSONObject(params).toString());

                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

    private String convertFileToBase64(Uri contentUri) {
        byte[] byteArray = new byte[1024*11];

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(contentUri);
            byte[] b = new byte[1024 * 11];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                byteArrayOutputStream.write(b, 0, bytesRead);
            }

            byteArray = byteArrayOutputStream.toByteArray();

            Log.e("Byte array", ">" + byteArray);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String base64_file = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64_file;
    }

    public String getImagePath(Uri uri){
        String[] imageprojection = { MediaStore.Images.Media.DATA};
        Cursor image_cursor = getContentResolver().query(uri,imageprojection,null,null,null);
        if(image_cursor != null){
            int column_index = image_cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            image_cursor.moveToFirst();
            return image_cursor.getString(column_index);
        }
        else{
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (networkUtils.checkConnection()) {
            getProfileDetailsFromServer();
            getLatestCompletedBookingStatusesFromServer();
            getLatestPendingBookingStatusesFromServer();
            getLatestCancelledBookingStatusesFromServer();
            getLatestFollowUpBookingStatusesFromServer();
        }

    }
}

