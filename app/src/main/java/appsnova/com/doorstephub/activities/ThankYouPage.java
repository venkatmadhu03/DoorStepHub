package appsnova.com.doorstephub.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import appsnova.com.doorstephub.R;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

public class ThankYouPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_page);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        new ActionBar.LayoutParams(250,90);
        android.app.ActionBar.LayoutParams layoutParams = new android.app.ActionBar.LayoutParams(250,90);
       // actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent  =new Intent(ThankYouPage.this,HomeActivity.class);
                intent.putExtra("INTENT_FROM", "Thankyou");
                startActivity(intent);
                finishAffinity();
            }
        },3000);

    }
}
