package appsnova.com.doorstephub;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import appsnova.com.doorstephub.activities.HomeActivity;
import appsnova.com.doorstephub.activities.vendor.MyBookingsVendorActivity;
import appsnova.com.doorstephub.adapters.vendor.FollowUpInterface;
import appsnova.com.doorstephub.adapters.vendor.FollowUp_RecyclerView_Adapter;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

import static android.app.Activity.RESULT_OK;

public class FollowUp_Fragment extends Fragment implements FollowUpInterface {
List<MyLeadsPojo> myfollowup_pojolist = new ArrayList<>();
FollowUp_RecyclerView_Adapter followUp_recyclerView_adapter;
int statusCode;
String statusMessage;
RecyclerView accepeted_recyclerview;
SwipeRefreshLayout follow_up_swipeRL;
TextView noFollowupsTv;

Dialog progressDialog;
SharedPref sharedPref;
NetworkUtils networkUtils;


    //Dialog Views
    Dialog dialog;
    private TextView customer_name, mobile_number;
    private ImageView invoice_image;
    private Button submit_button, cancel_button;
    final static int PERMISSION_REQUEST = 1;
    int CAMERA_ACTION_INTENT = 2;
    String base64_file = "", file_name="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //dialog = new Dialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = UrlUtility.showCustomDialog(getActivity());
        sharedPref = new SharedPref(getActivity());
        networkUtils = new NetworkUtils(getActivity());
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        accepeted_recyclerview.setLayoutManager(linearLayoutManager);
        followUp_recyclerView_adapter =new FollowUp_RecyclerView_Adapter(getContext(),myfollowup_pojolist, this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        accepeted_recyclerview.setAdapter(followUp_recyclerView_adapter);

        follow_up_swipeRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFollowUp_DetailsFromServer();
            }
        });
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
                            myLeadsPojo.setAppointment_id(jsonObject1.getString("appoitmentid"));
                            myLeadsPojo.setBooking_id(jsonObject1.getString("booking_id"));
                            myLeadsPojo.setTransaction_id(jsonObject1.getString("transaction_id"));
                            myLeadsPojo.setEnquiry_id(jsonObject1.getString("enquiry_id"));
                            myLeadsPojo.setAddress(jsonObject1.getString("address"));
                            myfollowup_pojolist.add(myLeadsPojo);
                        }

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
                follow_up_swipeRL.setRefreshing(false);
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

    @Override
    public void onAcceptClick(MyLeadsPojo myLeadsPojo, int position) {
        buildUploadDialogbox(myLeadsPojo, position);
    }

    private void buildUploadDialogbox(final MyLeadsPojo myLeadsPojo, final int position){
        dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.setContentView(R.layout.request_complete_upload_layout);

        customer_name = dialog.findViewById(R.id.customer_name);
        mobile_number = dialog.findViewById(R.id.mobile_number);
        invoice_image = dialog.findViewById(R.id.invoice_image);
        submit_button = dialog.findViewById(R.id.submit_button);
        cancel_button = dialog.findViewById(R.id.cancel_button);

        customer_name.append(myLeadsPojo.getName());
        mobile_number.append(myLeadsPojo.getPhone_number());

        invoice_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()){
                    CameraIntent();
                }else{
                    requestPermission();
                }
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkUtils.checkConnection()){
                    sendValuesToServer(myLeadsPojo, position);
                }else {
                    Toast.makeText(getContext(), getContext().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private boolean checkPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            return false;
        }else{
            return true;
        }
    }

    private void requestPermission(){
        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CAMERA)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    PERMISSION_REQUEST);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    CameraIntent();

                } else {
                    checkPermission();
                    Toast.makeText(getContext(), "Accept this permission to upload image", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void CameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_ACTION_INTENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == CAMERA_ACTION_INTENT){
                OpenCameraResult(data);
            }
        }else{
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void OpenCameraResult(Intent data) {

        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        assert bitmap != null;
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,bytes);

        File paths = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        Toast.makeText(getContext(), "Path -> " + paths.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        Log.d("tag","File Path -> " + paths.getName());
        file_name = paths.getName();


        try {
            FileOutputStream fos = new FileOutputStream(paths);
            paths.createNewFile();

            if(!paths.exists()){
                paths.mkdir();
            }

            fos.write(bytes.toByteArray());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        invoice_image.setImageBitmap(bitmap);

        convertFileToBase64(Uri.fromFile(paths));
    }

    private void convertFileToBase64(Uri contentUri) {
        byte[] byteArray = new byte[1024*11];

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            InputStream inputStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(contentUri);
            byte[] b = new byte[1024 * 11];
            int bytesRead = 0;

            while ((bytesRead = Objects.requireNonNull(inputStream).read(b)) != -1) {
                byteArrayOutputStream.write(b, 0, bytesRead);
            }

            byteArray = byteArrayOutputStream.toByteArray();

            Log.e("Byte array", ">" + byteArray);

        } catch (IOException e) {
            e.printStackTrace();
        }

        base64_file = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void sendValuesToServer(final MyLeadsPojo myLeadsPojo, int position){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_UPLOAD_INVOICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    if (statusCode == 200){
                        getUpdateBookingLeadsFromServer(myLeadsPojo.getBooking_id(), myLeadsPojo.getEnquiry_id());
                    }else{
                        Toast.makeText(getContext(), "Unable to update the data", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("User_ID", sharedPref.getStringValue("Vendor_User_id"));
                params.put("Enquiry_ID", myLeadsPojo.getEnquiry_id());
                params.put("Attachment_Name", file_name);
                params.put("Attachment", base64_file);
                Log.d("UploadInvoice", "getParams: "+new JSONObject(params).toString());
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);

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
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Booking Completed..", Toast.LENGTH_SHORT).show();
                        MyBookingsVendorActivity.viewPager.setCurrentItem(2, true);

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
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("enquiry_id", enquiryId);

                Log.d("FollowBookings_params", "getParams:"+new JSONObject(params).toString());
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

}
