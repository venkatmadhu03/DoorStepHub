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



public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_login);

        setUpWindowTransitions();

    }

    public void loginsuccessfull(View view) {

        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    public void setUpWindowTransitions()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Slide slide_transition = new Slide();
            slide_transition.setDuration(1000);
            slide_transition.setSlideEdge(Gravity.BOTTOM);
            slide_transition.setInterpolator(new AnticipateInterpolator());

            getWindow().setReenterTransition(slide_transition);

            getWindow().setExitTransition(slide_transition);

            getWindow().setAllowReturnTransitionOverlap(false);

        }

    }


}
