package appsnova.com.doorstephub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.adapters.ServiceSelectionAdapter;
import appsnova.com.doorstephub.models.ServiceSelectionModel;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class ServiceSelectionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ServiceSelectionAdapter serviceSelectionAdapter;
    List<ServiceSelectionModel> serviceSelectionModelList;
    List<ServiceSelectionModel> selecteditemlist;
    ActionMode mActionMode;
    boolean isMultiSelect = false;
    int statusCode;
    String statusMessage;
    Bundle bundle;
    String service_Id="",service_name="";
    NetworkUtils networkUtils;
    SharedPref sharedPref;
    ProgressDialog progressDialog;
    int selectedItemsCount;
    LinearLayout services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Transition enter_transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(enter_transition);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selection);
        services=findViewById(R.id.services);

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
        if (service_name.contains("Computer Service and Repairs")){
            services.setVisibility(View.VISIBLE);
        }else {
            services.setVisibility(View.GONE);
        }

        setTitle("ServiveSelectionActivity");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.serviceselection_list);
        serviceSelectionModelList = new ArrayList<>();
        selecteditemlist = new ArrayList<>();
        serviceSelectionAdapter = new ServiceSelectionAdapter(ServiceSelectionActivity.this, serviceSelectionModelList, selecteditemlist);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(ServiceSelectionActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(serviceSelectionAdapter);
       // serviceSelectionAdapter.notifyDataSetChanged();

        if (networkUtils.checkConnection()){
            getSubServicesListFromServer();
        }


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (!isMultiSelect) {
                    isMultiSelect = true;
                }


                multi_select(position);

            }

            @Override
            public void onItemLongClick(View view, int position) {
            }

        }));
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
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            ServiceSelectionModel serviceSelectionModel=new ServiceSelectionModel();
                            serviceSelectionModel.setId(jsonObject1.getString("id"));
                            serviceSelectionModel.setName(jsonObject1.getString("name"));
                            serviceSelectionModel.setTitle(jsonObject1.getString("title"));
                            serviceSelectionModel.setDescription(jsonObject1.getString("description"));
                            serviceSelectionModelList.add(serviceSelectionModel);
                        }
                        Log.d("size", "onResponse: "+serviceSelectionModelList.size());
                        serviceSelectionAdapter.notifyDataSetChanged();

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
                params.put("User_ID","65");
                params.put("service_subcat_id",service_Id);

                JSONObject jsonObject=new JSONObject(params);
                Log.d("subservicesparams", "getParams: "+jsonObject.toString());

                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);

    }//end of getSubServicesListFromServer


    public void serviceselection(View view) {

        selectedItemsList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        selecteditemlist.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        selecteditemlist.clear();
    }


    @Override
    protected void onStop() {
        super.onStop();
        selecteditemlist.clear();

    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return true;
    }

    public void multi_select(int position) {
            if (selecteditemlist.contains(serviceSelectionModelList.get(position))){
                selecteditemlist.remove(serviceSelectionModelList.get(position));
                Log.d("selecteditemlist", "multi_select: "+selecteditemlist);
            }else{
                selecteditemlist.add(serviceSelectionModelList.get(position));
            }
            refreshAdapter();
    }

    public void refreshAdapter() {
        serviceSelectionAdapter.selectedItemsList=selecteditemlist;
        serviceSelectionAdapter.serviceSelectionModelList = serviceSelectionModelList;
        serviceSelectionAdapter.notifyDataSetChanged();
    }
    private void selectedItemsList(){
        List<ServiceSelectionModel> list = null;
        selectedItemsCount=serviceSelectionAdapter.getSelectedItemCount();
        Log.d("selectedItemsCount", "selectedItemsList: "+selectedItemsCount);

        if (selectedItemsCount == serviceSelectionModelList.size()){
            list = serviceSelectionAdapter.getSelectedItem();
            Log.d("list", "assignUsers: "+list);

        }else{
            list = serviceSelectionAdapter.getSelectedItem();
            Log.d("selectedLeadsCount", "assignUsers: "+list);

        }

        if (list.size() > 0){
            StringBuilder sb = new StringBuilder();
            for (int index = 0; index < list.size(); index++){
                ServiceSelectionModel model = list.get(index);
                sb.append(model.getId()+",");
            }
            String output = sb.deleteCharAt(sb.lastIndexOf(",")).toString();
            Log.d("LeadList", "assignUsers: "+output);

            Intent intent = new Intent(ServiceSelectionActivity.this,ServiceScheduleActivity.class);
            intent.putExtra("Service_Id",service_Id);
            intent.putExtra("IntentFrom","serviceselection");
            intent.putExtra("serviceSelectionId",output);
            Log.d("intentvalues", "selectedItemsList: "+service_Id+","+output);
            startActivity(intent);

        }else{
            Toast.makeText(ServiceSelectionActivity.this, "No List", Toast.LENGTH_SHORT).show();
        }



    }

}
