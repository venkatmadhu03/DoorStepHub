package appsnova.com.doorstephub.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.MutableLiveData;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.vendor.VendorMyProfileActivity;
import appsnova.com.doorstephub.adapters.CitiesListAdapter;
import appsnova.com.doorstephub.models.MyBookingsModel;
import appsnova.com.doorstephub.models.vendor.SpinnerPojoVendor;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

public class ServiceScheduleActivity extends AppCompatActivity{
    EditText editText_name,editText_phone,editText_SelectedService,editText_SelectedSubService,editText_date,
            editText_description,editText_housenum,editText_colony,editText_landmark;

    AppCompatSpinner edittext_city;
    Button serviveschedulebutton;
    CheckBox serviceschedule_checkbox;
    NetworkUtils networkUtils;
    ProgressDialog progressDialog;
    SharedPref sharedPref;
    String address="",service_id="",service_name="",intent_from="",service_selection_id="",
            date="",dateInPicker="",timeInPicker="", service_selection_name="";
    Bundle bundle;
    int statusCode;
    String statusMessage;
    int year,month,dayOfMonth, hourOfDay, minute, seconds;
    Calendar calendar;
    List<MyBookingsModel> myBookingsModelList;
    String cityName="";

    ArrayAdapter arrayAdapter;
    List<String> citiesList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Transition service_schedule_transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_in);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(service_schedule_transition);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_schedule);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        new ActionBar.LayoutParams(250,90);

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
            service_name = bundle.getString("Service_Name");
            intent_from=bundle.getString("IntentFrom");
            service_selection_id=bundle.getString("serviceSelectionId");
            service_selection_name = bundle.getString("serviceSelections");
        }

        editText_name = findViewById(R.id.editText_name);
        editText_phone = findViewById(R.id.edittext_phone);
        editText_SelectedService = findViewById(R.id.editText_selectedservice);
        editText_SelectedSubService = findViewById(R.id.editText_selectedsubservice);
        editText_description = findViewById(R.id.edittext_description);
        editText_date = findViewById(R.id.edittext_date);
        editText_housenum = findViewById(R.id.edittext_housenumber);
        editText_colony = findViewById(R.id.edittext_colony);
        editText_landmark = findViewById(R.id.edittext_landmark);
        edittext_city = findViewById(R.id.edittext_city);



        editText_phone.setText(sharedPref.getStringValue("MobileNumber"));
        editText_SelectedService.setText(service_name);
        editText_SelectedSubService.setText(service_selection_name);


        serviveschedulebutton = findViewById(R.id.serviveschedulebutton);
        serviceschedule_checkbox = findViewById(R.id.serviceschedule_checkbox);
        editText_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();
            }
        });

        if (networkUtils.checkConnection()){
            getCitiesListFromServer();
        }else{
            UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
        }

        serviceschedule_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    serviveschedulebutton.setEnabled(true);
                }
                else{
                    serviveschedulebutton.setEnabled(false);
                    Toast.makeText(ServiceScheduleActivity.this, "Please Agree to Terms And Conditions..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        serviceschedule_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ServiceScheduleActivity.this);
                builder.setMessage(getResources().getString(R.string.terms_and_conditions_message));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        serviceschedule_checkbox.setEnabled(true);

                    }
                });
                builder.setCancelable(true);
                builder.show();
            }
        });

        edittext_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityName = edittext_city.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        return true;
    }

    public void getCitiesListFromServer(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.GET_CITIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Cities", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if (statusCode == 200){
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            citiesList.add(jsonObject1.getString("name"));
                        }
                        arrayAdapter = new ArrayAdapter(ServiceScheduleActivity.this, R.layout.cities_spinner_custom, citiesList);
                        edittext_city.setAdapter(arrayAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Cities", "onErrorResponse: "+error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("User_ID", sharedPref.getStringValue("User_Id"));

                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    } //end of CitiesListFromServer

    public void serviveschedulebutton(View view) {
        address=editText_housenum.getText().toString()+","+editText_colony.getText().toString()+","+editText_landmark.getText().toString()
                +","+cityName;
        Log.d("address", "serviveschedulebutton: "+address);
       /* Toast.makeText(this, "Details Saved!", Toast.LENGTH_SHORT).show();*/

//        submitDetailsToServer();



        if(editText_name.getText().toString().length()!=0 && editText_date.getText().toString().length() != 0 && editText_housenum.getText().toString().length() != 0
                && editText_colony.getText().toString().length() != 0 && cityName.length() != 0 && editText_description.getText().toString().length() !=0) {
            if(serviceschedule_checkbox.isChecked()){
                submitDetailsToServer();
            }
            else{
                Toast.makeText(ServiceScheduleActivity.this, "Please Agree to Terms And Conditions..", Toast.LENGTH_SHORT).show();
            }

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
            if(cityName.length() == 0)
            {
               UrlUtility.showCustomToast("City name required", ServiceScheduleActivity.this );
            }
            if (editText_description.getText().toString().length() == 0){
                editText_description.setError("Problem description is Required");
            }

            //  serviveschedulebutton.setEnabled(false);
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
                        Intent intent = new Intent(ServiceScheduleActivity.this,ThankYouPage.class);
                        startActivity(intent);
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
                params.put("User_ID",sharedPref.getStringValue("User_Id"));
                params.put("service_id",service_id);//first page id
                params.put("service_subcat_id",service_selection_id);
                params.put("requirement",editText_description.getText().toString());//description
                params.put("lead_from","2");//2
                params.put("address",address);
                params.put("email","");
                params.put("mobile",sharedPref.getStringValue("MobileNumber"));
                params.put("enquiry_date",editText_date.getText().toString());
                params.put("full_name",editText_name.getText().toString());
               // params.put("problem_id", )

                JSONObject jsonObject=new JSONObject(params);
                Log.d("scheduleparams", "getParams: "+jsonObject.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000*60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
