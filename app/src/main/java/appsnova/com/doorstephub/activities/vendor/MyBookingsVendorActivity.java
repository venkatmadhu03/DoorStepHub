package appsnova.com.doorstephub.activities.vendor;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import appsnova.com.doorstephub.Answered_Fragment;
import appsnova.com.doorstephub.Cancelled_Fragment;
import appsnova.com.doorstephub.Completed_Fragment;
import appsnova.com.doorstephub.FollowUp_Fragment;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.adapters.vendor.MyBookingsViewPagerAdapter;
import appsnova.com.doorstephub.utilities.SharedPref;

public class MyBookingsVendorActivity extends AppCompatActivity {
    public static ViewPager viewPager;
    TabLayout tabLayout;
    FragmentTransaction fragmentTransaction;
    SharedPref sharedPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
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
        Log.d("User_role", "setupViewPager: "+sharedPref.getStringValue("role_id"));
        if(sharedPref.getStringValue("role_id").equalsIgnoreCase("4")){
            viewPagerAdapter.addFragment(new FollowUp_Fragment(),"Follow Up");
            viewPager.setOffscreenPageLimit(4);
        }else{
            viewPager.setOffscreenPageLimit(3);
        }
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

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return super.onSupportNavigateUp();
    }
}
