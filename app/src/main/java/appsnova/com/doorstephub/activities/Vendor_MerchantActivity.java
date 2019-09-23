package appsnova.com.doorstephub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.plattysoft.leonids.ParticleSystem;

import appsnova.com.doorstephub.MainActivity;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.vendor.MainActivityVendor;
import appsnova.com.doorstephub.activities.vendor.VendorHomeActivity;
import appsnova.com.doorstephub.utilities.SharedPref;

public class Vendor_MerchantActivity extends AppCompatActivity {
    AppCompatButton button_vendor,button_customer;
    AppCompatTextView loginasTV;
    Button login_submitbtn;
    RelativeLayout loginas_RL;
    SharedPref sharedPref;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    String login_type="select";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_vendor_merchant);

        button_vendor = findViewById(R.id.vendor_btn);
        button_customer = findViewById(R.id.customer_btn);
        loginas_RL = findViewById(R.id.loginas_RL);
        login_submitbtn = findViewById(R.id.login_submitbtn);
        sharedPref = new SharedPref(Vendor_MerchantActivity.this);

        Animation animation  = AnimationUtils.loadAnimation(Vendor_MerchantActivity.this,R.anim.blink_anim);
        button_vendor.startAnimation(animation);
        button_customer.startAnimation(animation);

        login_submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login_type.equalsIgnoreCase("vendor")){
                    Intent intent = new Intent(Vendor_MerchantActivity.this, VendorHomeActivity.class);
                    startActivity(intent);
                }
                else if(login_type.equalsIgnoreCase("customer")){
                    Intent intent  = new Intent(Vendor_MerchantActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Vendor_MerchantActivity.this, "Please Select Vendor or Customer..", Toast.LENGTH_SHORT).show();
                }
            }
        });

                      /*new ParticleSystem(this, 100, R.drawable.star, 10000)
                .setSpeedByComponentsRange(-0.1f, 0.1f, -0.1f, 0.02f)
                .setAcceleration(0.000003f, 90)
                .setInitialRotationRange(0, 360)
                .setRotationSpeed(120)
                .setFadeOut(4000)
                .addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
                .oneShot(loginas_RL, 100);*/
          /* new ParticleSystem(this, 100, R.drawable.animated_confetti, 5000)
                .setSpeedRange(0.1f, 0.25f)
                .setRotationSpeedRange(90, 180)
                .setInitialRotationRange(0, 360)
                .oneShot(loginas_RL, 100);*/
    }

    public void vendor_app(View view) {
       /* ParticleSystem ps = new ParticleSystem(this, 100, R.drawable.star_pink, 800);
        ps.setScaleRange(0.7f, 1.3f);
        ps.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps.setFadeOut(200, new AccelerateInterpolator());
        ps.oneShot(view, 70);

        ParticleSystem ps2 = new ParticleSystem(this, 100, R.drawable.star_white, 800);
        ps2.setScaleRange(0.7f, 1.3f);
        ps2.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps2.setFadeOut(200, new AccelerateInterpolator());
        ps2.oneShot(view, 70);
        new ParticleSystem(this, 100, R.drawable.star_pink, 1000)
                .setSpeedRange(0.2f, 0.5f)
                .oneShot(loginas_RL, 100);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button_vendor.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.login_check),null);
            }
        },600);*/

        button_vendor.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.login_check_bg_blue),null);
        button_customer.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.login_check_bg_white),null);
        login_type = "vendor";

        Animation animation =AnimationUtils.loadAnimation(Vendor_MerchantActivity.this,R.anim.blink_anim);
        login_submitbtn.setVisibility(View.VISIBLE);
        login_submitbtn.startAnimation(animation);

    }

    public void customer_app(View view) {
        /*ParticleSystem ps = new ParticleSystem(this, 100, R.drawable.star_pink, 800);
        ps.setScaleRange(0.7f, 1.3f);
        ps.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps.setFadeOut(200, new AccelerateInterpolator());
        ps.oneShot(view, 70);

        ParticleSystem ps2 = new ParticleSystem(this, 100, R.drawable.star_white, 800);
        ps2.setScaleRange(0.7f, 1.3f);
        ps2.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps2.setFadeOut(200, new AccelerateInterpolator());
        ps2.oneShot(view, 70);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button_customer.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.login_check),null);
            }
        },600);*/

        button_customer.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.login_check_bg_blue),null);
        button_vendor.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.login_check_bg_white),null);
        login_type ="customer";

        Animation animation =AnimationUtils.loadAnimation(Vendor_MerchantActivity.this,R.anim.blink_anim);
        login_submitbtn.setVisibility(View.VISIBLE);
        login_submitbtn.startAnimation(animation);
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
