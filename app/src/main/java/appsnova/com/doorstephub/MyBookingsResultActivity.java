package appsnova.com.doorstephub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MyBookingsResultActivity extends AppCompatActivity {
    EditText feedback;
    Button feedback_submit;
TextView selectedorderid,selectedorder_username,selected_servicerequired,selected_subservice,selected_scheduled_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings_result);


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
}
