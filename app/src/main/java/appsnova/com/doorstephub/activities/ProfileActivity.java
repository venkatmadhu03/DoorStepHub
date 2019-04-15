package appsnova.com.doorstephub.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
EditText name,emailid,mobile,address;
SharedPref sharedPref;
Button profileupdate;
int statusCode,profile_StatusCode;
String statusMessage,profile_StatusMessage;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPref = new SharedPref(ProfileActivity.this);
        progressDialog=UrlUtility.showProgressDialog(this);

        name   = findViewById(R.id.textView_name);
        mobile   = findViewById(R.id.textview_Phone);
        emailid   = findViewById(R.id.textviewemail);
        address   = findViewById(R.id.textview_address);
        profileupdate = findViewById(R.id.profileUpdatebtn);
        getProfile();
        mobile.setText(sharedPref.getStringValue("MobileNumber"));

       // emailid.setText(sharedPref.getStringValue("email"));
        profileupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                StringRequest stringRequest  = new StringRequest(Request.Method.POST, UrlUtility.UPDATE_PROFILE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UpdateProfile", "onResponse: "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            statusCode = jsonObject.getInt("statusCode");
                            statusMessage = jsonObject.getString("statusMessage");
                            if(statusCode == 200){
                                Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("UPError", "onErrorResponse:"+error);
                        progressDialog.dismiss();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> params = new HashMap<>();
                        params.put("UserId",sharedPref.getStringValue("User_Id"));
                        params.put("MobileNumber",sharedPref.getStringValue("MobileNumber"));
                        params.put("name",name.getText().toString());
                        params.put("email",emailid.getText().toString());
                        params.put("address",address.getText().toString());
                        JSONObject jsonObject = new JSONObject(params);
                        Log.d("jsonobject", "getParams: "+jsonObject.toString());
                        return params;
                    }
                };
                VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
                VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
            }
        });
    }


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
                        name.setText(jsonObject1.getString("name"));
                        mobile.setText(jsonObject1.getString("mobile"));
                        emailid.setText(jsonObject1.getString("email"));
                        address.setText(jsonObject1.getString("address"));
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
                Toast.makeText(ProfileActivity.this, "Error Loading Profile!!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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
    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return super.onSupportNavigateUp();
    }
}
