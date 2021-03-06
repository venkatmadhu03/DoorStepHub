package appsnova.com.doorstephub.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.adapters.MyBookingsAdapter;
import appsnova.com.doorstephub.models.MyBookingsModel;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyBookingsActivity extends AppCompatActivity {

    NetworkUtils networkUtils;
    SharedPref sharedPref;
    RecyclerView mybookings_list;
    List<MyBookingsModel> myBookingsModels;
    MyBookingsAdapter myBookingsAdapter;
    SearchView searchView ;
    int status_code;
    ProgressDialog progressDialog;
    String status_message;
    TextView noBookingsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        noBookingsTextView = findViewById(R.id.noBookingsTextView);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide enter_transition = new Slide();
            enter_transition.setSlideEdge(Gravity.LEFT);
            enter_transition.setDuration(600);
            enter_transition.setInterpolator(new AnticipateOvershootInterpolator());
            getWindow().setEnterTransition(enter_transition);
        }
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
//        new ActionBar.LayoutParams(250,90);
//        //android.app.ActionBar.LayoutParams layoutParams = new android.app.ActionBar.LayoutParams(250,90);
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));

        networkUtils = new NetworkUtils(this);
        sharedPref = new SharedPref(this);
        progressDialog = UrlUtility.showProgressDialog(this);

        mybookings_list = findViewById(R.id.mybooking_list);
        myBookingsModels = new ArrayList<MyBookingsModel>();
        myBookingsAdapter = new MyBookingsAdapter(MyBookingsActivity.this, myBookingsModels, new MyBookingsAdapter.ItemClickListener() {
            @Override
            public void onClickItem(View v, int pos) {
              //  Toast.makeText(MyBookingsVendorActivity.this, myBookingsModels.get(pos).getUsername(), Toast.LENGTH_SHORT).show();
                /*if (myBookingsModels.get(pos).getStatus().equalsIgnoreCase("closed")||
                        myBookingsModels.get(pos).getStatus().equalsIgnoreCase("completed")){*/
                    Intent intent = new Intent(MyBookingsActivity.this, MyBookingsResultActivity.class);
                    intent.putExtra("selectedorderid",myBookingsModels.get(pos).getOrderid());
                    intent.putExtra("service",myBookingsModels.get(pos).getSelectedService());
                    intent.putExtra("subservice",myBookingsModels.get(pos).getSelectedSubService());
                    intent.putExtra("selectedorderusername",myBookingsModels.get(pos).getUsername());
                    intent.putExtra("selectedservicedescription",myBookingsModels.get(pos).getService_description());
                    intent.putExtra("scheduleddate",myBookingsModels.get(pos).getScheduleddate());
                    intent.putExtra("status",myBookingsModels.get(pos).getStatus());
                    intent.putExtra("appointmentId",myBookingsModels.get(pos).getAppointment_id());

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions activityOptions =ActivityOptions.makeSceneTransitionAnimation(MyBookingsActivity.this);
                        startActivity(intent,activityOptions.toBundle());
                    }

               /* }else {
                    UrlUtility.showCustomToast("cannot submit feedback until your request is completed",MyBookingsActivity.this);
                }*/

            }
        });
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(MyBookingsActivity.this);
        mybookings_list.setLayoutManager(linearLayoutManager);
        mybookings_list.setItemAnimator(new DefaultItemAnimator());
        mybookings_list.setAdapter(myBookingsAdapter);

        if (networkUtils.checkConnection()){
            mybookingsdetails();
        }

       // myBookingsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mybookingsmenu,menu);
       // MenuItem searchitem= menu.findItem(R.id.searchview);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.searchview).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>  Enter to Search </font>"));
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
                    myBookingsAdapter.notifyDataSetChanged();


                }else{
                    myBookingsAdapter.getFilter().filter(newText);
                    myBookingsAdapter.notifyDataSetChanged();
                }

                return true;

            }
        });
        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public void mybookingsdetails()
    {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.GET_BOOKINGS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyBookingResponse", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status_code = jsonObject.getInt("statusCode");
                    status_message = jsonObject.getString("statusMessage");
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    if (status_code==200) {
                        mybookings_list.setVisibility(View.VISIBLE);
                        noBookingsTextView.setVisibility(View.GONE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            MyBookingsModel myBookingsModel = new MyBookingsModel();
                            myBookingsModel.setOrderid(jsonObject1.getString("id"));
                            myBookingsModel.setSelectedService(jsonObject1.getString("service_name"));
                            myBookingsModel.setSelectedSubService(jsonObject1.getString("sub_service_name"));
                            myBookingsModel.setService_description(jsonObject1.getString("requirement"));
                            myBookingsModel.setScheduleddate(jsonObject1.getString("enquiry_date"));
                            myBookingsModel.setStatus(jsonObject1.getString("enquiry_status_value"));
                            myBookingsModel.setAppointment_id(jsonObject1.getString("appointment_id"));
                            myBookingsModels.add(myBookingsModel);
                        }
                        Log.d("mybookingslist", "onResponse: "+myBookingsModels.size());
                        myBookingsAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        mybookings_list.setVisibility(View.GONE);
                        noBookingsTextView.setVisibility(View.VISIBLE);
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
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("User_ID",sharedPref.getStringValue("User_Id"));
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);

    }
}
