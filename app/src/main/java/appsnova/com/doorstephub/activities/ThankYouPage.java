package appsnova.com.doorstephub.activities;

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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent  =new Intent(ThankYouPage.this,HomeActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        },3000);

    }
}
