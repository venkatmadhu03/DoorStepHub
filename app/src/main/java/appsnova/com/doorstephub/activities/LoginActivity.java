package appsnova.com.doorstephub.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import appsnova.com.doorstephub.R;


import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnticipateInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;


public class LoginActivity extends AppCompatActivity {
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    int statuscode;
    String statusmessage;

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

        mobilenumber_ET = findViewById(R.id.mobilenumber_edittext);
        otp_ET = findViewById(R.id.otp_edittext);


    }

    public void loginsuccessfull(View view) {


        if(mobilenumber_ET.getText().toString().length()>10 || mobilenumber_ET.getText().toString().length()<10){
            Toast.makeText(this, "Please Enter Valid MobileNumber", Toast.LENGTH_SHORT).show();
        }
        if(otp_ET.getText().toString().length()<4){
            Toast.makeText(this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
        }

        sendingRequestForOTP();
//            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();



    }

    private void sendingRequestForOTP() {
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



    }

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
