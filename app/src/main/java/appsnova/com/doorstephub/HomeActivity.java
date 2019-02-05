package appsnova.com.doorstephub;

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

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView servicesCategoryRecyclerView;
    List<ServiceCategoryModel> dataList;
    HomeAdapter homeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        servicesCategoryRecyclerView = findViewById(R.id.servicesCategoryRecyclerView);
        dataList = new ArrayList<ServiceCategoryModel>();
        homeAdapter = new HomeAdapter(dataList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        servicesCategoryRecyclerView.hasFixedSize();
        servicesCategoryRecyclerView.setLayoutManager(layoutManager);
        servicesCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        servicesCategoryRecyclerView.setAdapter(homeAdapter);

        onPrepareData();
    }
    public void onPrepareData(){
        ServiceCategoryModel data =new ServiceCategoryModel("Sai");
        dataList.add(data);
        data =new ServiceCategoryModel("Sri");
        dataList.add(data);
        data =new ServiceCategoryModel("Divya");
        dataList.add(data);
        data =new ServiceCategoryModel("Teja");
        dataList.add(data);
        data =new ServiceCategoryModel("Deepthi");
        dataList.add(data);
        data =new ServiceCategoryModel("Neelu");
        dataList.add(data);
        data =new ServiceCategoryModel("Latha");
        dataList.add(data);
        data =new ServiceCategoryModel("PVR");
        dataList.add(data);
        data =new ServiceCategoryModel("Sri");
        dataList.add(data);
        data =new ServiceCategoryModel("Divya");
        dataList.add(data);
        data =new ServiceCategoryModel("Teja");
        dataList.add(data);
        data =new ServiceCategoryModel("Deepthi");
        dataList.add(data);
        data =new ServiceCategoryModel("Neelu");
        dataList.add(data);
        data =new ServiceCategoryModel("Latha");
        dataList.add(data);
        data =new ServiceCategoryModel("PVR");
        dataList.add(data);
        data =new ServiceCategoryModel("Deepthi");
        dataList.add(data);
        data =new ServiceCategoryModel("Neelu");
        dataList.add(data);
        data =new ServiceCategoryModel("Latha");
        dataList.add(data);
        data =new ServiceCategoryModel("PVR");
        dataList.add(data);


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

        }else if (id == R.id.nav_feedback) {

        }
        else if (id == R.id.nav_setting) {

        }
        else if (id == R.id.nav_discounts) {

        }
        else if (id == R.id.nav_Aboutus) {

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
