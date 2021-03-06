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
import java.util.Objects;
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

    Bundle bundle;
    String intentFrom="", status="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);

        bundle = getIntent().getExtras();

        if (bundle !=null){
            intentFrom = bundle.getString("intentFrom");
        }

        setContentView(R.layout.activity_vendor_mybookings);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("My Orders");

        tabLayout = findViewById(R.id.tablayout);
        viewPager  =findViewById(R.id.view_pager);

        setupViewPager(viewPager);
        //start fragment transaction
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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


        if (intentFrom.equalsIgnoreCase("VendorHome")){
            status = bundle.getString("leadStatus");
            if (status.equalsIgnoreCase("pending")){
                Objects.requireNonNull(tabLayout.getTabAt(0)).select();

            }else if (status.equalsIgnoreCase("completed")){
                Objects.requireNonNull(tabLayout.getTabAt(1)).select();
            }else if (status.equalsIgnoreCase("cancelled")){
                Objects.requireNonNull(tabLayout.getTabAt(2)).select();
            }
        }

    }
    private void setupViewPager(ViewPager viewPager) {
        MyBookingsViewPagerAdapter viewPagerAdapter = new MyBookingsViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new Answered_Fragment(),"Answered");
        viewPagerAdapter.addFragment(new FollowUp_Fragment(),"Follow Up");
        viewPagerAdapter.addFragment(new Completed_Fragment(),"Completed");
        viewPagerAdapter.addFragment(new Cancelled_Fragment(),"Cancelled");
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setOffscreenPageLimit(4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mybookingsmenu,menu);
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
