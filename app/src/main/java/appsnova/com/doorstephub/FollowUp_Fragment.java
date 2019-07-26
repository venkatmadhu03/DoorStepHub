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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appsnova.com.doorstephub.adapters.vendor.FollowUp_RecyclerView_Adapter;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

public class FollowUp_Fragment extends Fragment {
List<MyLeadsPojo> myfollowup_pojolist = new ArrayList<>();
FollowUp_RecyclerView_Adapter followUp_recyclerView_adapter;
int statusCode;
String statusMessage;
RecyclerView accepeted_recyclerview;
Dialog progressDialog;
SharedPref sharedPref;
SwipeRefreshLayout follow_up_swipeRL;
TextView noFollowupsTv;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent = getActivity().getIntent();
//        intent.getBundleExtra("bundle");
//        name =getArguments().getString("answered_name");
//        city = getArguments().getString("answered_city");
//        description =getArguments().getString("answered_description");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = UrlUtility.showCustomDialog(getActivity());
        sharedPref = new SharedPref(getActivity());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        accepeted_recyclerview= view.findViewById(R.id.accepeted_recycler_view);
        follow_up_swipeRL = view.findViewById(R.id.follow_up_swipeRL);
        noFollowupsTv=view.findViewById(R.id.noFollowupsTv);
        follow_up_swipeRL.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        follow_up_swipeRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFollowUp_DetailsFromServer();
            }
        });
       /* accepted_leadpojo = new MyLeadsPojo("sai","hyderabad","Desciption about sai");
        myaccepeted_pojolist.add(accepted_leadpojo);
        accepted_leadpojo = new MyLeadsPojo("sree","bangalore","Description about sree");
        myaccepeted_pojolist.add(accepted_leadpojo);
        accepted_leadpojo = new MyLeadsPojo("raj","chennai","Description about raj");
        myaccepeted_pojolist.add(accepted_leadpojo);
        accepted_leadpojo = new MyLeadsPojo("rao","mumbai","Description about rao");
        myaccepeted_pojolist.add(accepted_leadpojo);
        accepted_leadpojo = new MyLeadsPojo("ram","vizag","Description about ram");
        myaccepeted_pojolist.add(accepted_leadpojo);*/
        getFollowUp_DetailsFromServer();
    }

    private void getFollowUp_DetailsFromServer() {
        progressDialog.show();
//        Toast.makeText(getActivity(), "InsideMethod", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_GETBOOKINGS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                myfollowup_pojolist.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                },3000);
                follow_up_swipeRL.setRefreshing(false);
                Log.d("VendorBookingsResponse", "onResponse Followup: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if(statusCode==200){
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        if (accepeted_recyclerview.getVisibility()==View.GONE){
                            accepeted_recyclerview.setVisibility(View.VISIBLE);
                            noFollowupsTv.setVisibility(View.GONE);
                        }
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            MyLeadsPojo myLeadsPojo = new MyLeadsPojo();
                            myLeadsPojo.setName(jsonObject1.getString("user_name"));
                            myLeadsPojo.setService(jsonObject1.getString("name"));
                            myLeadsPojo.setDescription(jsonObject1.getString("requirement"));
                            myLeadsPojo.setPhone_number(jsonObject1.getString("user_mobile"));
                            myfollowup_pojolist.add(myLeadsPojo);
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        accepeted_recyclerview.setLayoutManager(linearLayoutManager);
                        followUp_recyclerView_adapter =new FollowUp_RecyclerView_Adapter(getContext(),myfollowup_pojolist);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        accepeted_recyclerview.setAdapter(followUp_recyclerView_adapter);
                        followUp_recyclerView_adapter.notifyDataSetChanged();
                    }else {
                        noFollowupsTv.setVisibility(View.VISIBLE);
                        accepeted_recyclerview.setVisibility(View.GONE);
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
                params.put("status_name","Follow Up");
                Log.d("MainParams", "getParams:FollowUp"+params.toString());
                return params;

            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }
}
