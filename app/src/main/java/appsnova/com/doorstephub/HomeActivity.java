package appsnova.com.doorstephub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.adapters.HomeAdapter;
import appsnova.com.doorstephub.models.ServiceCategoryModel;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    
    //create View Objects
    RecyclerView servicesCategoryRecyclerView;
    
    //create Utils Objects
    NetworkUtils networkUtils;
    SharedPref sharedPref;
    ProgressDialog progressDialog;
    List<ServiceCategoryModel> serviceCategoryModelList;
    HomeAdapter homeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //intialie Utils Objects
        serviceCategoryModelList = new ArrayList<>();
        networkUtils = new NetworkUtils(this);
        sharedPref = new SharedPref(this);
        
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        servicesCategoryRecyclerView = findViewById(R.id.servicesCategoryRecyclerView);
        serviceCategoryModelList = new ArrayList<ServiceCategoryModel>();
        homeAdapter = new HomeAdapter(serviceCategoryModelList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        servicesCategoryRecyclerView.hasFixedSize();
        servicesCategoryRecyclerView.setLayoutManager(layoutManager);
        servicesCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        servicesCategoryRecyclerView.setAdapter(homeAdapter);

        onPrepareData();
    }
    public void onPrepareData(){
        ServiceCategoryModel data =new ServiceCategoryModel("Sai");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Sri");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Divya");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Teja");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Deepthi");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Neelu");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Latha");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("PVR");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Sri");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Divya");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Teja");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Deepthi");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Neelu");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Latha");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("PVR");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Deepthi");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Neelu");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("Latha");
        serviceCategoryModelList.add(data);
        data =new ServiceCategoryModel("PVR");
        serviceCategoryModelList.add(data);


        homeAdapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_message) {

        } else if (id == R.id.nav_mybookings) {
            if (networkUtils.checkConnection()){
                startActivity(new Intent(this, MyBookingsActivity.class));
            }else{
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }
        }else if (id == R.id.nav_feedback) {
            if (networkUtils.checkConnection()){
                startActivity(new Intent(this, FeedbackActivity.class));
            }else{
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);            }
            }
        else if (id == R.id.nav_setting) {

        }
        else if (id == R.id.nav_discounts) {
            if (networkUtils.checkConnection()){
                startActivity(new Intent(this, DiscountsActivity.class));
            }else{
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }
        }
        else if (id == R.id.nav_Aboutus) {
            if (networkUtils.checkConnection()){
                startActivity(new Intent(this, MyBookingsActivity.class));
            }else{
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }
        }
        else if (id == R.id.nav_share) {

        }
        else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
