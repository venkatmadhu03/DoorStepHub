package appsnova.com.doorstephub.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Visibility;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import appsnova.com.doorstephub.R;


public class MyBookingsResultActivity extends AppCompatActivity {
    EditText feedback;
    Button feedback_submit;
    TextView selectedorderid,selectedorder_username,selected_servicerequired,selected_subservice,selected_scheduled_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode enter_transition = new Explode();
            enter_transition.setDuration(400);
            enter_transition.setInterpolator(new FastOutLinearInInterpolator());
            getWindow().setEnterTransition(enter_transition);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        feedback  = findViewById(R.id.feedback_form);
        feedback_submit = findViewById(R.id.feedback_submit);
        selectedorderid  = findViewById(R.id.selected_orderid);
        selectedorder_username = findViewById(R.id.selected_orderusername);
        selected_servicerequired = findViewById(R.id.selected_servicerequired);
        selected_subservice = findViewById(R.id.selected_subservice);
        selected_scheduled_date = findViewById(R.id.selectedscheduled_date);


        Intent intent =getIntent();
        selectedorderid.setText("Order ID:"+intent.getStringExtra("selectedorderid"));
        selectedorder_username.setText("Name:"+intent.getStringExtra("selectedorderusername"));
        selected_servicerequired.setText("Service Required:"+intent.getStringExtra("selectedservicerequired"));
        selected_subservice.setText("Sub Service:"+intent.getStringExtra("selectedsubservice"));
        selected_scheduled_date.setText("Scheduled On:"+intent.getStringExtra("scheduleddate"));



        feedback_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(feedback.getText().toString()!=null){
                    Toast.makeText(MyBookingsResultActivity.this, "Thank You For Your FeedBack..", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return super.onSupportNavigateUp();
    }
}
