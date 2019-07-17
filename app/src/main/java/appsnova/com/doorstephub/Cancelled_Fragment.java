package appsnova.com.doorstephub;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import appsnova.com.doorstephub.adapters.vendor.Cancelled_Recyclerview_Adapter;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;


public class Cancelled_Fragment extends Fragment {
    List<MyLeadsPojo> myLeadsPojoList  =new ArrayList<>();
    Dialog progressDialog;
    SharedPref sharedPref;
    int statusCode;
    String statusMessage;
    RecyclerView cancelled_Leads_RV;
   Cancelled_Recyclerview_Adapter cancelled_recyclerview_adapter;
   SwipeRefreshLayout cancelled_swipeRL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = UrlUtility.showCustomDialog(getActivity());
        sharedPref = new SharedPref(getActivity());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cancelled, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancelled_Leads_RV= view.findViewById(R.id.cancelled_recycler_view);
        cancelled_swipeRL = view.findViewById(R.id.cancelled_swipeRL);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        cancelled_Leads_RV.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);


        getCancelled_DetailsFromServer();
        cancelled_swipeRL.setColorSchemeResources(R.color.colorAccent);
        cancelled_swipeRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCancelled_DetailsFromServer();
            }
        });
    }

    private void getCancelled_DetailsFromServer() {
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
                cancelled_swipeRL.setRefreshing(false);
                Log.d("VendorBookingsResponse", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    if(statusCode==200){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            MyLeadsPojo myLeadsPojo = new MyLeadsPojo();
                            myLeadsPojo.setName(jsonObject1.getString("user_name"));
                            myLeadsPojo.setService(jsonObject1.getString("name"));
                            myLeadsPojo.setDescription(jsonObject1.getString("requirement"));
                            if(jsonObject1.has("cancellation_reason")){
                                myLeadsPojo.setCancelled_reason(jsonObject1.getString("cancellation_reason"));
                            }
                            sharedPref.setStringValue("vendor_booking_status",jsonObject1.getString("status_name"));
                            sharedPref.setStringValue("vendor_booking_id",jsonObject1.getString("booking_id"));
                            myLeadsPojoList.add(myLeadsPojo);

                        }

                        cancelled_recyclerview_adapter= new Cancelled_Recyclerview_Adapter(myLeadsPojoList,getContext());
                        cancelled_Leads_RV.setAdapter(cancelled_recyclerview_adapter);
                        cancelled_recyclerview_adapter.notifyDataSetChanged();
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
                params.put("status_name","Cancelled");
                Log.d("MainParams", "getParams:Cancelled"+params.toString());
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

}
