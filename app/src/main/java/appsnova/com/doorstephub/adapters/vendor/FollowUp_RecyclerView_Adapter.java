package appsnova.com.doorstephub.adapters.vendor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.vendor.MyBookingsVendorActivity;
import appsnova.com.doorstephub.models.CancelledReasonPOJO;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

public class FollowUp_RecyclerView_Adapter extends RecyclerView.Adapter<FollowUp_RecyclerView_Adapter.AcceptedViewHolder> {
    Context mcontext;
    List<MyLeadsPojo> myfollow_upLeadsPojoList;

    SharedPref sharedPref;
    Dialog progressDialog;
    NetworkUtils networkUtils;

    int statusCode,deduct_statusCode;
    String statusMessage,deduct_statusMessage;
    double eighteen_percent_deduction,final_amount;
    double standard_amount =75;

    List<CancelledReasonPOJO> cancelledReasonPOJOList;
    LinearLayout.LayoutParams layoutParams;
    RadioGroup radioGroup;
    RadioButton dynamic_radiobutton;
    String selectedRadioButtonValue ="", selectedRadioButtonId="";

    FollowUpInterface followpInterface;

    public FollowUp_RecyclerView_Adapter(Context mcontext, List<MyLeadsPojo> myfollow_upLeadsPojoList, FollowUpInterface accpetButtonClickListener) {
        this.mcontext = mcontext;
        this.myfollow_upLeadsPojoList = myfollow_upLeadsPojoList;
        this.followpInterface = accpetButtonClickListener;

        sharedPref = new SharedPref(mcontext);
        progressDialog = UrlUtility.showCustomDialog(mcontext);
        networkUtils = new NetworkUtils(mcontext);

        cancelledReasonPOJOList = new ArrayList<>();
    }

    @NonNull
    @Override
    public AcceptedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.followup_list_row,viewGroup,false);
        AcceptedViewHolder acceptedViewHolder = new AcceptedViewHolder(view);
        return acceptedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedViewHolder acceptedViewHolder, final int i) {
        final MyLeadsPojo myLeadsPojo = myfollow_upLeadsPojoList.get(i);
        acceptedViewHolder.textView_acceptedname.append(" "+myLeadsPojo.getName());
        acceptedViewHolder.textView_acceptedcity.append(" "+myLeadsPojo.getService());
        acceptedViewHolder.textView_accepteddescription.append(" "+myLeadsPojo.getDescription());
        acceptedViewHolder.followup_textview_address.append(" "+myLeadsPojo.getAddress());

        getCancelledBookingsReasonList();

        acceptedViewHolder.button_accepetedcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + myfollow_upLeadsPojoList.get(i).getPhone_number()));
                mcontext.startActivity(intent);
            }
        });
        acceptedViewHolder.button_accepetedstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkUtils.checkConnection()){
                    followpInterface.onAcceptClick(myfollow_upLeadsPojoList.get(i), i);
                }else {
                    Toast.makeText(mcontext, mcontext.getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }

                //getUpdateBookingLeadsFromServer(myfollow_upLeadsPojoList.get(i).getBooking_id(), myfollow_upLeadsPojoList.get(i).getEnquiry_id());
            }
        });
        acceptedViewHolder.followup_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkUtils.checkConnection()){
                    reasonForCancellation(myfollow_upLeadsPojoList.get(i).getBooking_id(),
                            myfollow_upLeadsPojoList.get(i).getStatus_name(), myfollow_upLeadsPojoList.get(i).getEnquiry_id());
                }else{
                    Toast.makeText(mcontext, mcontext.getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

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
                        selectedRadioButtonId = cancelledReasonPOJOList.get(rg).getId();

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

    @Override
    public int getItemCount() {
        return myfollow_upLeadsPojoList.size();
    }

    public class AcceptedViewHolder extends RecyclerView.ViewHolder {
        TextView textView_acceptedname,textView_acceptedcity,textView_accepteddescription, followup_textview_address;
        ImageButton button_accepetedcall,button_accepetedstart,followup_cancel_btn;
        public AcceptedViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_acceptedname = itemView.findViewById(R.id.followup_textview_name);
            textView_acceptedcity = itemView.findViewById(R.id.followup_textview_city);
            textView_accepteddescription = itemView.findViewById(R.id.followup_textview_description);
            followup_textview_address = itemView.findViewById(R.id.followup_textview_address);
            button_accepetedcall = itemView.findViewById(R.id.followup_call_btn);
            button_accepetedstart = itemView.findViewById(R.id.followup_start_btn);
            followup_cancel_btn = itemView.findViewById(R.id.followup_cancel_btn);

        }
    }

    /*private void getUpdateBookingLeadsFromServer(final String bookingId, final String enquiryId) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.UPDATE_VENDORBOOKINGS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            progressDialog.dismiss();
                Log.d("FollowVendorResponse", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if(statusCode == 200){
                        Toast.makeText(mcontext, "Booking Completed..", Toast.LENGTH_SHORT).show();
                       MyBookingsVendorActivity.viewPager.setCurrentItem(2, true);

                    }else{
//                      //else content
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
                params.put("Booking_ID",bookingId);
                params.put("booking_status","complete");
                params.put("enquiry_id", enquiryId);

                Log.d("FollowBookings_params", "getParams:"+new JSONObject(params).toString());
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }*/

    private void deductAmountResultFromServer() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_BOOKINGS_DEDUCTBALANCE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DeductBalanceResponse", "onResponse:FollowUp "+response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if(statusCode==200){
                        Toast.makeText(mcontext, "Deducted Amount of 75/-", Toast.LENGTH_SHORT).show();
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
                params.put("amount", String.valueOf(standard_amount));
                params.put("status","cancel");
                Log.d("DeductParams ", "getParams: "+params.toString());
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
                        MyBookingsVendorActivity.viewPager.setCurrentItem(3, true);
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

                    int cancelled_reason_statusCode = jsonObject.getInt("statusCode");
                   // String cancelled_reason_statusMessage = jsonObject.getString("statusMessage");

                    if(cancelled_reason_statusCode ==200){
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



