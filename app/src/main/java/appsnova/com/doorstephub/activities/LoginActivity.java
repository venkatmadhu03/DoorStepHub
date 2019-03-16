package appsnova.com.doorstephub.activities;

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


public class LoginActivity extends AppCompatActivity {
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    EditText mobilenumber_ET,otp_ET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_login);

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
        else{

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();

        }

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
