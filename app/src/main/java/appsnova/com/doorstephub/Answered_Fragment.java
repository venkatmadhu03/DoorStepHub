package appsnova.com.doorstephub;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;;
import android.os.Handler;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appsnova.com.doorstephub.adapters.vendor.Answer_Recyclerview_Adapter;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;


public class Answered_Fragment extends Fragment {
    List<MyLeadsPojo> myLeadsPojoList  =new ArrayList<>();
    Dialog progressDialog;
    SharedPref sharedPref;
    int statusCode;
    String statusMessage;
    RecyclerView answered_Leads_RV;
    Answer_Recyclerview_Adapter answer_recyclerview_adapter;
    SwipeRefreshLayout answered_swipeRL;
    TextView noLeadsTv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = UrlUtility.showCustomDialog(getActivity());
        sharedPref = new SharedPref(getActivity());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_answer, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        answered_Leads_RV= view.findViewById(R.id.answered_recycler_view);
        answered_swipeRL = view.findViewById(R.id.answered_swipeRL);
        noLeadsTv=view.findViewById(R.id.noLeadsTv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        answered_Leads_RV.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        getBooking_DetailsFromServer();
        answered_swipeRL.setColorSchemeResources(R.color.colorAccent);
        answered_swipeRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBooking_DetailsFromServer();
//                Toast.makeText(getActivity(), "AfterRefreshDataUpdated", Toast.LENGTH_SHORT).show();

            }
        });
    }



    private void getBooking_DetailsFromServer() {
        progressDialog.show();
//        Toast.makeText(getActivity(), "InsideMethod", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_GETBOOKINGS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                myLeadsPojoList.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                },3000);
                answered_swipeRL.setRefreshing(false);
                Log.d("VendorBookingsResponse", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if(statusCode==200){
                        if (answered_Leads_RV.getVisibility()==View.GONE){
                            noLeadsTv.setVisibility(View.GONE);
                            answered_Leads_RV.setVisibility(View.VISIBLE);
                        }
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            MyLeadsPojo myLeadsPojo = new MyLeadsPojo();
                            myLeadsPojo.setName(jsonObject1.getString("user_name"));
                            myLeadsPojo.setService(jsonObject1.getString("name"));
                            myLeadsPojo.setDescription(jsonObject1.getString("requirement"));
                            myLeadsPojo.setStatus_name(jsonObject1.getString("status_name"));
                            myLeadsPojo.setAppointment_id(jsonObject1.getString("appoitmentid"));
                            myLeadsPojo.setBooking_id(jsonObject1.getString("booking_id"));
                            myLeadsPojo.setTransaction_id(jsonObject1.getString("transaction_id"));
                            myLeadsPojo.setEnquiry_id(jsonObject1.getString("enquiry_id"));

                            myLeadsPojoList.add(myLeadsPojo);

                        }
                        answer_recyclerview_adapter= new Answer_Recyclerview_Adapter(myLeadsPojoList,getContext());
                        answered_Leads_RV.setAdapter(answer_recyclerview_adapter);
                        answer_recyclerview_adapter.notifyDataSetChanged();
                    }else {
                        noLeadsTv.setVisibility(View.VISIBLE);
                        answered_Leads_RV.setVisibility(View.GONE);
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
                params.put("status_name","Answered");
                Log.d("MainParams", "getParams:Answered"+params.toString());
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }
}
