package appsnova.com.doorstephub.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.vendor.MainActivityVendor;
import appsnova.com.doorstephub.adapters.HomeAdapter;
import appsnova.com.doorstephub.models.ServiceCategoryModel;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    
    //create View Objects
    RecyclerView servicesCategoryRecyclerView;
    TextView navusername,nav_usermobilenumber;
    ImageView customer_profile_pic;


    int statusCode,profile_StatusCode;
    String statusMessage,profile_StatusMessage, file_name="", base64file="", mCurrentPhotoPath="";
    private static final int IMAGE_REQUEST_1 = 1;
    private static final int CHOOSE_REQUEST_1 = 2;
    private static final int GALLERY_PERMISSION = 5;

    /*Time delay for back press*/
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    //create Utils Objects
    NetworkUtils networkUtils;
    SharedPref sharedPref;
    ProgressDialog progressDialog;

    List<ServiceCategoryModel> serviceCategoryModelList;
    HomeAdapter homeAdapter;
    String navheader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //intialie Utils Objects
        serviceCategoryModelList = new ArrayList<>();
        networkUtils = new NetworkUtils(this);
        sharedPref = new SharedPref(this);
        progressDialog=UrlUtility.showProgressDialog(this);
        
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setTitleMarginStart(280);
        //toolbar.setTitleMargin(440,80,0,10);
       // toolbar.setTitleMargin((int) getResources().getDimension(R.dimen.tabtextmargin_start),(int) getResources().getDimension(R.dimen.tabtextmargin_top),0,5);
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navusername= headerView.findViewById(R.id.nav_username);
        nav_usermobilenumber= headerView.findViewById(R.id.nav_usermobilenumber);
        customer_profile_pic = headerView.findViewById(R.id.customer_profile_pic);

        getProfile();


        navigationView.setNavigationItemSelectedListener(this);
        servicesCategoryRecyclerView = findViewById(R.id.servicesCategoryRecyclerView);
      //  serviceCategoryModelList = new ArrayList<ServiceCategoryModel>();
        homeAdapter = new HomeAdapter(serviceCategoryModelList, this);


        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
       // layoutManager.setOrientation(RecyclerView.VERTICAL);
        servicesCategoryRecyclerView.hasFixedSize();
        servicesCategoryRecyclerView.setLayoutManager(layoutManager);
        servicesCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        servicesCategoryRecyclerView.setAdapter(homeAdapter);

        if (networkUtils.checkConnection()){
            getServicesListFromServer();
        }

        customer_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

    } //onCreate

    private void checkPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
        } else {
           choosePhotoFromGallery();
        }

    }

    private void requestPermission(){
        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                        Manifest.permission.CAMERA)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    GALLERY_PERMISSION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GALLERY_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //buildAlertDialogBox();
                    choosePhotoFromGallery();
                } else {
                    checkPermission();
                    Toast.makeText(this, "Accept this permission to upload image", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    private void buildAlertDialogBox(){
        AlertDialog.Builder alert_dialog = new AlertDialog.Builder(HomeActivity.this);
        alert_dialog.setTitle("Choose Mode:");
        alert_dialog.setItems(new CharSequence[]{"Capture Image", "Choose Files"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    takePhotoFromCamera();
                } else {
                    choosePhotoFromGallery();
                }
            }
        });
        alert_dialog.show();
    }



    private void takePhotoFromCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        File photoFile = null;
        Uri photoURI;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.provider",
                        photoFile);
                //FAApplication.setPhotoUri(photoURI);
            } else {
                photoURI = Uri.fromFile(photoFile);
            }

            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePicture, IMAGE_REQUEST_1);
        }
    }

    private void choosePhotoFromGallery() {
        Intent galleryPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryPhotoIntent, CHOOSE_REQUEST_1);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);


        /*File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                + File.separator
                + imageFileName);
        if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
            mCurrentPhotoPath = file.getAbsolutePath();
        }*/


        File file = File.createTempFile(
                imageFileName,   //prefix
                ".jpg",          //suffix
                storageDir       //directory
        );
        mCurrentPhotoPath = file.getAbsolutePath();
        // Save a file: path for use with ACTION_VIEW intents

        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == IMAGE_REQUEST_1 && resultCode == RESULT_OK) {


                Uri contentUri = FileProvider.getUriForFile(this,
                        "com.example.provider",
                        new File(mCurrentPhotoPath)
                ); //You wll get the proper image uri here.

                Log.d("HomeActivity", "onActivityResult: "+contentUri);
                String selectedImagePath  = getImagePath(contentUri);
                File apFrontfile = new File(selectedImagePath);
                base64file=convertFileToBase64(Uri.fromFile(apFrontfile));
               // Bitmap photo = (Bitmap) data.getExtras().get("data");
                //navheader_imageview.setImageBitmap(photo);
                file_name = System.currentTimeMillis()+".jpg";
            }
            else if (requestCode == CHOOSE_REQUEST_1) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    String selectedImagePath = getImagePath(contentURI);
                    File apFrontfile = new File(selectedImagePath);
                    base64file=convertFileToBase64(Uri.fromFile(apFrontfile));
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    // navheader_imageview.setImageBitmap(bitmap);
                    file_name = System.currentTimeMillis()+".jpg";
                }
            }

            if (!base64file.isEmpty()){
                if (networkUtils.checkConnection()){
                    sendProfileImageToServer(base64file, file_name);
                }
            }

        }

    } //end of OnActivity result

    private void sendProfileImageToServer(final String base64file, final String file_name){
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.UPDATE_PROFILE_IMAGE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ProfilePhoto", "onResponse: "+response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("statusCode") ==  200){
                        progressDialog.dismiss();
                        UrlUtility.showCustomToast(jsonObject.getString("statusMessage"), HomeActivity.this);
                        getProfile();
                    }else{
                        progressDialog.dismiss();
                        UrlUtility.showCustomToast(jsonObject.getString("statusMessage"), HomeActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Hello", "onErrorResponse: "+error.toString());
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", sharedPref.getStringValue("User_Id"));
                params.put("profile_photo", base64file);
                params.put("file_name", file_name);
                params.put("user_role", "customer");

                Log.d("ProfileParams", "getParams: "+new JSONObject(params).toString());

                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
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

    public String getImagePath(Uri uri){

        String[] imageprojection = { MediaStore.Images.Media.DATA};
        Cursor image_cursor = getContentResolver().query(uri,imageprojection,null,null,null);
        if(image_cursor != null){
            int column_index = image_cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            image_cursor.moveToFirst();
            String path = image_cursor.getString(column_index);
            image_cursor.close();
            Log.d("Path", "getImagePath: "+path);
            return path;
        }
        else{
            return null;
        }
    }

    private void getProfile() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.GET_PROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GetProfileResponse", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    profile_StatusCode = jsonObject.getInt("statusCode");
                    profile_StatusMessage = jsonObject.getString("statusMessage");
                    if(profile_StatusCode ==200){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                        navusername.setText(jsonObject1.getString("name"));
                        nav_usermobilenumber.setText(jsonObject1.getString("mobile"));
                        if(jsonObject1.getString("attachment") !=null || jsonObject1.getString("attachment").isEmpty()){
                            Picasso
                                    .get()
                                    .load(jsonObject1.getString("attachment"))
                                    .error(R.drawable.user_profile)
                                    .placeholder(R.drawable.user_profile).into(customer_profile_pic);
                        }else{
                            Picasso
                                    .get()
                                    .load(R.drawable.user_profile)
                                    .error(R.drawable.user_profile)
                                    .placeholder(R.drawable.user_profile).into(customer_profile_pic);
                        }
                        sharedPref.setStringValue("email",jsonObject1.getString("email"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "Error Loading Profile!!", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("Mobile_Number",sharedPref.getStringValue("MobileNumber"));
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

    //get Services List from server
    public void getServicesListFromServer(){
        progressDialog.show();
        serviceCategoryModelList.clear();
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
                        homeAdapter.notifyDataSetChanged();
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Press once again to exit!",
                        Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
       /* Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);*/
       //finishAffinity();

    } //onBackPressed

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        }  else if (id == R.id.nav_mybookings) {
            if (networkUtils.checkConnection()) {
                startActivity(new Intent(this, MyBookingsActivity.class));
            } else {
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }
        }
        else if (id == R.id.nav_Pivacy_policy) {
            if (networkUtils.checkConnection()){
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
            }else{
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }
        }
        else if (id == R.id.nav_Termscond) {
            if (networkUtils.checkConnection()){
                startActivity(new Intent(this, TermsConditionsActivity.class));
            }else{
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }
        }
        else if (id == R.id.nav_Aboutus) {
            if (networkUtils.checkConnection()){
                startActivity(new Intent(this, AboutUsActivity.class));
            }else{
                UrlUtility.showCustomToast(getResources().getString(R.string.no_connection), this);
            }
        }
        else if (id == R.id.nav_share) {
            String sharetext = "Hey check out my app at: https://play.google.com/store/apps/details?id="+this.getPackageName();
            Log.d("sharetext", "onNavigationItemSelected: "+sharetext);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,sharetext);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        else if (id == R.id.nav_logout) {
            sharedPref.removeSession("MobileNumber");
            sharedPref.removeSession("User_Id");
            Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    } //onNavigationItemSelected

    public void profileDetails(View view) {
        Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
        startActivity(intent);
    }
}
