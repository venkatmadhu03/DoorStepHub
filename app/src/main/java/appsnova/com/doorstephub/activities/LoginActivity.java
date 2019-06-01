package appsnova.com.doorstephub.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnticipateInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    int statuscode;
    String statusmessage;
    int verificationStatusCode;
    String verificationStatusMessage;
    NetworkUtils networkUtils;
    ProgressDialog progressDialog;
    SharedPref sharedPref;
    Intent intent;

    EditText mobilenumber_ET,otp_ET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_login);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        new ActionBar.LayoutParams(250,90);
        //android.app.ActionBar.LayoutParams layoutParams = new android.app.ActionBar.LayoutParams(250,90);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));

        networkUtils = new NetworkUtils(this);
        progressDialog = UrlUtility.showProgressDialog(this);
        sharedPref = new SharedPref(this);

        mobilenumber_ET = findViewById(R.id.mobilenumber_edittext);
        otp_ET = findViewById(R.id.otp_edittext);

        mobilenumber_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 10) {
                    final AlertDialog.Builder confirm_Number = new AlertDialog.Builder(LoginActivity.this);
                    confirm_Number.setMessage("Are You Sure To Send OTP to this Number?"+Html.fromHtml("<font><b>" + mobilenumber_ET.getText().toString() + "</b></font>"));
                   /* confirm_Number.setItems(new CharSequence[]{"Change Number?"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });*/

                    confirm_Number.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (networkUtils.checkConnection()) {
                                progressDialog.show();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.LOGIN_URL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("LoginResponse", "onResponse: "+response);

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            statuscode = jsonObject.getInt("statusCode");
                                            statusmessage = jsonObject.getString("statusMessage");
                                            if(statuscode==200){
                                                Toast.makeText(LoginActivity.this, "OTP Has Been Sent Your MobileNumber", Toast.LENGTH_SHORT).show();
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
                                        Log.d("OTPErrorResponse", "onErrorResponse: "+error);
                                        Toast.makeText(LoginActivity.this, "OOPS SomeThing Went Wrong..", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("Mobile_Number", mobilenumber_ET.getText().toString());
                                        return params;
                                    }
                                };

                                VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
                                VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
                            }
                        }
                    });

                    confirm_Number.setNegativeButton("Change Number", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                        }
                    });
                    confirm_Number.show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void loginsuccessfull(View view) {

        if(otp_ET.getText().toString().length()==0){
            Toast.makeText(this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
        }
          //  sendingRequestForOTP();
       else{
           progressDialog.show();
           StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VERIFY_OTP_URL, new Response.Listener<String>() {
               @Override
               public void onResponse(String response) {
                   Log.d("OTPResponse", "onResponse: "+response);

                   try {
                       JSONObject jsonObject = new JSONObject(response);
                       verificationStatusCode= jsonObject.getInt("statusCode");
                       verificationStatusMessage =jsonObject.getString("statusMessage");
                       if(verificationStatusCode==200){
                           JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                           sharedPref.setStringValue("MobileNumber",jsonObject1.getString("mobile"));
                           sharedPref.setStringValue("User_Id",jsonObject1.getString("id"));
                           Log.d("User_Id", "onResponse: "+jsonObject1.getString("id"));
                           intent = new Intent(LoginActivity.this,HomeActivity.class);
                           intent.putExtra("userid",mobilenumber_ET.getText().toString());
                           startActivity(intent);
                       }
                       else{
                           Toast.makeText(LoginActivity.this, "Plz Enter the Correct OTP", Toast.LENGTH_SHORT).show();
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
                   Toast.makeText(LoginActivity.this, "OTP Verification Failed!!", Toast.LENGTH_SHORT).show();
               }
           })
           {
               @Override
               protected Map<String, String> getParams() throws AuthFailureError {
                   HashMap<String,String> params = new HashMap<>();
                   params.put("Mobile_Number",mobilenumber_ET.getText().toString());
                   params.put("otp",otp_ET.getText().toString());
                   return params;
               }
           };
            VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
            VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
        }
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
   /* private void sendingRequestForOTP() {
        String password="";
        String path = "login.bulksmsgateway.in/sendmessage.php?user=doorstephub&password="+password+
                "&mobile="+mobilenumber_ET.getText().toString()+"&message=&"+"&sender=DSLHUB"+"&type=3";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            statuscode=jsonObject.getInt("statusCode");
                            if (statuscode==200){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    @Override
    public void onBackPressed() {

        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}
