package appsnova.com.doorstephub.activities;

import androidx.annotation.NonNull;
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

public class ServiceSelectionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RelativeLayout servicetype_container;
    TextView noSubserviceTv;
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
    CardView services;
    RadioButton rb_desktop,rb_laptop;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Transition service_selection_transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(service_selection_transition);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selection);
        services=findViewById(R.id.services);
        radioGroup = findViewById(R.id.services_radiogroup);
        rb_desktop = findViewById(R.id.rb_desktop);
        rb_laptop = findViewById(R.id.rb_laptop);


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
        if (service_name.contains("Computer")){
            services.setVisibility(View.VISIBLE);
        }else {
            services.setVisibility(View.GONE);
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.serviceselection_list);
        servicetype_container =findViewById(R.id.servicetype_container);
        noSubserviceTv =findViewById(R.id.noSubserviceTv);

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
        if (service_name.contains("Computer")){
            services.setVisibility(View.VISIBLE);

            if(rb_desktop.isChecked()||rb_laptop.isChecked()){
                selectedItemsList();
            }
            else{
                Toast.makeText(this, "Please Select Type Of Device", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            services.setVisibility(View.GONE);
            selectedItemsList();
        }
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
    protected void onResume() {
        super.onResume();
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

            Intent intent = new Intent(ServiceSelectionActivity.this, ServiceScheduleActivity.class);
            intent.putExtra("Service_Id",service_Id);
            intent.putExtra("IntentFrom","serviceselection");
            intent.putExtra("serviceSelectionId",output);
           // startActivity(intent);
            Log.d("intentvalues", "selectedItemsList: "+service_Id+","+output);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions activityOptions = (ActivityOptions) ActivityOptions.makeSceneTransitionAnimation(ServiceSelectionActivity.this);
                startActivity(intent,activityOptions.toBundle());
            }

        }else{
            Toast.makeText(ServiceSelectionActivity.this, "Please Select Any Service..", Toast.LENGTH_SHORT).show();
        }



    }

}
