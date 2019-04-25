package appsnova.com.doorstephub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.VectorEnabledTintResources;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.plattysoft.leonids.ParticleSystem;
import com.plattysoft.leonids.modifiers.ScaleModifier;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.vendor.VendorHomeActivity;

public class Vendor_MerchantActivity extends AppCompatActivity {
    AppCompatButton button_vendor,button_customer;
    AppCompatTextView loginasTV;
    RelativeLayout loginas_RL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_merchant);

        button_vendor = findViewById(R.id.vendor_btn);
        button_customer = findViewById(R.id.customer_btn);
        loginasTV = findViewById(R.id.loginAsTV);
        loginas_RL = findViewById(R.id.loginas_RL);

        Animation animation  = AnimationUtils.loadAnimation(Vendor_MerchantActivity.this,R.anim.blink_anim);
        button_vendor.startAnimation(animation);
        button_customer.startAnimation(animation);


       /* new ParticleSystem(this, 10, R.drawable.star, 10000)
                .setSpeedByComponentsRange(-0.1f, 0.1f, -0.1f, 0.02f)
                .setAcceleration(0.000003f, 90)
                .setInitialRotationRange(0, 360)
                .setRotationSpeed(120)
                .setFadeOut(4000)
                .addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
                .oneShot(loginas_RL, 10);*/


        new ParticleSystem(this, 100, R.drawable.animated_confetti, 5000)
                .setSpeedRange(0.1f, 0.25f)
                .setRotationSpeedRange(90, 180)
                .setInitialRotationRange(0, 360)
                .oneShot(loginas_RL, 100);

    }

    public void vendor_app(View view) {
        Intent intent = new Intent(Vendor_MerchantActivity.this, VendorHomeActivity.class);
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
        ps2.oneShot(view, 70);*/
        startActivity(intent);
    }

    public void merchant_app(View view) {
    Intent intent  = new Intent(Vendor_MerchantActivity.this,LoginActivity.class);
    startActivity(intent);
    }
}