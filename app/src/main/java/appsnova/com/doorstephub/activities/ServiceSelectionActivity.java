package appsnova.com.doorstephub.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.RecyclerItemClickListener;
import appsnova.com.doorstephub.activities.ServiceScheduleActivity;
import appsnova.com.doorstephub.adapters.ServiceSelectionAdapter;
import appsnova.com.doorstephub.models.ServiceSelectionModel;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class
ServiceSelectionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RelativeLayout servicetype_container;
    TextView noSubserviceTv;


    ServiceSelectionAdapter serviceSelectionAdapter;
    List<ServiceSelectionModel> serviceSelectionModelList;

    ActionMode mActionMode;
    boolean isMultiSelect = false;
    int statusCode;
    String statusMessage;
    Bundle bundle;
    String service_Id="",service_name="";

    NetworkUtils networkUtils;
    SharedPref sharedPref;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Transition service_selection_transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(service_selection_transition);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selection);


        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        new ActionBar.LayoutParams(250,90);
        //android.app.ActionBar.LayoutParams layoutParams = new android.app.ActionBar.LayoutParams(250,90);
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));


        //intialie Utils Objects
        networkUtils = new NetworkUtils(this);
        sharedPref = new SharedPref(this);
        progressDialog = UrlUtility.showProgressDialog(this);
        bundle=getIntent().getExtras();
        if (bundle!=null){
            service_Id=bundle.getString("service_id");
            service_name=bundle.getString("service_name");
            Log.d("service_Id", "onCreate: "+service_Id+","+service_name);
        }

        recyclerView = findViewById(R.id.serviceselection_list);
        servicetype_container =findViewById(R.id.servicetype_container);
        noSubserviceTv =findViewById(R.id.noSubserviceTv);

        serviceSelectionModelList = new ArrayList<>();
        serviceSelectionAdapter = new ServiceSelectionAdapter(ServiceSelectionActivity.this, serviceSelectionModelList
                                    , service_Id, service_name);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(ServiceSelectionActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(serviceSelectionAdapter);
       // serviceSelectionAdapter.notifyDataSetChanged();

        if (networkUtils.checkConnection()){
            getSubServicesListFromServer();
        }
    }

    // get SubServices
    public void getSubServicesListFromServer() {
        progressDialog.show();
        serviceSelectionModelList.clear();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, UrlUtility.SUB_SERVICES_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SubServicesResponse", "onResponse: "+response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    statusCode=jsonObject.getInt("statusCode");
                    statusMessage=jsonObject.getString("statusMessage");
                    JSONArray jsonArray=jsonObject.getJSONArray("response");
                    if (statusCode==200){
                        noSubserviceTv.setVisibility(View.GONE);
                        servicetype_container.setVisibility(View.VISIBLE);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            ServiceSelectionModel serviceSelectionModel=new ServiceSelectionModel();
                            serviceSelectionModel.setId(jsonObject1.getString("id"));
                            serviceSelectionModel.setName(jsonObject1.getString("name"));
                            serviceSelectionModel.setTitle(jsonObject1.getString("title"));
                            serviceSelectionModel.setDescription(jsonObject1.getString("description"));
                            serviceSelectionModel.setService_selection_image(jsonObject1.getString("sub_service_image"));
                            serviceSelectionModelList.add(serviceSelectionModel);
                        }
                        Log.d("size", "onResponse: "+serviceSelectionModelList.size());
                        serviceSelectionAdapter.notifyDataSetChanged();
                    }
                    else{
                        noSubserviceTv.setVisibility(View.VISIBLE);
                        servicetype_container.setVisibility(View.GONE);
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
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("User_ID",sharedPref.getStringValue("User_Id"));
                params.put("service_subcat_id",service_Id);

                JSONObject jsonObject=new JSONObject(params);
                Log.d("subservicesparams", "getParams: "+jsonObject.toString());

                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);

    }//end of getSubServicesListFromServer

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return true;
    }
}
