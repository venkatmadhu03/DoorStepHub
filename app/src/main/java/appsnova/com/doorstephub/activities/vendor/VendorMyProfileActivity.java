package appsnova.com.doorstephub.activities.vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import java.io.StringBufferInputStream;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.ProfileActivity;
import appsnova.com.doorstephub.adapters.CitiesListAdapter;
import appsnova.com.doorstephub.adapters.vendor.VendorSpinnerAdapter;
import appsnova.com.doorstephub.models.ServiceCategoryModel;
import appsnova.com.doorstephub.models.vendor.SpinnerPojoVendor;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

public class VendorMyProfileActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText_fullname, editText_email_Id,
            editText_alternate_number, editText_description,editText_address;
    TextView textView_sevices_spinner, locations_spinner, address_proof_text, Id_proof_text,
            address_proof_nameTV,idproof_name_TV,addressproof_frontTV,addressproof_backTV,
            idproof_frontTV,idproof_backTV;
    Button update_btn;
    StringBuilder servicesStringBuilder, citiesStringBuilder;
    ImageView ap_FrontImage,ap_BackImage,id_FrontImage,id_BackImage;

   /* private static final int TAKE_PICTURE = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;*/
    private static final int PERMISSION_REQUEST_CODE = 123;

    private static final int PP_ImageRequest = 1;
    private static final int AP_Front_ImageRequest =2 ;
    private static final int AP_Back_ImageRequest = 3;
    private static final int ID_Front_ImageRequest =4 ;
    private static final int ID_Back_ImageRequest= 5;

    private static final int PP_CHOOSE_REQUEST = 6;
    private static final int AP_Front_CHOOSE_REQUEST =7;
    private static final int AP_Back_CHOOSE_REQUEST = 8;
    private static final int ID_Front_CHOOSE_REQUEST = 9;
    private static final int ID_Back_CHOOSE_REQUEST = 10;

   // private Uri imageUri;

    NetworkUtils networkUtils;
    SharedPref sharedPref;
    Dialog progressDialog;

    String statusMessage,base64file1="",base64file2="",base64file3="",base64file4="";
    int statusCode;
    List<ServiceCategoryModel> serviceCategoryModelList = new ArrayList<>();
    VendorSpinnerAdapter vendorSpinnerAdapter;

    List<SpinnerPojoVendor> spinnerPojoVendorList = new ArrayList<>();
    CitiesListAdapter citiesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendoractivity_my_profile);

        servicesStringBuilder = new StringBuilder();
        citiesStringBuilder = new StringBuilder();


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (!checkSelfPermission()) {
            requestPermission();
        }

        networkUtils = new NetworkUtils(VendorMyProfileActivity.this);
        sharedPref = new SharedPref(VendorMyProfileActivity.this);
        progressDialog = UrlUtility.showCustomDialog(VendorMyProfileActivity.this);

        address_proof_text = findViewById(R.id.address_proof_text);
        Id_proof_text = findViewById(R.id.idproof);

        editText_fullname = findViewById(R.id.edittext_fullname);
        editText_email_Id = findViewById(R.id.edittext_emailId);
        editText_alternate_number = findViewById(R.id.edittet_alternatenumber);
      //  editText_description = findViewById(R.id.editext_description);
        editText_address = findViewById(R.id.editext_address);
        textView_sevices_spinner = findViewById(R.id.edittext_services_spinner);
        locations_spinner = findViewById(R.id.edittext_locations_spinner);

        address_proof_nameTV = findViewById(R.id.address_proof_nameTV);
        idproof_name_TV = findViewById(R.id.id_proof_nameTV);

        addressproof_frontTV = findViewById(R.id.addressproof_front_TV);
        addressproof_backTV = findViewById(R.id.addressproof_back_TV);
        idproof_frontTV = findViewById(R.id.idproof_front_TV);
        idproof_backTV = findViewById(R.id.idproof_backTV);

        //ImageViews

        ap_FrontImage = findViewById(R.id.addressproof_frontIV);
        ap_BackImage = findViewById(R.id.addressproof_backIV);
        id_FrontImage = findViewById(R.id.idproof_frontIV);
        id_BackImage = findViewById(R.id.idproof_backIV);


        ap_FrontImage.setOnClickListener(this);
        ap_BackImage.setOnClickListener(this);
        id_FrontImage.setOnClickListener(this);
        id_BackImage.setOnClickListener(this);

        update_btn = findViewById(R.id.update_button);

        if (networkUtils.checkConnection()){
            getProfileDetailsFromServer();
            getServicesListFromServer();
            getCitiesListFromServer();
        }else{
            Toast.makeText(this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkUtils.checkConnection()){
                    updateVendorProfileDetails();
                }else {
                    Toast.makeText(VendorMyProfileActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void updateVendorProfileDetails() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_UPDATEPROFILE_URL, new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                Log.d("VendorUpdateProfile", "onResponse:"+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode  = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if(statusCode == 200){
                        sendAttachmentsToServer();
                        Toast.makeText(VendorMyProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("VendorUpdateProfileError", "onErrorResponse: "+error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("User_ID",sharedPref.getStringValue("Vendor_User_id"));
                params.put("address",editText_address.getText().toString());
                params.put("name",editText_fullname.getText().toString());
                params.put("email",editText_email_Id.getText().toString());
                if (!citiesStringBuilder.toString().isEmpty()){
                    params.put("city_ids", citiesStringBuilder.toString());
                }else{
                    params.put("city_ids", "");
                }
                if(!servicesStringBuilder.toString().isEmpty()){
                    params.put("service_ids", servicesStringBuilder.toString());
                }else{
                    params.put("service_ids", "");
                }
                Log.d("Profile", "getParams: "+new JSONObject(params));

                return params;
            }
        };

        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    } //update vendor Profile

    private void sendAttachmentsToServer(){
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, UrlUtility.VENDOR_PROOF_ATTACHMENTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("vendorResponse", "onResponse: "+response);
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getInt("statusCode")==200){
                        Log.d("vendorResponse", "onResponse: "+response);
                        finish();
                    }else {
                        Log.d("vendorResponse", "onResponse: "+response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("vendorResponse", "onResponse: "+error.toString());
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("address_front",base64file1);
                params.put("address_back",base64file2);
                params.put("file_name1",addressproof_frontTV.getText().toString());
                params.put("file_name2",addressproof_backTV.getText().toString());
                params.put("id_back",base64file4);
                params.put("id_front",base64file3);
                params.put("file_name3",idproof_frontTV.getText().toString());
                params.put("file_name4",idproof_backTV.getText().toString());
                params.put("user_id",sharedPref.getStringValue("Vendor_User_id"));

                Log.d("AttachmentParams", "getParams:filenames "+addressproof_frontTV.getText().toString()+","+addressproof_backTV.getText().toString()
                        +","+idproof_frontTV.getText().toString()+","+idproof_backTV.getText().toString());
                Log.d("AttachmentParams", "getParams: "+new JSONObject(params).toString());


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }


    private void getProfileDetailsFromServer() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_GETPROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("VendorProfile", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if(statusCode ==  200){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                        editText_fullname.setText(jsonObject1.getString("vendor_name"));
                        editText_alternate_number.setText(jsonObject1.getString("mobile"));
                        editText_email_Id.setText(jsonObject1.getString("email"));
                        editText_address.setText(jsonObject1.getString("location"));
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
                Log.d("VendorGetProfileError", "onErrorResponse: "+error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("User_ID",sharedPref.getStringValue("Vendor_User_id"));
                Log.d("VendorProfile", "getParams: "+new JSONObject(params));
                return params;
            }
        };

        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    } //end of getProfileFromServer

    //get Services List from server
    public void getServicesListFromServer(){
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, UrlUtility.SERVICES_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("servicesResponse", "onResponse: "+response);
                    JSONObject jsonObject=new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    JSONArray jsonArray=jsonObject.getJSONArray("response");
                    if (statusCode==200){
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            ServiceCategoryModel serviceCategoryModel=new ServiceCategoryModel();
                            serviceCategoryModel.setId(jsonObject1.getString("id"));
                            serviceCategoryModel.setName(jsonObject1.getString("name"));
                            serviceCategoryModel.setServices_id(jsonObject1.getString("services_id"));
                            serviceCategoryModel.setService_image(jsonObject1.getString("service_image"));
                            serviceCategoryModelList.add(serviceCategoryModel);
                        }
                        vendorSpinnerAdapter= new VendorSpinnerAdapter(serviceCategoryModelList);
                        vendorSpinnerAdapter.notifyDataSetChanged();
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
        });
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);

    } //end of getServicesListFromServer

    public void getCitiesListFromServer(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.GET_CITIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Cities", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getInt("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if (statusCode == 200){
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i=0; i<jsonArray.length(); i++){
                            SpinnerPojoVendor spinnerPojoVendor = new SpinnerPojoVendor();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            spinnerPojoVendor.setId(jsonObject1.getString("id"));
                            spinnerPojoVendor.setLocationName(jsonObject1.getString("name"));

                            spinnerPojoVendorList.add(spinnerPojoVendor);
                        }
                        citiesListAdapter = new CitiesListAdapter(spinnerPojoVendorList, VendorMyProfileActivity.this);
                        citiesListAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Cities", "onErrorResponse: "+error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("User_ID", sharedPref.getStringValue("Vendor_User_id"));

                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    } //end of CitiesListFromServer

    //Service Spinner
    @SuppressLint("WrongConstant")
    public void servicespinner(View view) {
        final Dialog dialog = new Dialog(VendorMyProfileActivity.this);
        dialog.setContentView(R.layout.spinner_dialog_vendor);
        TextView spinner_title = dialog.findViewById(R.id.spinner_title);
        spinner_title.setText("Select Services:");

        dialog.setCanceledOnTouchOutside(true);
        RecyclerView spinnercontent = dialog.findViewById(R.id.spinner_content);

        LinearLayoutManager layoutManager = new LinearLayoutManager(VendorMyProfileActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        spinnercontent.setLayoutManager(layoutManager);

        spinnercontent.setAdapter(vendorSpinnerAdapter);

        Button Submit = dialog.findViewById(R.id.btn_submit);
        final Button Cancel = dialog.findViewById(R.id.btn_cancel);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicesStringBuilder.setLength(0);
                textView_sevices_spinner.setText("");
                //  String text = "";
                List<String> namelist = new ArrayList<>();
                List<String> serviceIdsList = new ArrayList<>();
                if (namelist.size() > 0) {
                    namelist.clear();
                }

                for (ServiceCategoryModel model : serviceCategoryModelList) {
                    if (model.isSelected()) {
                        String name = model.getName();
                        namelist.add(name);
                        serviceIdsList.add(model.getId());
                        Log.d("name", "Output : " + name);
                        Log.d("namelist", String.valueOf(namelist.size()));
                    }
                    //Log.d("TAG","Output : " + name);
                }

                String name_text = "";
                for (int i = 0; i < namelist.size(); i++) {
                    Log.d("geti", namelist.get(i));
                    //name_text = namelist.get(i);
                    textView_sevices_spinner.append(namelist.get(i)+", ");
                    servicesStringBuilder.append(serviceIdsList.get(i));
                    if (i < namelist.size() - 1) {
                        servicesStringBuilder.append(",");
                    }
                }
                //textView_sevices_spinner.setText(textView_sevices_spinner.getText().toString().substring(0, namelist.size() - 1));

                dialog.dismiss();
            }

        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VendorMyProfileActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        dialog.show();
    } //end of ServiceSpinner

    //Location Spinner
    @SuppressLint("WrongConstant")
    public void locationsspinner(View view) {

        final Dialog dialog = new Dialog(VendorMyProfileActivity.this);
        dialog.setContentView(R.layout.spinner_dialog_vendor);
        TextView spinner_title = dialog.findViewById(R.id.spinner_title);
        spinner_title.setText("Select Locations:");
        dialog.setCanceledOnTouchOutside(true);
        RecyclerView spinnercontent = dialog.findViewById(R.id.spinner_content);

        LinearLayoutManager layoutManager = new LinearLayoutManager(VendorMyProfileActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        spinnercontent.setLayoutManager(layoutManager);
        spinnercontent.setItemAnimator(new DefaultItemAnimator());

        spinnercontent.setAdapter(citiesListAdapter);
        citiesListAdapter.notifyDataSetChanged();

        Button Submit = dialog.findViewById(R.id.btn_submit);
        final Button Cancel = dialog.findViewById(R.id.btn_cancel);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citiesStringBuilder.setLength(0);
                locations_spinner.setText("");
                //  String text = "";
                List<String> namelist = new ArrayList<>();
                List<String> cityIdsList = new ArrayList<>();
                if (namelist.size() > 0) {
                    namelist.clear();
                }

                for (SpinnerPojoVendor model : spinnerPojoVendorList) {
                    if (model.isSelected()) {
                        String name = model.getLocationName();
                        namelist.add(name);
                        cityIdsList.add(model.getId());
                        Log.d("namelist", String.valueOf(namelist.size()));
                    }
                    //Log.d("TAG","Output : " + name);
                }

                String name_text = "";
                for (int i = 0; i < namelist.size(); i++) {
                    Log.d("geti", namelist.get(i));
                    name_text = namelist.get(i);
                    locations_spinner.append(name_text+ ", ");
                    citiesStringBuilder.append(cityIdsList.get(i));
                    if (i < namelist.size() - 1) {
                        citiesStringBuilder.append(",");
                    }

                    // text=sb.toString();
                    Log.d("text", name_text);
                }

                //locations_spinner.setText(locations_spinner.getText().toString().substring(0, namelist.size() - 1));

                dialog.dismiss();
            }

        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(VendorMyProfileActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return super.onSupportNavigateUp();
    }

    public void getAddressproof(View view) {
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        alertdialog.setItems(new CharSequence[]{"Aaadhar Card", "Voter Id", "PAN card"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        address_proof_nameTV.setText("Aaadhar Card");
                        break;
                    case 1:
                        address_proof_nameTV.setText("Voter Id");
                        break;
                    case 2:
                        address_proof_nameTV.setText("PAN card");
                        break;
                }
            }
        });

        alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(VendorMyProfileActivity.this, address_proof_nameTV.getText(), Toast.LENGTH_SHORT).show();
                alertdialog.setCancelable(true);

            }
        });
        alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertdialog.setCancelable(true);
            }
        });
        alertdialog.show();
    }

    public void getidproof(View view) {
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        alertdialog.setItems(new CharSequence[]{"Aaadhar Card", "Voter Id", "PAN card"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        idproof_name_TV.setText("Aaadhar Card");
                        break;
                    case 1:
                        idproof_name_TV.setText("Voter Id");
                        break;
                    case 2:
                        idproof_name_TV.setText("PAN card");
                        break;
                }
            }
        });

        alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(VendorMyProfileActivity.this, idproof_name_TV.getText(), Toast.LENGTH_SHORT).show();
                alertdialog.setCancelable(true);

            }
        });
        alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertdialog.setCancelable(true);
            }
        });
        alertdialog.show();
    }

   public void uploadButtonOptions(final String image){
       AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
       alertdialog.setTitle("Select Mode");
       alertdialog.setItems(new CharSequence[]{"Choose From Camera", "Choose From Gallery"}, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               if (which == 0) {
                   takePhotoFromCamera(image);

               } else {
                   choosePhotoFromGallery(image);
               }
           }
       });
       alertdialog.show();
   }

    private void takePhotoFromCamera(String image) {
        Intent cameraphoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (image.equalsIgnoreCase("image1")) {
            startActivityForResult(cameraphoto, PP_ImageRequest);
        }
        if (image.equalsIgnoreCase("image2")) {
            startActivityForResult(cameraphoto, AP_Front_ImageRequest);
        }
        if (image.equalsIgnoreCase("image3")) {
            startActivityForResult(cameraphoto, AP_Back_ImageRequest);
        }
        if (image.equalsIgnoreCase("image4")) {
            startActivityForResult(cameraphoto, ID_Front_ImageRequest);
        }
        if (image.equalsIgnoreCase("image5")) {
            startActivityForResult(cameraphoto, ID_Back_ImageRequest);
        }
    }

    private void choosePhotoFromGallery(String image) {
        Intent galleryPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (image.equalsIgnoreCase("image1")) {
            startActivityForResult(galleryPhotoIntent, PP_CHOOSE_REQUEST);
        }
        if (image.equalsIgnoreCase("image2")) {
            startActivityForResult(galleryPhotoIntent, AP_Front_CHOOSE_REQUEST);
        }
        if (image.equalsIgnoreCase("image3")) {
            startActivityForResult(galleryPhotoIntent, AP_Back_CHOOSE_REQUEST);
        }
        if (image.equalsIgnoreCase("image4")) {
            startActivityForResult(galleryPhotoIntent, ID_Front_CHOOSE_REQUEST);
        }
        if (image.equalsIgnoreCase("image5")) {
            startActivityForResult(galleryPhotoIntent, ID_Back_CHOOSE_REQUEST);
        }
    }


    private boolean checkSelfPermission() {
        if( ContextCompat.checkSelfPermission
                (VendorMyProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
                &&  ContextCompat.checkSelfPermission(VendorMyProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
                &&  ContextCompat.checkSelfPermission(VendorMyProfileActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
                &&  ContextCompat.checkSelfPermission(VendorMyProfileActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
                &&  ContextCompat.checkSelfPermission(VendorMyProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED

        ){
            return  true;
        }else{
            return false;
        }
    }
    //end of checkpermission

    public void requestPermission(){
        if( ActivityCompat.shouldShowRequestPermissionRationale(VendorMyProfileActivity.this,Manifest.permission.CAMERA)&&
                ActivityCompat.shouldShowRequestPermissionRationale(VendorMyProfileActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)&&
                ActivityCompat.shouldShowRequestPermissionRationale(VendorMyProfileActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(VendorMyProfileActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale(VendorMyProfileActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            Toast.makeText(this, "Allow Us pemissions. Please allow in App Settings for additional functionality.", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(VendorMyProfileActivity.this,new String[]
                            {       Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            },
                    PERMISSION_REQUEST_CODE);
        }

    } //end of requestpermissions

    public String getImagePath(Uri uri){
        String[] imageprojection = { MediaStore.Images.Media.DATA};
        Cursor image_cursor = getContentResolver().query(uri,imageprojection,null,null,null);
        if(image_cursor != null){
            int column_index = image_cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            image_cursor.moveToFirst();
            return image_cursor.getString(column_index);
        }
        else{
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Permission not Granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("result", "" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == AP_Front_ImageRequest && resultCode == RESULT_OK) {
                Uri contentURI1 = data.getData();
                String selectedImagePath  = getImagePath(contentURI1);
                File apFrontfile = new File(selectedImagePath);
                base64file1=convertFileToBase64(Uri.fromFile(apFrontfile));
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ap_FrontImage.setImageBitmap(photo);
                addressproof_frontTV.setText(editText_fullname.getText().toString()+"_add_f_"+System.currentTimeMillis()+".jpg");
                addressproof_frontTV.setTextColor(getResources().getColor(R.color.colorWhite));
            }
            else if (requestCode == AP_Front_CHOOSE_REQUEST) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    String selectedImagePath = getImagePath(contentURI);
                    File apFrontfile = new File(selectedImagePath);
                    base64file1=convertFileToBase64(Uri.fromFile(apFrontfile));
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    ap_FrontImage.setImageBitmap(bitmap);
                    addressproof_frontTV.setText(editText_fullname.getText().toString()+"_add_f_"+System.currentTimeMillis()+".jpg");
                    addressproof_frontTV.setTextColor(getResources().getColor(R.color.colorWhite));
                    Log.d("FileName", "onActivityResult:FileName "+apFrontfile.getName());

                }
            }

        }

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == AP_Back_ImageRequest && resultCode == RESULT_OK) {
                Uri contentURI1 = data.getData();
                String selectedImagePath = getImagePath(contentURI1);
                File apBackfile = new File(selectedImagePath);
                base64file2=convertFileToBase64(Uri.fromFile(apBackfile));
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ap_BackImage.setImageBitmap(photo);
                addressproof_backTV.setText(editText_fullname.getText().toString()+"_add_b_"+System.currentTimeMillis()+".jpg");
                addressproof_backTV.setTextColor(getResources().getColor(R.color.colorWhite));


            }
            else if (requestCode == AP_Back_CHOOSE_REQUEST) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    String selectedImagePath = getImagePath(contentURI);
                    File apBackfile = new File(selectedImagePath);
                    base64file2=convertFileToBase64(Uri.fromFile(apBackfile));
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    ap_BackImage.setImageBitmap(bitmap);
                    addressproof_backTV.setText(editText_fullname.getText().toString()+"_add_b_"+System.currentTimeMillis()+".jpg");
                    addressproof_backTV.setTextColor(getResources().getColor(R.color.colorWhite));

                }
            }

        }

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == ID_Front_ImageRequest && resultCode == RESULT_OK) {
                Uri contentUri = data.getData();
                String selectedImagePath = getImagePath(contentUri);
                File idFrontfile  = new File(selectedImagePath);
                base64file3=convertFileToBase64(Uri.fromFile(idFrontfile));
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                id_FrontImage.setImageBitmap(photo);
                idproof_frontTV.setText(editText_fullname.getText().toString()+"_id_f_"+System.currentTimeMillis()+".jpg");
                idproof_frontTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            }
            else if (requestCode == ID_Front_CHOOSE_REQUEST) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    String selectedImagePath = getImagePath(contentURI);
                    File idFrontfile = new File(selectedImagePath);
                    base64file3=convertFileToBase64(Uri.fromFile(idFrontfile));
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    id_FrontImage.setImageBitmap(bitmap);
                    idproof_frontTV.setText(editText_fullname.getText().toString()+"_id_f_"+System.currentTimeMillis()+".jpg");
                    idproof_frontTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                    showExif(contentURI);
                }
            }

        }

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == ID_Back_ImageRequest && resultCode == RESULT_OK) {
                Uri contentUri = data.getData();
                String selectedImagePath = getImagePath(contentUri);
                File idBackfile = new File(selectedImagePath);
                base64file4=convertFileToBase64(Uri.fromFile(idBackfile));
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                id_BackImage.setImageBitmap(photo);
                idproof_backTV.setText(editText_fullname.getText().toString()+"_id_b_"+System.currentTimeMillis()+".jpg");
                idproof_backTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            } else if (requestCode == ID_Back_CHOOSE_REQUEST) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    String selectedImagePath = getImagePath(contentURI);
                    File idBackfile   = new File(selectedImagePath);
                    base64file4=convertFileToBase64(Uri.fromFile(idBackfile));
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    id_BackImage.setImageBitmap(bitmap);
                    idproof_backTV.setText(editText_fullname.getText().toString()+"_id_b_"+System.currentTimeMillis()+".jpg");
                    idproof_backTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        }

    }

    private String convertFileToBase64(Uri contentUri) {
        byte[] byteArray = new byte[1024*11];

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(contentUri);
            byte[] b = new byte[1024 * 11];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                byteArrayOutputStream.write(b, 0, bytesRead);
            }

            byteArray = byteArrayOutputStream.toByteArray();

            Log.e("Byte array", ">" + byteArray);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String base64_file = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64_file;
    }

    @Override
    public void onClick(View v) {
       int id = v.getId();

        if(id == R.id.addressproof_frontIV){
            uploadButtonOptions("image2");
        }
        else if(id == R.id.addressproof_backIV){
            uploadButtonOptions("image3");
        }
        else if (id == R.id.idproof_frontIV){
            uploadButtonOptions("image4");
        }
        else if(id==R.id.idproof_backIV){
            uploadButtonOptions("image5");
        }
    }
}
