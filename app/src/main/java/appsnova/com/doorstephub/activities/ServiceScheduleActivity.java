package appsnova.com.doorstephub.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.models.MyBookingsModel;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

public class ServiceScheduleActivity extends AppCompatActivity{
    EditText editText_name,editText_phone,editText_date,
            editText_description,editText_housenum,editText_colony,editText_landmark,editText_city;
    Button serviveschedulebutton;
    CheckBox serviceschedule_checkbox;
    NetworkUtils networkUtils;
    ProgressDialog progressDialog;
    SharedPref sharedPref;
    String address="",service_id="",intent_from="",service_selection_id="",date="",dateInPicker="",timeInPicker="";
    Bundle bundle;
    int statusCode;
    String statusMessage;
    int year,month,dayOfMonth, hourOfDay, minute, seconds;
    Calendar calendar;
    List<MyBookingsModel> myBookingsModelList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /*Transition service_schedule_transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_in);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(service_schedule_transition);
        }*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_schedule);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getting current time
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        seconds = calendar.get(Calendar.SECOND);

        networkUtils=new NetworkUtils(this);
        progressDialog=UrlUtility.showProgressDialog(this);
        sharedPref=new SharedPref(this);
        myBookingsModelList=new ArrayList<>();

        bundle=getIntent().getExtras();
        if (bundle!=null){
            service_id=bundle.getString("Service_Id");
            intent_from=bundle.getString("IntentFrom");
            service_selection_id=bundle.getString("serviceSelectionId");
            Log.d("bundlevalues", "onCreate: "+service_id+","+intent_from+","+service_selection_id);
        }

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
        editText_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();
            }
        });
        serviceschedule_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    serviveschedulebutton.setEnabled(true);
                }
                else{
                    serviveschedulebutton.setEnabled(false);
                }
            }
        });
    }


    public void serviveschedulebutton(View view) {
        address=editText_housenum.getText().toString()+","+editText_colony.getText().toString()+","+editText_landmark.getText().toString()
                +","+editText_city.getText().toString();
        Log.d("address", "serviveschedulebutton: "+address);
       /* Toast.makeText(this, "Details Saved!", Toast.LENGTH_SHORT).show();*/

//        submitDetailsToServer();



        if(editText_name.getText().toString().length()!=0 && editText_date.getText().toString().length() != 0 && editText_housenum.getText().toString().length() != 0
                && editText_colony.getText().toString().length() != 0 && editText_city.getText().toString().length() != 0) {
            submitDetailsToServer();
        }
        else{

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
            }          //  serviveschedulebutton.setEnabled(false);
        }

    }
    private void submitDetailsToServer(){
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, UrlUtility.CREATE_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("submitdetailsResponse", "onResponse: "+response);
                try {
                 //   sharedPref.setStringValue("user_name",editText_name.getText().toString());
                    JSONObject jsonObject=new JSONObject(response);
                    statusCode=jsonObject.getInt("statusCode");
                    if(statusCode == 200){
                        Intent intent = new Intent(ServiceScheduleActivity.this,HomeActivity.class);
                        startActivity(intent);
                      finishAffinity();
                    }
                    statusMessage=jsonObject.getString("statusMessage");

                    Toast.makeText(ServiceScheduleActivity.this, statusMessage, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("error", "onErrorResponse: "+error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("User_ID","65");//local = 70, remote=65
                params.put("service_id",service_id);//first page id
                params.put("service_subcat_id",service_selection_id);//second page id
                params.put("requirement",editText_description.getText().toString());//description
                params.put("lead_from","2");//2
                params.put("address",address);
                params.put("email","");
                params.put("enquiry_date",editText_date.getText().toString());
                params.put("full_name",editText_name.getText().toString());


                JSONObject jsonObject=new JSONObject(params);
                Log.d("scheduleparams", "getParams: "+jsonObject.toString());
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }
    private void datePickerDialog(){
        DatePickerDialog datePicker = null;
        datePicker = new DatePickerDialog(ServiceScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
               dateInPicker = String.valueOf(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                openTimePickerDialog();
            }
        }, year, month, dayOfMonth);

        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePicker.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return super.onSupportNavigateUp();
    }

    private void openTimePickerDialog(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(ServiceScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfday, int minute) {
                 timeInPicker = String.format("%02d:%02d", hourOfday, minute);
                editText_date.setText(dateInPicker+" "+timeInPicker);

            }
        },calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)+5, false);

        timePickerDialog.show();
    }


}
