package appsnova.com.doorstephub.adapters.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import appsnova.com.doorstephub.activities.vendor.VendorMyProfileActivity;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

public class Answer_Recyclerview_Adapter extends RecyclerView.Adapter<Answer_Recyclerview_Adapter.MyViewHolder> {
    Context mcontext;
    List<MyLeadsPojo> myLeadsPojoList;
    SharedPref sharedPref;
    ProgressDialog progressDialog;
    int statusCode;
    String statusMessage;

    public Answer_Recyclerview_Adapter(List<MyLeadsPojo> myLeadsPojoList, Context mcontext) {
        this.myLeadsPojoList = myLeadsPojoList;
        this.mcontext = mcontext;
        progressDialog = UrlUtility.showProgressDialog(mcontext);
        sharedPref = new SharedPref(mcontext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.answered_fragment_list_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int pos) {
        final MyLeadsPojo myLeadsPojo = myLeadsPojoList.get(pos);
        Log.d("Acceptedlistsize", "onBindViewHolder: "+myLeadsPojoList.size());
        Log.d("AcceptedlistName", "onBindViewHolder: "+myLeadsPojo.getName());
        myViewHolder.textView_name.setText("Name:"+myLeadsPojo.getName());
        myViewHolder.textView_city.setText("Service:"+myLeadsPojo.getService());
        myViewHolder.textView_description.setText("Description:"+myLeadsPojo.getDescription());
        myViewHolder.answered_accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext, "Accepeted", Toast.LENGTH_SHORT).show();

                getAcceptedBookingFromServer();

            }
        });
        myViewHolder.button_rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCancelledBookingFromServer();

                Toast.makeText(mcontext, "Rejected", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return myLeadsPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_name,textView_city,textView_description;
        ImageButton answered_accept_btn,button_rejected;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.accepted_textview_name);
            textView_city  = itemView.findViewById(R.id.accepted_textview_city);
            textView_description = itemView.findViewById(R.id.accepted_TV_description);
            answered_accept_btn = itemView.findViewById(R.id.answered_accept_btn);
            button_rejected = itemView.findViewById(R.id.answered_reject_btn);
        }
    }

    private void getAcceptedBookingFromServer() {
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
                params.put("Booking_ID",sharedPref.getStringValue("vendor_booking_id"));
                params.put("booking_status","accept");

                Log.d("Bookings_params", "getParams: "+params.toString());

                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }


    private void getCancelledBookingFromServer() {
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
                params.put("Booking_ID",sharedPref.getStringValue("vendor_booking_id"));
                params.put("booking_status","cancel");

                Log.d("Bookings_params", "getParams: "+params.toString());

                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);

    }
}
