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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.vendor.MyBookingsVendorActivity;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

public class FollowUp_RecyclerView_Adapter extends RecyclerView.Adapter<FollowUp_RecyclerView_Adapter.AcceptedViewHolder> {
    Context mcontext;
    List<MyLeadsPojo> myfollow_upLeadsPojoList;
    SharedPref sharedPref;
    Dialog progressDialog;
    int statusCode,deduct_statusCode;
    String statusMessage,deduct_statusMessage;
    double eighteen_percent_deduction,final_amount;
    double standard_amount =75;

    public FollowUp_RecyclerView_Adapter(Context mcontext, List<MyLeadsPojo> myfollow_upLeadsPojoList) {
        this.mcontext = mcontext;
        this.myfollow_upLeadsPojoList = myfollow_upLeadsPojoList;
        sharedPref = new SharedPref(mcontext);
        progressDialog = UrlUtility.showCustomDialog(mcontext);

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
        acceptedViewHolder.textView_acceptedname.setText("Name:"+myLeadsPojo.getName());
        acceptedViewHolder.textView_acceptedcity.setText("Service:"+myLeadsPojo.getService());
        acceptedViewHolder.textView_accepteddescription.setText("Description:"+myLeadsPojo.getDescription());
        acceptedViewHolder.button_accepetedcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + myLeadsPojo.getPhone_number()));
                mcontext.startActivity(intent);
            }
        });
        acceptedViewHolder.button_accepetedstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUpdateBookingLeadsFromServer(myfollow_upLeadsPojoList.get(i).getBooking_id(), myfollow_upLeadsPojoList.get(i).getEnquiry_id());
            }
        });
        acceptedViewHolder.followup_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mcontext, "Deduct 75/- as penality", Toast.LENGTH_SHORT).show();
                eighteen_percent_deduction = (18.0f/100.0f) * (standard_amount);
                final_amount = standard_amount+eighteen_percent_deduction;
//                Toast.makeText(mcontext, String.format("%.2f",final_amount), Toast.LENGTH_SHORT).show();
                deductAmountResultFromServer();
                Toast.makeText(mcontext, statusMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myfollow_upLeadsPojoList.size();
    }

    public class AcceptedViewHolder extends RecyclerView.ViewHolder {
        TextView textView_acceptedname,textView_acceptedcity,textView_accepteddescription;
        ImageButton button_accepetedcall,button_accepetedstart,followup_cancel_btn;
        public AcceptedViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_acceptedname = itemView.findViewById(R.id.followup_textview_name);
            textView_acceptedcity = itemView.findViewById(R.id.followup_textview_city);
            textView_accepteddescription = itemView.findViewById(R.id.followup_textview_description);
            button_accepetedcall = itemView.findViewById(R.id.followup_call_btn);
            button_accepetedstart = itemView.findViewById(R.id.followup_start_btn);
            followup_cancel_btn = itemView.findViewById(R.id.followup_cancel_btn);

        }
    }
    private void getUpdateBookingLeadsFromServer(final String bookingId, final String enquiryId) {
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

                Log.d("FollowBookings_params", "getParams:"+new JSONObject(params).toString());
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }
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
                params.put("amount", String.format("%.2f",final_amount));
                params.put("status","accept");
                Log.d("DeductParams ", "getParams: "+params.toString());
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }
}
