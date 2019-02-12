package appsnova.com.doorstephub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.adapters.MyBookingsAdapter;
import appsnova.com.doorstephub.models.MyBookingsModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {
    RecyclerView mybookings_list;
    List<MyBookingsModel> myBookingsModels;
    MyBookingsAdapter myBookingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);


        mybookings_list = findViewById(R.id.mybooking_list);
        myBookingsModels = new ArrayList<MyBookingsModel>();
        myBookingsAdapter = new MyBookingsAdapter(MyBookingsActivity.this, myBookingsModels, new MyBookingsAdapter.ItemClickListener() {
            @Override
            public void onClickItem(View v, int pos) {
                Toast.makeText(MyBookingsActivity.this, myBookingsModels.get(pos).getUsername(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyBookingsActivity.this,MyBookingsResultActivity.class);
                intent.putExtra("selectedorderid",myBookingsModels.get(pos).getOrderid());
                intent.putExtra("selectedorderusername",myBookingsModels.get(pos).getUsername());
                intent.putExtra("selectedservicerequired",myBookingsModels.get(pos).getServicerequired());
                intent.putExtra("selectedsubservice",myBookingsModels.get(pos).getSubservice());
                intent.putExtra("scheduleddate",myBookingsModels.get(pos).getScheduleddate());
                startActivity(intent);
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
