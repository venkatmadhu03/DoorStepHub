package appsnova.com.doorstephub.activities.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

public class WalletActivity extends AppCompatActivity implements PaymentResultListener {

    //Create Util objects
    NetworkUtils networkUtils;
    SharedPref sharedPref;
    Dialog progressDialog;

    //create View objects
    EditText rechargeWalletEdittext;
    AppCompatButton rechargeWalletButton;
    TextView walletBalanceValueTv, rewardsValueTv, remainingLeadsValueTv;

    int statusCode;
    String statusMessage = "";
    Toast toast;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //intilize utils objects
        sharedPref = new SharedPref(this);
        networkUtils = new NetworkUtils(this);
        progressDialog = UrlUtility.showCustomDialog(this);

        /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());

        setContentView(R.layout.activity_wallet);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Wallet");

        //initialize View objects
        rechargeWalletEdittext = findViewById(R.id.rechargeWalletEdittext);
        rechargeWalletButton = findViewById(R.id.rechargeWalletButton);
        walletBalanceValueTv = findViewById(R.id.walletBalanceValueTv);
        rewardsValueTv = findViewById(R.id.rewardsValueTv);
        remainingLeadsValueTv = findViewById(R.id.remainingLeadsValueTv);

        if (networkUtils.checkConnection()){
            getProfileFromServer();
        }else{
            UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
        }

        rechargeWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rechargeWalletEdittext.getText().toString().isEmpty()){
                    //if (Double.parseDouble(rechargeWalletEdittext.getText().toString()) > 7500){
                        proceedForPayment(Double.parseDouble(rechargeWalletEdittext.getText().toString()));
//                    } else{
//                        UrlUtility.showCustomToast("Recharge amount must be greater than 7500", WalletActivity.this);
//                    }
                }else{
                    UrlUtility.showCustomToast("Enter recharge amount", WalletActivity.this);
                }

            }
        });

    }

    private void getProfileFromServer() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_GETPROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("VendorProfile", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if(statusCode ==  200){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                        if (sharedPref.getStringValue("role_id").equals("4")){
                            walletBalanceValueTv.setText(jsonObject1.getString("security_deposit"));
                        }else{
                            walletBalanceValueTv.setText(jsonObject1.getString("wallet_balance"));
                        }
                        rewardsValueTv.setText(jsonObject1.getString("reward_points"));
                        remainingLeadsValueTv.setText(jsonObject1.getString("remaining_leads"));


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
                Log.d("VendorGetProfileError", "onErrorResponse: "+error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("User_ID",sharedPref.getStringValue("Vendor_User_id"));
                return params;
            }
        };

        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    } //end of getProfileFromServer

    private void proceedForPayment(double rechargeAmount){
        if (networkUtils.checkConnection()){
            startPayment(rechargeAmount);
        }else{
            UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
        }
    }

    private void startPayment(double rechargeAmount){
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", sharedPref.getStringValue("vendor_name"));
            options.put("description", "Wallet recharge");
            options.put("image", "https://www.doorstephub.com//themes/dhb/img/logo.png");
            options.put("currency", "INR");
            options.put("amount", String.format("%.0f", rechargeAmount*100));

            JSONObject preFill = new JSONObject();

            preFill.put("email", "");
            preFill.put("contact", sharedPref.getStringValue("mobile"));

            options.put("prefill", preFill);

            Log.d("CompleteBilling", "startPayment: "+options.toString());

            co.open(activity, options);
        } catch (Exception e) {
            UrlUtility.showCustomToast("Error in Payment", this);
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        UrlUtility.showCustomToast("Payment Success "+s, this);
        sendSuccessRequestToServer(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        UrlUtility.showCustomToast("Payment Failure "+s, this);
    }

    private void sendSuccessRequestToServer(final String transaction_id){
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.PAYMENT_SUCCESS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("WalletRecharge", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if (statusCode ==  200){
                        UrlUtility.showCustomToast(statusMessage, WalletActivity.this);
                        finish();
                    }else{
                        UrlUtility.showCustomToast(statusMessage, WalletActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("WalletRecharge", "onErrorResponse: "+error.toString());
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",sharedPref.getStringValue("Vendor_User_id"));
                params.put("role_id",sharedPref.getStringValue("role_id"));
                params.put("recharge_amount", rechargeWalletEdittext.getText().toString());
                params.put("transaction_id", transaction_id);
                params.put("booking_id", "0");

                Log.d("WalletRecharge", "getParams: "+new JSONObject(params));

                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);

    }
}
