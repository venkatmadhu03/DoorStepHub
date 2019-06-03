package appsnova.com.doorstephub.activities.vendor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import appsnova.com.doorstephub.Answered_Fragment;
import appsnova.com.doorstephub.Cancelled_Fragment;
import appsnova.com.doorstephub.Completed_Fragment;
import appsnova.com.doorstephub.FollowUp_Fragment;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.adapters.vendor.MyBookingsViewPagerAdapter;

public class MyBookingsVendorActivity extends AppCompatActivity {
    /*RecyclerView mybookings_list;
    List<MyBookingsVendorModel> myBookingsModels;
    MyBookingsVendorAdapter myBookingsAdapter;*/
    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_mybookings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transactions");
        /*mybookings_list = findViewById(R.id.mybookinglist);
        myBookingsModels = new ArrayList<MyBookingsVendorModel>();
        myBookingsAdapter = new MyBookingsVendorAdapter(MyBookingsVendorActivity.this, myBookingsModels, new MyBookingsVendorAdapter.ItemClickListener() {
            @Override
            public void onClickItem(View v, int pos) {
                Toast.makeText(MyBookingsVendorActivity.this, myBookingsModels.get(pos).getUsername(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyBookingsVendorActivity.this, MyBookingsResultVendorActivity.class);
                intent.putExtra("selectedorderid",myBookingsModels.get(pos).getOrderid());
                intent.putExtra("selectedorderusername",myBookingsModels.get(pos).getUsername());
                intent.putExtra("selectedservicerequired",myBookingsModels.get(pos).getServicerequired());
                intent.putExtra("selectedsubservice",myBookingsModels.get(pos).getSubservice());
                intent.putExtra("scheduleddate",myBookingsModels.get(pos).getScheduleddate());
                startActivity(intent);
            }
        });*/
       /* LinearLayoutManager linearLayoutManager =new LinearLayoutManager(MyBookingsVendorActivity.this);
        mybookings_list.setLayoutManager(linearLayoutManager);
        mybookings_list.setItemAnimator(new DefaultItemAnimator());
        mybookings_list.setAdapter(myBookingsAdapter);
        mybookingsdetails();*/
        tabLayout = findViewById(R.id.tablayout);
        viewPager  =findViewById(R.id.view_pager);

        setupViewPager(viewPager);
        //start fragment transaction
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);

    }
    private void setupViewPager(ViewPager viewPager) {
        MyBookingsViewPagerAdapter viewPagerAdapter = new MyBookingsViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new Answered_Fragment(),"Answered");
        viewPagerAdapter.addFragment(new FollowUp_Fragment(),"Follow Up");
        viewPagerAdapter.addFragment(new Completed_Fragment(),"Completed");
        viewPagerAdapter.addFragment(new Cancelled_Fragment(),"Cancelled");


        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mybookingsmenu,menu);
        MenuItem searchitem= menu.findItem(R.id.searchview);
        SearchView searchView = (SearchView) searchitem.getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    /*public void mybookingsdetails()
    {
        MyBookingsVendorModel bookingsModel = new MyBookingsVendorModel("#834536",
                "Harbhajan Singh","Microwave Open Service and Repairs","Grill Microwave","10/6/2018 6:17:14PM","Completed");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsVendorModel("#834544",
                "Bharath","Refrigerator Service and Repairs","Single Door Fridge repair","15/6/2018 7:17:14PM","Rejected");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsVendorModel("#834545",
                "Swamy","Painting Service","Commercial","20/6/2018 8:17:14PM","Cancel");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsVendorModel("#834548",
                "Singh","Computer Service and Repairs","Desktop","01/8/2018 9:17:14PM","Completed");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsVendorModel("#834544",
                "Bharath","Refrigerator Service and Repairs","Single Door Fridge repair","15/6/2018 7:17:14PM","Rejected");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsVendorModel("#834545",
                "Swamy","Painting Service","Commercial","20/6/2018 8:17:14PM","Cancel");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsVendorModel("#834548",
                "Singh","Computer Service and Repairs","Desktop","01/8/2018 9:17:14PM","Completed");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsVendorModel("#834548",
                "Singh","Computer Service and Repairs","Desktop","01/8/2018 9:17:14PM","Rejected");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsVendorModel("#834544",
                "Bharath","Refrigerator Service and Repairs","Single Door Fridge repair","15/6/2018 7:17:14PM","Cancel");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsVendorModel("#834545",
                "Swamy","Painting Service","Commercial","20/6/2018 8:17:14PM","Completed");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsVendorModel("#834548",
                "Singh","Computer Service and Repairs","Desktop","01/8/2018 9:17:14PM","Cancel");
        myBookingsModels.add(bookingsModel);




        myBookingsAdapter.notifyDataSetChanged();

    }*/

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return super.onSupportNavigateUp();
    }
}
