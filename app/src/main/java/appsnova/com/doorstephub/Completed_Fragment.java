package appsnova.com.doorstephub;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.instamojo.android.Instamojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appsnova.com.doorstephub.adapters.vendor.Completed_RecyclerView_Adapter;
import appsnova.com.doorstephub.adapters.vendor.FollowUp_RecyclerView_Adapter;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;


public class Completed_Fragment extends Fragment {
    List<MyLeadsPojo> mycompleted_pojolist = new ArrayList<>();
    Completed_RecyclerView_Adapter completed_recyclerView_adapter;
    int statusCode;
    String statusMessage;
    Dialog progressDialog;
    SharedPref sharedPref;

    TextView noCompletedLeadsTv;
    SwipeRefreshLayout completed_swipeRL;
    RecyclerView completed_recyclerview;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        progressDialog = UrlUtility.showCustomDialog(getActivity());
        sharedPref = new SharedPref(getActivity());
        return inflater.inflate(R.layout.fragment_completed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        completed_recyclerview= view.findViewById(R.id.completed_recyclerview);
        completed_swipeRL = view.findViewById(R.id.completed_swipeRL);
        noCompletedLeadsTv = view.findViewById(R.id.noCompletedLeadsTv);

        getCompleted_DetailsFromServer();

        completed_swipeRL.setColorSchemeResources(R.color.colorAccent);
        completed_swipeRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCompleted_DetailsFromServer();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        completed_recyclerview.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
    }

    private void getCompleted_DetailsFromServer() {
        progressDialog.show();
//        Toast.makeText(getActivity(), "InsideMethod", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_GETBOOKINGS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mycompleted_pojolist.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                },3000);
                completed_swipeRL.setRefreshing(false);
                Log.d("VendorBookingsResponse", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");

                    if(statusCode==200){
                        completed_recyclerview.setVisibility(View.VISIBLE);
                        noCompletedLeadsTv.setVisibility(View.GONE);

                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            MyLeadsPojo myLeadsPojo = new MyLeadsPojo();
                            myLeadsPojo.setName(jsonObject1.getString("user_name"));
                            myLeadsPojo.setService(jsonObject1.getString("name"));
                            myLeadsPojo.setDescription(jsonObject1.getString("requirement"));
                            myLeadsPojo.setAppointment_id(jsonObject1.getString("appoitmentid"));
                            myLeadsPojo.setBooking_id(jsonObject1.getString("booking_id"));
                            myLeadsPojo.setTransaction_id(jsonObject1.getString("transaction_id"));
                            myLeadsPojo.setEnquiry_id(jsonObject1.getString("enquiry_id"));
                            myLeadsPojo.setPhone_number(jsonObject1.getString("user_mobile"));

                            mycompleted_pojolist.add(myLeadsPojo);
                        }
                        completed_recyclerView_adapter =new Completed_RecyclerView_Adapter(getContext(),mycompleted_pojolist);
                        completed_recyclerview.setAdapter(completed_recyclerView_adapter);
                        completed_recyclerView_adapter.notifyDataSetChanged();
                    }else{
                        completed_recyclerview.setVisibility(View.GONE);
                        noCompletedLeadsTv.setVisibility(View.VISIBLE);
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
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("User_ID",sharedPref.getStringValue("Vendor_User_id"));
                params.put("status_name","Closed");
                Log.d("MainParams", "getParams:Completed"+params.toString());
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }
}
