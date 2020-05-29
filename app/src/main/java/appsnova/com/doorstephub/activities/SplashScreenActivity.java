package appsnova.com.doorstephub.activities;

import androidx.appcompat.app.AppCompatActivity;
import appsnova.com.doorstephub.MainActivity;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.vendor.MainActivityVendor;
import appsnova.com.doorstephub.services.MyFirebaseMessagingService;
import appsnova.com.doorstephub.utilities.SharedPref;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashScreenActivity extends AppCompatActivity {
SharedPref sharedPref;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        startService(new Intent(this, MyFirebaseMessagingService.class));

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        sharedPref = new SharedPref(SplashScreenActivity.this);

        imageView = findViewById(R.id.imageView);

        final float growTo = 1.2f;
        final long duration = 800;

        /*ScaleAnimation grow = new ScaleAnimation(1, growTo, 1, growTo,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        grow.setDuration(duration / 2);
        grow.setRepeatMode(Animation.REVERSE);
        grow.setRepeatCount(Animation.INFINITE);


        ScaleAnimation shrink = new ScaleAnimation(growTo, 1, growTo, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(duration / 2);
        shrink.setStartOffset(duration / 2);
        AnimationSet growAndShrink = new AnimationSet(true);
        growAndShrink.setInterpolator(new LinearInterpolator());
        growAndShrink.addAnimation(grow);
        growAndShrink.addAnimation(shrink);*/

        //Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        //imageView.startAnimation(animation);

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 0.5f);
        scaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleYAnimator.setRepeatCount(2);
        scaleYAnimator.setDuration(1000);

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", -0.5f);
        scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleXAnimator.setRepeatCount(2);
        scaleXAnimator.setDuration(1000);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleXAnimator, scaleYAnimator);
        set.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("sharedprefValue", "run: "+sharedPref.getStringValue("isFirstOpen"));
                if(!sharedPref.getStringValue("isFirstOpen").isEmpty()){
                    if(!sharedPref.getStringValue("MobileNumber").isEmpty() &&
                            !sharedPref.getStringValue("User_Id").isEmpty()){
                        Log.d("sharedprefValue", "run: "+sharedPref.getStringValue("MobileNumber"));
                        startActivity(new Intent(SplashScreenActivity.this,HomeActivity.class));
                        return;
                    } else if(!sharedPref.getStringValue("mobile").isEmpty() &&
                            !sharedPref.getStringValue("Vendor_User_id").isEmpty()){
                        startActivity(new Intent(SplashScreenActivity.this, MainActivityVendor.class));
                    } else{
                        //startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                        startActivity(new Intent(SplashScreenActivity.this,Vendor_MerchantActivity.class));
                    }
                } else{
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                }
            }
        },2000);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        onSaveInstanceState(Bundle.EMPTY);
    }

}

/*

if(!sharedPref.getStringValue("isFirstOpen").isEmpty()){
            if(!sharedPref.getStringValue("MobileNumber").isEmpty() &&
                    !sharedPref.getStringValue("User_Id").isEmpty()){
                Log.d("sharedprefValue", "run: "+sharedPref.getStringValue("MobileNumber"));
                startActivity(new Intent(Vendor_MerchantActivity.this,HomeActivity.class));
            }else if(!sharedPref.getStringValue("vendor_mobile_number").isEmpty() &&
                    !sharedPref.getStringValue("vendor_password").isEmpty()){
                //startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                startActivity(new Intent(Vendor_MerchantActivity.this, MainActivityVendor.class));
            }
        }
        else{
            startActivity(new Intent(Vendor_MerchantActivity.this, Vendor_MerchantActivity.class));
        }



*/
