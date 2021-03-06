package appsnova.com.doorstephub.adapters.vendor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.RadialGradient;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appsnova.com.doorstephub.Answered_Fragment;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.HomeActivity;
import appsnova.com.doorstephub.activities.vendor.MyBookingsVendorActivity;
import appsnova.com.doorstephub.activities.vendor.VendorMyProfileActivity;
import appsnova.com.doorstephub.models.CancelledReasonPOJO;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

public class Answer_Recyclerview_Adapter extends RecyclerView.Adapter<Answer_Recyclerview_Adapter.MyViewHolder>{
    Context mcontext;
    List<MyLeadsPojo> myLeadsPojoList;
    SharedPref sharedPref;
    Dialog progressDialog;
    int statusCode,cancelled_reson_statusCode;
    String statusMessage,cancelled_reason_statusMessage;
    List<CancelledReasonPOJO> cancelledReasonPOJOList;
    LinearLayout.LayoutParams layoutParams;
    RadioGroup radioGroup;
    RadioButton dynamic_radiobutton;
    String selectedRadioButtonValue ="", selectedRadioButtonId="";
    double standard_amount = 150;
    double eighteen_percent_deduction;
    double final_amount;
    int deduct_statusCode;
    String deduct_statusMessage="";

    NetworkUtils networkUtils;

    public Answer_Recyclerview_Adapter(List<MyLeadsPojo> myLeadsPojoList, Context mcontext) {
        this.myLeadsPojoList = myLeadsPojoList;
        this.mcontext = mcontext;
        progressDialog = UrlUtility.showCustomDialog(mcontext);
        sharedPref = new SharedPref(mcontext);
        networkUtils = new NetworkUtils(mcontext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.answered_fragment_list_row,viewGroup,false);
        cancelledReasonPOJOList = new ArrayList<>();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int pos) {
        final MyLeadsPojo myLeadsPojo = myLeadsPojoList.get(pos);
        Log.d("Acceptedlistsize", "onBindViewHolder: "+myLeadsPojoList.size());
        Log.d("AcceptedlistName", "onBindViewHolder: "+myLeadsPojo.getName());
        myViewHolder.textView_name.append(" "+myLeadsPojo.getName());
        myViewHolder.textView_city.append(" "+myLeadsPojo.getService());
        myViewHolder.textView_description.append(" "+myLeadsPojo.getDescription());
        myViewHolder.appointmentid_textview.append(" "+myLeadsPojo.getDescription());

        getCancelledBookingsReasonList();

        myViewHolder.answered_accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String userRole_id = sharedPref.getStringValue("role_id");
               Log.d("role_id", "onClick: "+userRole_id);

               buildAlertBox(myLeadsPojoList.get(pos).getBooking_id(),
                       myLeadsPojoList.get(pos).getStatus_name(), myLeadsPojoList.get(pos).getEnquiry_id(), 250);


               /*if(userRole_id.equalsIgnoreCase("5")){
                   deductAmountResultFromServer(myLeadsPojoList.get(pos).getBooking_id(),
                           myLeadsPojoList.get(pos).getStatus_name(), myLeadsPojoList.get(pos).getEnquiry_id(), 250);

               }else if(userRole_id.equalsIgnoreCase("6")){
                   deductAmountResultFromServer(myLeadsPojoList.get(pos).getBooking_id(),
                           myLeadsPojoList.get(pos).getStatus_name(), myLeadsPojoList.get(pos).getEnquiry_id(), 250);

               }else if(userRole_id.equalsIgnoreCase("7")){
                   deductAmountResultFromServer(myLeadsPojoList.get(pos).getBooking_id(),
                           myLeadsPojoList.get(pos).getStatus_name(), myLeadsPojoList.get(pos).getEnquiry_id(), 250);

               }else{
                   getAcceptedBookingFromServer(myLeadsPojo.getBooking_id(),
                           myLeadsPojo.getStatus_name(), myLeadsPojoList.get(pos).getEnquiry_id());
               }*/

            }
        });
        myViewHolder.button_rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reasonForCancellation(myLeadsPojoList.get(pos).getBooking_id(),
                        myLeadsPojoList.get(pos).getStatus_name(), myLeadsPojoList.get(pos).getEnquiry_id());
            }
        });

    }



    private void buildAlertBox(final String bookingId, final String statusName, final String enquiryId, final int amount){
        final AlertDialog.Builder alert_dialog = new AlertDialog.Builder(mcontext);
        alert_dialog.setTitle("Alert!!!");
        alert_dialog.setMessage("Penality of Rs. 75 will be charged if the query is not attended with in 24 hrs..");
        alert_dialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (networkUtils.checkConnection()){
                    dialog.dismiss();
                    deductAmountResultFromServer(bookingId,
                            statusName, enquiryId, amount);
                }else {
                    Toast.makeText(mcontext, mcontext.getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert_dialog.show();
    }

    private void deductAmountResultFromServer(final String bookingId, final String statusName, final String enquiry_id, final int final_amount) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_BOOKINGS_DEDUCTBALANCE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DeductBalanceResponse", "onResponse: "+response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if(statusCode==200){
                        getAcceptedBookingFromServer(bookingId, statusName, enquiry_id);
                    }else{
                        Toast.makeText(mcontext, statusMessage, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mcontext, error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("User_ID",sharedPref.getStringValue("Vendor_User_id"));
                params.put("user_role",sharedPref.getStringValue("role_id"));
                params.put("amount", String.valueOf(final_amount));
                params.put("status","accept");
                params.put("booking_id", bookingId);
                Log.d("deductparams", "getParams:AnsweredFragment "+params.toString());
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return myLeadsPojoList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_name,textView_city,textView_description, appointmentid_textview;
        ImageButton answered_accept_btn,button_rejected;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.answered_textview_name);
            textView_city  = itemView.findViewById(R.id.answered_textview_city);
            textView_description = itemView.findViewById(R.id.answered_TV_description);
            answered_accept_btn = itemView.findViewById(R.id.answered_accept_btn);
            button_rejected = itemView.findViewById(R.id.answered_reject_btn);
            appointmentid_textview = itemView.findViewById(R.id.appointmentid_textview);
        }
    }

    private void getAcceptedBookingFromServer(final String bookingId, String statusName, final String enquiry_id) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.UPDATE_VENDORBOOKINGS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("UpdateVendorResponse", "onResponse: "+response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if(statusCode == 200){
                        Toast.makeText(mcontext, "Booking Accepted..", Toast.LENGTH_SHORT).show();
                        MyBookingsVendorActivity.viewPager.setCurrentItem(1, true);
                    }else{
//                        Toast.makeText(mcontext, "Booking Not Accepted", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            progressDialog.dismiss();
                Toast.makeText(mcontext, error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("User_ID",sharedPref.getStringValue("Vendor_User_id"));
                params.put("User_Role",sharedPref.getStringValue("role_id"));
                params.put("Booking_ID", bookingId);
                params.put("enquiry_id", enquiry_id);
                params.put("booking_status","accept");
                /*if (sharedPref.getStringValue("role_id").equalsIgnoreCase("5")){
                    params.put("booking_status","complete");
                }else{
                    params.put("booking_status","accept");
                }*/

                Log.d("Bookings_params", "getParams: "+params.toString());

                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

    private void sendCancelledBookingRequestToServer(final String bookingId, String statusName, final String enquiryId, final String cancelledReasonId) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.UPDATE_VENDORBOOKINGS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("UpdateVendorResponse", "onResponse: "+response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if(statusCode == 200){
                        Toast.makeText(mcontext, "Booking Cancelled..", Toast.LENGTH_SHORT).show();
                    }else{
//                        Toast.makeText(mcontext, "Booking Cancelled", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(mcontext, error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("User_ID",sharedPref.getStringValue("Vendor_User_id"));
                params.put("User_Role",sharedPref.getStringValue("role_id"));
                params.put("Booking_ID",bookingId);
                params.put("enquiry_id", enquiryId);
                params.put("Reason_ID", cancelledReasonId);
                params.put("booking_status","cancel");

                Log.d("Bookings_params", "getParams: "+new JSONObject(params).toString());

                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);

    } //end of sendCancelBookingsToServer

    private void reasonForCancellation(final String bookingId, final String statusName, final String enquiryId) {
        final Dialog reasonForCancelDialog = new Dialog(mcontext);
        reasonForCancelDialog.setContentView(R.layout.reasonforcancellayout);
        LinearLayout reason_LL;
        Button reason_submit_btn,reason_cancel_btn;

        reason_LL =reasonForCancelDialog.findViewById(R.id.reasonLL);
        reason_submit_btn = reasonForCancelDialog.findViewById(R.id.reason_submit_btn);
        reason_cancel_btn = reasonForCancelDialog.findViewById(R.id.reason_cancel_btn);

        radioGroup=new RadioGroup(mcontext);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for(int  i=0;i<cancelledReasonPOJOList.size();i++){
            //create Dynamic UI\
            dynamic_radiobutton = new RadioButton(mcontext);
            dynamic_radiobutton.setPadding(1,1,1,1);
            dynamic_radiobutton.setId(i+1);
            dynamic_radiobutton.setTextSize(14);
            dynamic_radiobutton.setText(cancelledReasonPOJOList.get(i).getReason());
            RadioGroup.LayoutParams  rprms= new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            radioGroup.addView(dynamic_radiobutton, rprms);
            dynamic_radiobutton.setLayoutParams(layoutParams);

            if (radioGroup.getParent()!=null){
                ((ViewGroup)radioGroup.getParent()).removeView(radioGroup);
            }
            reason_LL.addView(radioGroup);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
              for (int rg = 0; rg < radioGroup.getChildCount(); rg++) {
                  dynamic_radiobutton = (RadioButton) radioGroup.getChildAt(rg);
                  if (dynamic_radiobutton.getId() == checkedId) {
                      selectedRadioButtonValue = dynamic_radiobutton.getText().toString();
                      selectedRadioButtonId = cancelledReasonPOJOList.get(rg).getReason();
//                      Toast.makeText(mcontext, selectedRadioButtonValue, Toast.LENGTH_SHORT).show();

                  }
              }
          }
      });

        reason_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCancelledBookingRequestToServer(bookingId, statusName, enquiryId, selectedRadioButtonId);
                Toast.makeText(mcontext, "Submitted Reason:"+selectedRadioButtonValue, Toast.LENGTH_SHORT).show();
                reasonForCancelDialog.dismiss();
            }
        });
        reason_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reasonForCancelDialog.dismiss();

            }
        });

        ArrayAdapter<CancelledReasonPOJO> dataAdapter =
                new ArrayAdapter<CancelledReasonPOJO>
                        (mcontext, android.R.layout.simple_spinner_item,cancelledReasonPOJOList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner

        reasonForCancelDialog.show();
    }

    private void getCancelledBookingsReasonList() {
        progressDialog.show();
    StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_CANCELLED_REASONS_URL, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            cancelledReasonPOJOList.clear();
            Log.d("cancelled_Reason_list", "onResponse: "+response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(response);

                cancelled_reson_statusCode = jsonObject.getInt("statusCode");
                cancelled_reason_statusMessage = jsonObject.getString("statusMessage");

                if(cancelled_reson_statusCode ==200){
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for(int i=0;i<jsonArray.length();i++){
                        CancelledReasonPOJO cancelledReasonPOJO = new CancelledReasonPOJO();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        cancelledReasonPOJO.setId(jsonObject1.getString("id"));
                        cancelledReasonPOJO.setReason(jsonObject1.getString("reason"));
                        cancelledReasonPOJOList.add(cancelledReasonPOJO);
                    }
                }
            } catch (JSONException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressDialog.dismiss();
        }
    })
    {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String,String> params = new HashMap<>();
            params.put("User_ID",sharedPref.getStringValue("Vendor_User_id"));
            Log.d("cancelled_params", "getParams: "+params.toString());
            return params;
        }
    };

    VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
    VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);

    }

}
