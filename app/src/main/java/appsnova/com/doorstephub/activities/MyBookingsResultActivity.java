package appsnova.com.doorstephub.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.models.MyBookingsModel;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;


public class MyBookingsResultActivity extends AppCompatActivity {
    EditText feedback;
    RatingBar ratingBar;
    Button feedback_submit;
    Bundle bundle;
    int statusCode;
    String statusMessage;
    String service_order_id="";
    TextView selectedorderid,service,subservice,selected_servicerequired,selected_scheduled_date;
    ProgressDialog progressDialog;
    NetworkUtils networkUtils;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode enter_transition = new Explode();
            enter_transition.setDuration(400);
            enter_transition.setInterpolator(new FastOutLinearInInterpolator());
            getWindow().setEnterTransition(enter_transition);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings_result);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        new ActionBar.LayoutParams(250,90);
        //android.app.ActionBar.LayoutParams layoutParams = new android.app.ActionBar.LayoutParams(250,90);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));

        progressDialog = UrlUtility.showProgressDialog(this);
        networkUtils = new NetworkUtils(this);
        sharedPref=  new SharedPref(MyBookingsResultActivity.this);
        bundle= getIntent().getExtras();

        //initialize views
        feedback  = findViewById(R.id.feedback_form);
        ratingBar = findViewById(R.id.rating_bar);
        feedback_submit = findViewById(R.id.feedback_submit);
        selectedorderid  = findViewById(R.id.selected_orderid);
        service = findViewById(R.id.selected_service);
        subservice = findViewById(R.id.selected_subservice);
        selected_servicerequired = findViewById(R.id.selected_servicerequired);
        selected_scheduled_date = findViewById(R.id.selectedscheduled_date);

        //get Values from bundle
        if(bundle!=null){
            service_order_id =bundle.getString("selectedorderid");
            selectedorderid.setText("Order ID:"+bundle.getString("selectedorderid"));
            service.setText("Service:"+bundle.getString("service"));
            subservice.setText("Sub Service:"+bundle.getString("subservice"));
            selected_servicerequired.setText("Service Description:"+bundle.getString("selectedservicedescription"));
            selected_scheduled_date.setText("Scheduled On:"+bundle.getString("scheduleddate"));
        }
        setTitle(service_order_id);
        //check for connection and get feedback from server
        if (networkUtils.checkConnection()){
            getFeedbackFromServer();
        }else{
            Toast.makeText(this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }

        feedback_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkUtils.checkConnection()){
                    sendfeedback();
                }else{
                    Toast.makeText(MyBookingsResultActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void getFeedbackFromServer(){
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.GET_FEEDBACK_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("GetFeedbackResponse", "onResponse: "+response);
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");

                    if(statusCode == 200){
                        JSONObject responseObject= jsonObject.getJSONObject("response");
                        feedback.setText(responseObject.getString("feedback"));
                        ratingBar.setRating(responseObject.getInt("rating"));
                        feedback.setFocusable(false);
                        feedback.setEnabled(false);
                        ratingBar.setEnabled(false);
                        ratingBar.setFocusable(false);
                        ratingBar.setClickable(false);
                       // feedback_submit.setVisibility(View.GONE);
                        feedback_submit.setEnabled(false);
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
                Toast.makeText(MyBookingsResultActivity.this, "FeedBack Not Available", Toast.LENGTH_SHORT).show();
                Log.d("feedbackerror", "onErrorResponse: "+error);

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("User_ID", sharedPref.getStringValue("User_Id"));
                params.put("enquiry_id",service_order_id);
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);

    }
    public void sendfeedback(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.FEEDBACK_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        Log.d("SubmitFeedbackResponse", "onResponse: "+response);
                        JSONObject jsonObject = new JSONObject(response);
                        statusCode = jsonObject.getInt("statusCode");
                        if(statusCode ==200) {
                            Toast.makeText(MyBookingsResultActivity.this, "Thank You For Your FeedBack..", Toast.LENGTH_SHORT).show();
                            finish();
                            //  Toast.makeText(MyBookingsResultActivity.this, jsonObject.getString("statusMessage"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MyBookingsResultActivity.this, "Error Occured!!"+error, Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("User_ID",sharedPref.getStringValue("User_Id"));
                    params.put("enquiry_id",service_order_id);
                    params.put("rating", String.valueOf(ratingBar.getRating()));
                    params.put("feedback",feedback.getText().toString());
                    return params;
                }
            };
            VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
            VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
        }
    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return super.onSupportNavigateUp();
    }
}
