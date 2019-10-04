package appsnova.com.doorstephub.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.adapters.ProblemsAdapter;
import appsnova.com.doorstephub.adapters.ServiceRequiredForAdapter;
import appsnova.com.doorstephub.models.ProblemsModel;
import appsnova.com.doorstephub.models.ServiceRequiredForModel;
import appsnova.com.doorstephub.models.ServiceSelectionModel;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;
import retrofit2.http.Url;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SubServiceSelectionActivity extends AppCompatActivity {

    private static final String TAG = SubServiceSelectionActivity.class.getSimpleName();

    //Create Objects for Utils
    SharedPref sharedPref;
    NetworkUtils networkUtils;
    ProgressDialog progressDialog;

    CardView services;
    RadioButton rb_service_type_1,rb_service_type_2;
    RadioGroup radioGroup;
    Button button_continue;
    RecyclerView problemsRecyclerView, serviceRequiredForRecyclerView;
    TextView noIssuesTV;

    int selectedItemsCount, statusCode;
    String statusMessage="", subServiceId="", service_id="", service_name="", subServiceName="";
    boolean isMultiSelect = false;

    List<ServiceRequiredForModel> serviceRequiredForModelList;
    List<ProblemsModel> problemsModelList;
    ProblemsAdapter problemsAdapter;
    ServiceRequiredForAdapter serviceRequiredForAdapter;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize utils
        sharedPref = new SharedPref(this);
        networkUtils = new NetworkUtils(this);
        problemsModelList = new ArrayList<>();
        serviceRequiredForModelList = new ArrayList<>();
        progressDialog = UrlUtility.showProgressDialog(this);

        bundle = getIntent().getExtras();
        if (bundle !=null){
            subServiceId = bundle.getString("sub_service_id");
            service_id = bundle.getString("Service_Id");
            service_name = bundle.getString("Service_Name");
            subServiceName = bundle.getString("sub_service_name");
        }

        setContentView(R.layout.activity_sub_service_selection);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        new ActionBar.LayoutParams(250,90);

        //initilze views
        services=findViewById(R.id.problemsCardView);
        button_continue = findViewById(R.id.button_continue);
        problemsRecyclerView = findViewById(R.id.problemsRecyclerView);
        noIssuesTV = findViewById(R.id.noIssuesTV);
        serviceRequiredForRecyclerView = findViewById(R.id.serviceRequiredForRecyclerView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        problemsRecyclerView.setLayoutManager(linearLayoutManager);
        problemsRecyclerView.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        serviceRequiredForRecyclerView.setLayoutManager(linearLayoutManager1);
        serviceRequiredForRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (networkUtils.checkConnection()){
            getServiceRequiredTypeFromServer();
        }else{
            UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
        }
//        problemsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, problemsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                if (!isMultiSelect) {
//                    isMultiSelect = true;
//                }
//                multi_select(position);
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//            }
//
//        }));

    } //end of onCreate

    private void getServiceRequiredTypeFromServer(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.GET_SERVICE_REQUIRED_TYPE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse:ServiceRequired "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    if (statusCode == 200){
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            ServiceRequiredForModel serviceRequiredForModel = new ServiceRequiredForModel();
                            serviceRequiredForModel.setId(jsonObject1.getString("id"));
                            serviceRequiredForModel.setName(jsonObject1.getString("name"));
                            serviceRequiredForModel.setService_required_for_id(jsonObject1.getString("sub_services_services_id"));

                            serviceRequiredForModelList.add(serviceRequiredForModel);
                        }

                    }else {
                        services.setVisibility(View.GONE);
                    }
                    serviceRequiredForAdapter = new ServiceRequiredForAdapter(serviceRequiredForModelList, SubServiceSelectionActivity.this, new ServiceRequiredForAdapter.ItemClickListener() {
                        @Override
                        public void onClickItem(ServiceRequiredForModel serviceRequiredForModel) {
                            getProblemsListFromServer(serviceRequiredForModel.getId());
                        }
                    });
                    serviceRequiredForRecyclerView.setAdapter(serviceRequiredForAdapter);
                    serviceRequiredForAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("User_ID", sharedPref.getStringValue("User_Id"));
                params.put("sub_service_id", subServiceId);
                Log.d(TAG, "getParams: "+new JSONObject(params));
                return params;
            }
        };

        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    } // end of getServiceRequiredTypeFromServer

    private void getProblemsListFromServer(final String id){
        problemsModelList.clear();
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.GET_PROBLEMS_FROM_SERVER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse:Problems "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    if (statusCode == 200){
                        noIssuesTV.setVisibility(View.GONE);
                        problemsRecyclerView.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            ProblemsModel problemsModel = new ProblemsModel();
                            problemsModel.setId(jsonObject1.getString("id"));
                            problemsModel.setName(jsonObject1.getString("name"));
                            problemsModel.setSub_service_id(jsonObject1.getString("landpages_id"));

                            problemsModelList.add(problemsModel);
                        }
                    }else {
                        noIssuesTV.setVisibility(View.VISIBLE);
                        problemsRecyclerView.setVisibility(View.GONE);
                    }

                    problemsAdapter = new ProblemsAdapter(problemsModelList, SubServiceSelectionActivity.this, service_id, service_name, subServiceId, subServiceName);
                    problemsRecyclerView.setAdapter(problemsAdapter);
                    problemsAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d(TAG, "onErrorResponse: "+error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("User_ID", sharedPref.getStringValue("User_Id"));
                params.put("service_required_for_id", id);
                Log.d(TAG, "getParams: "+new JSONObject(params));
                return params;
             }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    } //end of getProblemsListFromServer


    @Override
    protected void onPause() {
        super.onPause();
        //selecteditemlist.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //selecteditemlist.clear();
    }


    @Override
    protected void onStop() {
        super.onStop();
       // selecteditemlist.clear();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //selecteditemlist.clear();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return true;
    }

//    public void multi_select(int position) {
//        if (selecteditemlist.contains(serviceSelectionModelList.get(position))){
//            selecteditemlist.remove(serviceSelectionModelList.get(position));
//            Log.d("selecteditemlist", "multi_select: "+selecteditemlist);
//        }else{
//            selecteditemlist.add(serviceSelectionModelList.get(position));
//        }
//        refreshAdapter();
//    }
//
//    public void refreshAdapter() {
//        serviceSelectionAdapter.selectedItemsList=selecteditemlist;
//        serviceSelectionAdapter.serviceSelectionModelList = serviceSelectionModelList;
//        serviceSelectionAdapter.notifyDataSetChanged();
//    }
//    private void selectedItemsList(){
//        List<ServiceSelectionModel> list = null;
//        selectedItemsCount=serviceSelectionAdapter.getSelectedItemCount();
//        Log.d("selectedItemsCount", "selectedItemsList: "+selectedItemsCount);
//
//        if (selectedItemsCount == serviceSelectionModelList.size()){
//            list = serviceSelectionAdapter.getSelectedItem();
//            Log.d("list", "assignUsers: "+list);
//        }else{
//            list = serviceSelectionAdapter.getSelectedItem();
//            Log.d("selectedLeadsCount", "assignUsers: "+list);
//
//        }
//
//        if (list.size() > 0){
//            StringBuilder sb = new StringBuilder();
//            StringBuilder sbIds = new StringBuilder();
//            for (int index = 0; index < list.size(); index++){
//                ServiceSelectionModel model = list.get(index);
//                sb.append(model.getName()).append(",");
//                sbIds.append(model.getId()).append(",");
//            }
//            //String output = sb.deleteCharAt(sb.lastIndexOf(",")).toString();
//            Log.d("LeadList", "assignUsers: "+sb.toString());
//
//            Intent intent = new Intent(ServiceSelectionActivity.this, ServiceScheduleActivity.class);
//            intent.putExtra("Service_Id",service_Id);
//            intent.putExtra("Service_Name",service_name);
//            intent.putExtra("IntentFrom","serviceselection");
//            intent.putExtra("serviceSelectionId",sbIds.toString());
//            intent.putExtra("serviceSelections", sb.toString());
//            // startActivity(intent);
//            Log.d("intentvalues", "selectedItemsList: "+service_name+","+sb.toString());
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                ActivityOptions activityOptions = (ActivityOptions) ActivityOptions.makeSceneTransitionAnimation(ServiceSelectionActivity.this);
//                startActivity(intent,activityOptions.toBundle());
//            }
//
//        }else{
//            Toast.makeText(ServiceSelectionActivity.this, "Please Select Any Service..", Toast.LENGTH_SHORT).show();
//        }
//
//
//
//    }
}
