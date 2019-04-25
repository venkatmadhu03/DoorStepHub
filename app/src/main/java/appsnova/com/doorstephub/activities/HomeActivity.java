package appsnova.com.doorstephub.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.util.Log;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.adapters.HomeAdapter;
import appsnova.com.doorstephub.models.ServiceCategoryModel;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    
    //create View Objects
    RecyclerView servicesCategoryRecyclerView;
    int statusCode,profile_StatusCode;
    String statusMessage,profile_StatusMessage;
    TextView navusername,nav_usermobilenumber;
    /*Time delay for back press*/
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    //create Utils Objects
    NetworkUtils networkUtils;
    SharedPref sharedPref;
    ProgressDialog progressDialog;
    List<ServiceCategoryModel> serviceCategoryModelList;
    HomeAdapter homeAdapter;
    String navheader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //intialie Utils Objects
        serviceCategoryModelList = new ArrayList<>();
        networkUtils = new NetworkUtils(this);
        sharedPref = new SharedPref(this);
        progressDialog=UrlUtility.showProgressDialog(this);
        
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setTitleMarginStart(280);
        toolbar.setTitleMargin(440,80,0,10);
       // toolbar.setTitleMargin((int) getResources().getDimension(R.dimen.tabtextmargin_start),(int) getResources().getDimension(R.dimen.tabtextmargin_top),0,5);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navusername= headerView.findViewById(R.id.nav_username);
        nav_usermobilenumber= headerView.findViewById(R.id.nav_usermobilenumber);

        getProfile();


          /*if(navheader==null || navheader.equals("")){
            nav_usermobilenumber.setText("To Doorstep Hub");
        }
        else{
            nav_usermobilenumber.setText(navheader);
        }*/

        navigationView.setNavigationItemSelectedListener(this);
        servicesCategoryRecyclerView = findViewById(R.id.servicesCategoryRecyclerView);
      //  serviceCategoryModelList = new ArrayList<ServiceCategoryModel>();
        homeAdapter = new HomeAdapter(serviceCategoryModelList, this);


        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
       // layoutManager.setOrientation(RecyclerView.VERTICAL);
        servicesCategoryRecyclerView.hasFixedSize();
        servicesCategoryRecyclerView.setLayoutManager(layoutManager);
        servicesCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        servicesCategoryRecyclerView.setAdapter(homeAdapter);
        if (networkUtils.checkConnection()){
            getServicesListFromServer();
        }

    } //onCreate

    private void getProfile() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.GET_PROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GetProfileResponse", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    profile_StatusCode = jsonObject.getInt("statusCode");
                    profile_StatusMessage = jsonObject.getString("statusMessage");
                    if(profile_StatusCode ==200){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                        navusername.setText(jsonObject1.getString("name"));
                        nav_usermobilenumber.setText(jsonObject1.getString("mobile"));
                        sharedPref.setStringValue("email",jsonObject1.getString("email"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "Error Loading Profile!!", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("Mobile_Number",sharedPref.getStringValue("MobileNumber"));
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

    //get Services List from server
    public void getServicesListFromServer(){
        progressDialog.show();
        serviceCategoryModelList.clear();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, UrlUtility.SERVICES_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("servicesResponse", "onResponse: "+response);
                    JSONObject jsonObject=new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    JSONArray jsonArray=jsonObject.getJSONArray("response");
                    if (statusCode==200){
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            ServiceCategoryModel serviceCategoryModel=new ServiceCategoryModel();
                            serviceCategoryModel.setId(jsonObject1.getString("id"));
                            serviceCategoryModel.setName(jsonObject1.getString("name"));
                            serviceCategoryModel.setServices_id(jsonObject1.getString("services_id"));
                            serviceCategoryModel.setService_image(jsonObject1.getString("service_image"));
                            serviceCategoryModelList.add(serviceCategoryModel);
                        }
                        homeAdapter.notifyDataSetChanged();
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
            }
        });
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);

    } //end of getServicesListFromServer
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
            }
            else {
                Toast.makeText(getApplicationContext(), "Press once again to exit!",
                        Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
       /* Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);*/
       //finishAffinity();

    } //onBackPressed

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        }  else if (id == R.id.nav_mybookings) {
            if (networkUtils.checkConnection()) {
                startActivity(new Intent(this, MyBookingsActivity.class));
            } else {
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }
        }
        else if (id == R.id.nav_Pivacy_policy) {
            if (networkUtils.checkConnection()){
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
            }else{
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }
        }
        else if (id == R.id.nav_Termscond) {
            if (networkUtils.checkConnection()){
                startActivity(new Intent(this, TermsConditionsActivity.class));
            }else{
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }
        }
        else if (id == R.id.nav_Aboutus) {
            if (networkUtils.checkConnection()){
                startActivity(new Intent(this, AboutUsActivity.class));
            }else{
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }
        }
        else if (id == R.id.nav_share) {
            String sharetext = "Hey check out my app at: https://play.google.com/store/apps/details?id="+this.getPackageName();
            Log.d("sharetext", "onNavigationItemSelected: "+sharetext);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,sharetext);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        else if (id == R.id.nav_logout) {
            sharedPref.removeSession("MobileNumber");
            sharedPref.removeSession("User_Id");
            Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    } //onNavigationItemSelected

    public void profileDetails(View view) {
        Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
        startActivity(intent);
    }
}
