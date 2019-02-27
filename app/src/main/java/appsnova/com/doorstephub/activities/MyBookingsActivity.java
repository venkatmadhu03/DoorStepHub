package appsnova.com.doorstephub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.adapters.MyBookingsAdapter;
import appsnova.com.doorstephub.models.MyBookingsModel;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {
    RecyclerView mybookings_list;
    List<MyBookingsModel> myBookingsModels;
    MyBookingsAdapter myBookingsAdapter;
    SearchView searchView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide enter_transition = new Slide();
            enter_transition.setSlideEdge(Gravity.START);
            enter_transition.setDuration(600);
            enter_transition.setInterpolator(new AnticipateOvershootInterpolator());
            getWindow().setEnterTransition(enter_transition);
        }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mybookings_list = findViewById(R.id.mybooking_list);
        myBookingsModels = new ArrayList<MyBookingsModel>();
        myBookingsAdapter = new MyBookingsAdapter(MyBookingsActivity.this, myBookingsModels, new MyBookingsAdapter.ItemClickListener() {
            @Override
            public void onClickItem(View v, int pos) {
                Toast.makeText(MyBookingsActivity.this, myBookingsModels.get(pos).getUsername(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyBookingsActivity.this, MyBookingsResultActivity.class);
                intent.putExtra("selectedorderid",myBookingsModels.get(pos).getOrderid());
                intent.putExtra("selectedorderusername",myBookingsModels.get(pos).getUsername());
                intent.putExtra("selectedservicerequired",myBookingsModels.get(pos).getServicerequired());
                intent.putExtra("selectedsubservice",myBookingsModels.get(pos).getSubservice());
                intent.putExtra("scheduleddate",myBookingsModels.get(pos).getScheduleddate());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions activityOptions =ActivityOptions.makeSceneTransitionAnimation(MyBookingsActivity.this);
                    startActivity(intent,activityOptions.toBundle());
                }
            }
        });
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(MyBookingsActivity.this);
        mybookings_list.setLayoutManager(linearLayoutManager);
        mybookings_list.setItemAnimator(new DefaultItemAnimator());
        mybookings_list.setAdapter(myBookingsAdapter);
        mybookingsdetails();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mybookingsmenu,menu);
       // MenuItem searchitem= menu.findItem(R.id.searchview);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.searchview)
                .getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setBackgroundResource(R.drawable.gray_border);
        searchView.setQueryHint(Html.fromHtml("<font color = #000000>  Enter to Search </font>"));

        searchView.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // filter recycler view when text is changed
                if (myBookingsModels.size() != 0){
                    myBookingsAdapter.getFilter().filter(newText);
                }else{
                    myBookingsAdapter.getFilter().filter(newText);
                }

                return false;

            }
        });
        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public void mybookingsdetails()
    {
        MyBookingsModel bookingsModel = new MyBookingsModel("#834536",
                "Harbhajan Singh","Microwave Open Service and Repairs","Grill Microwave","10/6/2018 6:17:14PM","Completed");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsModel("#834544",
                "Bharath","Refrigerator Service and Repairs","Single Door Fridge repair","15/6/2018 7:17:14PM","Rejected");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsModel("#834545",
                "Swamy","Painting Service","Commercial","20/6/2018 8:17:14PM","Cancel");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsModel("#834548",
                "Singh","Computer Service and Repairs","Desktop","01/8/2018 9:17:14PM","Completed");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsModel("#834544",
                "Bharath","Refrigerator Service and Repairs","Single Door Fridge repair","15/6/2018 7:17:14PM","Rejected");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsModel("#834545",
                "Swamy","Painting Service","Commercial","20/6/2018 8:17:14PM","Cancel");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsModel("#834548",
                "Singh","Computer Service and Repairs","Desktop","01/8/2018 9:17:14PM","Completed");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsModel("#834548",
                "Singh","Computer Service and Repairs","Desktop","01/8/2018 9:17:14PM","Rejected");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsModel("#834544",
                "Bharath","Refrigerator Service and Repairs","Single Door Fridge repair","15/6/2018 7:17:14PM","Cancel");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsModel("#834545",
                "Swamy","Painting Service","Commercial","20/6/2018 8:17:14PM","Completed");
        myBookingsModels.add(bookingsModel);

        bookingsModel = new MyBookingsModel("#834548",
                "Singh","Computer Service and Repairs","Desktop","01/8/2018 9:17:14PM","Cancel");
        myBookingsModels.add(bookingsModel);


        myBookingsAdapter.notifyDataSetChanged();

    }

}
