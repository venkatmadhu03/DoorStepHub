package appsnova.com.doorstephub;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ServiceScheduleActivity extends AppCompatActivity{
    EditText editText_name,editText_phone,editText_date,
            editText_description,editText_housenum,editText_colony,editText_landmark,editText_city;
    Button serviveschedulebutton;
    CheckBox serviceschedule_checkbox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_schedule);

        setTitle("ServiceScheduleActivity");

        editText_name = findViewById(R.id.editText_name);
        editText_phone = findViewById(R.id.edittext_phone);
        editText_description = findViewById(R.id.edittext_description);
        editText_date = findViewById(R.id.edittext_date);
        editText_housenum = findViewById(R.id.edittext_housenumber);
        editText_colony = findViewById(R.id.edittext_colony);
        editText_landmark = findViewById(R.id.edittext_landmark);
        editText_city = findViewById(R.id.edittext_city);

        serviveschedulebutton = findViewById(R.id.serviveschedulebutton);
        serviceschedule_checkbox = findViewById(R.id.serviceschedule_checkbox);


        if(editText_name.getText().toString().length() == 0)
        {
            editText_name.setError("Name is Required");
        }
        if(editText_phone.getText().toString().length() == 0 )
        {
            editText_phone.setError("PhoneNumber is Required");
        }
       /* else if(editText_phone.getText().toString().length()<10 && editText_phone.getText().toString().length()>10){
            editText_phone.setError("Please enter the valid mobile number");
        }*/
        if(editText_date.getText().toString().length() == 0)
        {
            editText_date.setError("Date is Required");
        }

        if(editText_housenum.getText().toString().length() == 0)
        {
            editText_housenum.setError("HouseNumber is Required");
        }
        if(editText_colony.getText().toString().length() == 0)
        {
            editText_colony.setError("Colony is Required");
        }
        if(editText_city.getText().toString().length() == 0)
        {
            editText_city.setError("City is Required");
        }
    }

    public void serviveschedulebutton(View view) {
        if(editText_name.getText().toString().length()!=0 && editText_phone.getText().toString().length() != 0
                && editText_date.getText().toString().length() != 0 && editText_housenum.getText().toString().length() != 0
                && editText_colony.getText().toString().length() != 0 && editText_city.getText().toString().length() != 0) {
           // serviveschedulebutton.setEnabled(true);
            Toast.makeText(this, "Details Saved!", Toast.LENGTH_SHORT).show();

        }
        else{

            Toast.makeText(this, "Please Fill The Required Fields..", Toast.LENGTH_SHORT).show();
          //  serviveschedulebutton.setEnabled(false);
        }
        }

}
