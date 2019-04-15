package appsnova.com.doorstephub.activities;

import androidx.appcompat.app.AppCompatActivity;
import appsnova.com.doorstephub.MainActivity;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.utilities.SharedPref;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SplashScreenActivity extends AppCompatActivity {
SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        sharedPref = new SharedPref(SplashScreenActivity.this);

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
                    }else{
                        startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                    }
                }
                else{
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                }
            }
        },3000);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onSaveInstanceState(Bundle.EMPTY);
    }

}
