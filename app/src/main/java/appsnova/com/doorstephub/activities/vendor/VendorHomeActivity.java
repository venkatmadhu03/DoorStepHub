package appsnova.com.doorstephub.activities.vendor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//import com.example.saideepthi.doorstephubmerchant.MainActivityVendor;
//import com.example.saideepthi.doorstephubmerchant.R;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.Vendor_MerchantActivity;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;


public class VendorHomeActivity extends AppCompatActivity {
    Button sigin_btn;
    EditText mobilenumberET,passwordET;
    NetworkUtils networkUtils;
    Dialog progressDialog;
    String statusCode,statusMessage;
    String id,role_id,
            vendor_name,cname,address,
            email,mobile,wallet_balance,
            security_deposit,total_leads,remaining_leads,user_role,category_name;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_vendor);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mobilenumberET = findViewById(R.id.vendor_loginmobile);
        passwordET = findViewById(R.id.vendor_loginpassword);
        sigin_btn = findViewById(R.id.signin_btn);
        networkUtils = new NetworkUtils(VendorHomeActivity.this);
        progressDialog = UrlUtility.showCustomDialog(VendorHomeActivity.this);
        sharedPref = new SharedPref(VendorHomeActivity.this);
        sigin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localvalidation();

                sharedPref.setStringValue("vendor_mobile_number",mobilenumberET.getText().toString());
                sharedPref.setStringValue("vendor_password",passwordET.getText().toString());

            }
        });
    }
    private void localvalidation() {

        if(mobilenumberET.getText().toString().equals("") || mobilenumberET.getText().toString()==null){
            mobilenumberET.setError("MobileNumber is Required");
        }
        else if(passwordET.getText().toString().equals("") || passwordET.getText().toString() == null){
            passwordET.setError("Password is Required");
        }
        else {
            if (networkUtils.checkConnection()) {
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_LOGIN_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VendorLoginResponse", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            statusCode = jsonObject.getString("statusCode");
                            statusMessage = jsonObject.getString("statusMessage");
                            if (statusCode.equalsIgnoreCase("200")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                                id = jsonObject1.getString("id");
                                role_id = jsonObject1.getString("role_id");
                                vendor_name = jsonObject1.getString("vendor_name");
                                cname = jsonObject1.getString("cname");
                                address = jsonObject1.getString("address");
                                email = jsonObject1.getString("email");
                                mobile = jsonObject1.getString("mobile");
                                wallet_balance = jsonObject1.getString("wallet_balance");
                                security_deposit = jsonObject1.getString("security_deposit");
                                total_leads = jsonObject1.getString("total_leads");
                                remaining_leads = jsonObject1.getString("remaining_leads");
                                user_role = jsonObject1.getString("user_role");
                                category_name = jsonObject1.getString("category_name");

                                sharedPref.setStringValue("user_role", user_role);
                                sharedPref.setStringValue("role_id", role_id);
                                sharedPref.setStringValue("Vendor_User_id", id);
                                sharedPref.setStringValue("mobile", mobile);
                                sharedPref.setStringValue("vendor_name", vendor_name);

                                Log.d("Vendor_User_ID", "onResponse:"+"User_Id:"+id);

                               Intent intent  = new Intent(VendorHomeActivity.this,MainActivityVendor.class);
                               startActivity(intent);
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
                        Log.d("VendorErrorMessage", "onErrorResponse: " + error);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("Mobile_Number", mobilenumberET.getText().toString());
                        params.put("Password", passwordET.getText().toString());
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000*60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
                VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
            }
            else{
                Toast.makeText(this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
