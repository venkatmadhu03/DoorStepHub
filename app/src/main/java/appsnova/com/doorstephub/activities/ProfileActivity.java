package appsnova.com.doorstephub.activities;

import androidx.appcompat.app.AppCompatActivity;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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
EditText name,calendar,mobile,city,description;
SharedPref sharedPref;
Button profileupdate;
int statusCode;
String statusMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPref = new SharedPref(ProfileActivity.this);

        name   = findViewById(R.id.textView_name);
        mobile   = findViewById(R.id.textview_Phone);
        calendar   = findViewById(R.id.textviewdate);
        description   = findViewById(R.id.textview_description);
        profileupdate = findViewById(R.id.profileUpdatebtn);


        mobile.setText(sharedPref.getStringValue("MobileNumber"));
        calendar.setText(sharedPref.getStringValue("calendar"));
        profileupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest  = new StringRequest(Request.Method.POST, UrlUtility.UPDATE_PROFILE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UpdateProfile", "onResponse: "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            statusCode = jsonObject.getInt("statusCode");
                            statusMessage = jsonObject.getString("statusMessage");
                            if(statusCode == 200){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("UPError", "onErrorResponse:"+error);
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> params = new HashMap<>();
                        params.put("UserId",sharedPref.getStringValue("User_Id"));
                        params.put("MobileNumber",sharedPref.getStringValue("MobileNumber"));
                        params.put("Name",name.getText().toString());
                        params.put("Description",calendar.getText().toString());
                        return params;
                    }
                };
                VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
                VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
            }
        });


    }
}
