package appsnova.com.doorstephub.activities.vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.ProfileActivity;
import appsnova.com.doorstephub.adapters.vendor.VendorSpinnerAdapter;
import appsnova.com.doorstephub.models.vendor.SpinnerPojoVendor;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

public class VendorMyProfileActivity extends AppCompatActivity {
    EditText editText_fullname, editText_email_Id,
            editText_alternate_number, editText_description,editText_address;
    TextView textView_sevices_spinner, locations_spinner, profile_pic_text, address_proof_text, Id_proof_text;
    Button update_btn;
    StringBuilder sb;

    private static final int TAKE_PICTURE = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
   // private Uri imageUri;

    NetworkUtils networkUtils;
    SharedPref sharedPref;
    ProgressDialog progressDialog;

    String statusCode,statusMessage,vendorstatusCode,vendorstatusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendoractivity_my_profile);
        sb = new StringBuilder();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        networkUtils = new NetworkUtils(VendorMyProfileActivity.this);
        sharedPref = new SharedPref(VendorMyProfileActivity.this);
        progressDialog = UrlUtility.showProgressDialog(VendorMyProfileActivity.this);

        profile_pic_text = findViewById(R.id.profilepic_filename);
        address_proof_text = findViewById(R.id.address_proof_text);
        Id_proof_text = findViewById(R.id.idprooffile_name);

        editText_fullname = findViewById(R.id.edittext_fullname);
        editText_email_Id = findViewById(R.id.edittext_emailId);
        editText_alternate_number = findViewById(R.id.edittet_alternatenumber);
      //  editText_description = findViewById(R.id.editext_description);
        editText_address = findViewById(R.id.editext_address);
        textView_sevices_spinner = findViewById(R.id.edittext_services_spinner);
        locations_spinner = findViewById(R.id.edittext_locations_spinner);

        update_btn = findViewById(R.id.update_button);

        if (networkUtils.checkConnection()){

            getProfileDetailsFromServer();
        }

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateVendorProfileDetails();
              //  Toast.makeText(VendorMyProfileActivity.this, "Certified Technician Amount", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void updateVendorProfileDetails() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_UPDATEPROFILE_URL, new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                Log.d("VendorUpdateProfileResponse", "onResponse:"+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    vendorstatusCode  = jsonObject.getString("statusCode");
                    vendorstatusMessage = jsonObject.getString("statusMessage");
                    if(vendorstatusCode.equalsIgnoreCase("200")){
                        Toast.makeText(VendorMyProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
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
                params.put("User_ID","21");
                params.put("address",editText_address.getText().toString());
                params.put("name",editText_fullname.getText().toString());
                params.put("email",editText_email_Id.getText().toString());
                return params;
            }
        };

        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

    private void getProfileDetailsFromServer() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.VENDOR_GETPROFILE_URL, new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                Log.d("VendorGetProfileResponse", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    statusCode = jsonObject.getString("statusCode");
                    statusMessage = jsonObject.getString("statusMessage");
                    if(statusCode.equalsIgnoreCase("200")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                        editText_fullname.setText(jsonObject1.getString("vendor_name"));
                        editText_alternate_number.setText(jsonObject1.getString("mobile"));
                        editText_email_Id.setText(jsonObject1.getString("email"));
                        editText_address.setText(jsonObject1.getString("address"));
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
                params.put("User_ID","21");
                return params;
            }
        };

        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    }

    //Service Spinner
    @SuppressLint("WrongConstant")
    public void servicespinner(View view) {
        final Dialog dialog = new Dialog(VendorMyProfileActivity.this);
        dialog.setContentView(R.layout.spinner_dialog_vendor);
        TextView spinner_title = dialog.findViewById(R.id.spinner_title);
        spinner_title.setText("Select Services:");
      //  dialog.setTitle("Select Services");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //  dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);
        RecyclerView spinnercontent = dialog.findViewById(R.id.spinner_content);

        final List<SpinnerPojoVendor> mydata_list = new ArrayList<SpinnerPojoVendor>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(VendorMyProfileActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        spinnercontent.setLayoutManager(layoutManager);
        spinnercontent.setItemAnimator(new DefaultItemAnimator());

        SpinnerPojoVendor mydatacontent = new SpinnerPojoVendor("Computer Repairs");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Laptop Services");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Hardware Connections");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Network Services");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("TV Repair Services");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Refrigirator Services");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Geasar Reapir");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Electricity Connections");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Pesticides");
        mydata_list.add(mydatacontent);

        VendorSpinnerAdapter mySpinnerAdapter = new VendorSpinnerAdapter(mydata_list);

        spinnercontent.setAdapter(mySpinnerAdapter);
        mySpinnerAdapter.notifyDataSetChanged();

        Button Submit = dialog.findViewById(R.id.btn_submit);
        final Button Cancel = dialog.findViewById(R.id.btn_cancel);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VendorMyProfileActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                sb.setLength(0);
                textView_sevices_spinner.setText("");
                //  String text = "";
                List<String> namelist = new ArrayList<>();
                if (namelist.size() > 0) {
                    namelist.clear();
                }

                for (SpinnerPojoVendor model : mydata_list) {
                    if (model.isSelected()) {
                        String name = model.getName();
                        namelist.add(name);
                        Log.d("name", "Output : " + name);
                        Log.d("namelist", String.valueOf(namelist.size()));
                    }
                    //Log.d("TAG","Output : " + name);
                }

                String name_text = "";
                for (int i = 0; i < namelist.size(); i++) {
                    Log.d("geti", namelist.get(i));
                    name_text = namelist.get(i);

                    sb.append(name_text);
                    if (i < namelist.size() - 1) {
                        sb.append(",");
                    }

                    Toast.makeText(VendorMyProfileActivity.this, sb, Toast.LENGTH_SHORT).show();

                    // text=sb.toString();
                    Log.d("text", name_text);
                }

                textView_sevices_spinner.setText(sb);
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
    }
//Location Spinner
    @SuppressLint("WrongConstant")
    public void locationsspinner(View view) {

        final Dialog dialog = new Dialog(VendorMyProfileActivity.this);
        dialog.setContentView(R.layout.spinner_dialog_vendor);
      //  dialog.setTitle("Select Services");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        TextView spinner_title = dialog.findViewById(R.id.spinner_title);
        spinner_title.setText("Select Locations:");
        //  dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);
        RecyclerView spinnercontent = dialog.findViewById(R.id.spinner_content);

        final List<SpinnerPojoVendor> mydata_list = new ArrayList<SpinnerPojoVendor>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(VendorMyProfileActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        spinnercontent.setLayoutManager(layoutManager);
        spinnercontent.setItemAnimator(new DefaultItemAnimator());

        SpinnerPojoVendor mydatacontent = new SpinnerPojoVendor("Hyderabad");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Bangalore");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("NewDelhi");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Chennai");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Mumbai");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Raichur");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Singapore");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Jaipur");
        mydata_list.add(mydatacontent);
        mydatacontent = new SpinnerPojoVendor("Maharashtra");
        mydata_list.add(mydatacontent);

        VendorSpinnerAdapter mySpinnerAdapter = new VendorSpinnerAdapter(mydata_list);

        spinnercontent.setAdapter(mySpinnerAdapter);
        mySpinnerAdapter.notifyDataSetChanged();

        Button Submit = dialog.findViewById(R.id.btn_submit);
        final Button Cancel = dialog.findViewById(R.id.btn_cancel);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VendorMyProfileActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                sb.setLength(0);
                locations_spinner.setText("");
                //  String text = "";
                List<String> namelist = new ArrayList<>();
                if (namelist.size() > 0) {
                    namelist.clear();
                }

                for (SpinnerPojoVendor model : mydata_list) {
                    if (model.isSelected()) {
                        String name = model.getName();
                        namelist.add(name);
                        Log.d("name", "Output : " + name);
                        Log.d("namelist", String.valueOf(namelist.size()));
                    }
                    //Log.d("TAG","Output : " + name);
                }

                String name_text = "";
                for (int i = 0; i < namelist.size(); i++) {
                    Log.d("geti", namelist.get(i));
                    name_text = namelist.get(i);

                    sb.append(name_text);
                    if (i < namelist.size() - 1) {
                        sb.append(",");
                    }

                    Toast.makeText(VendorMyProfileActivity.this, sb, Toast.LENGTH_SHORT).show();

                    // text=sb.toString();
                    Log.d("text", name_text);
                }

                locations_spinner.setText(sb);
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

    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return super.onSupportNavigateUp();
    }

    public void getidproof(View view) {
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        alertdialog.setItems(new CharSequence[]{"Aaadhar Card", "Voter Id", "PAN card"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

   /* public void getAddressproof(View view) {
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        alertdialog.setItems(new CharSequence[]{"Aaadhar Card", "Voter Id", "Residential Proof"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
    }*/

   public void uploadButtonOptions(){
       AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
       alertdialog.setTitle("Select Mode");
       alertdialog.setItems(new CharSequence[]{"Choose From Camera", "Choose From Gallery"}, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               if (which == 0) {
                   if (ActivityCompat.checkSelfPermission(VendorMyProfileActivity.this, Manifest.permission.CAMERA) !=
                           PackageManager.PERMISSION_GRANTED) {
                       ActivityCompat.requestPermissions(VendorMyProfileActivity.this,
                               new String[]{Manifest.permission.CAMERA},
                               TAKE_PICTURE); }
                   else {
                       Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                       startActivityForResult(cameraIntent, TAKE_PICTURE);
                   }

               } else {
                   if (ActivityCompat.checkSelfPermission(VendorMyProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                           PackageManager.PERMISSION_GRANTED) {
                       ActivityCompat.requestPermissions(VendorMyProfileActivity.this,
                               new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                               PICK_FROM_GALLERY); }
                   else{
                       Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                       startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                   }
               }
           }
       });
       alertdialog.show();
   }

    public void uploadbutton(View view) {
       uploadButtonOptions();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    //do something like displaying a message that he didn`t allow
                    // the app to access gallery and you wont be able to let him select from gallery
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case TAKE_PICTURE:
                /*// If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                    imageUri = Uri.fromFile(photo);
                    startActivityForResult(intent, TAKE_PICTURE);
                } else {
                    //do something like displaying a message that he didn`t allow
                    // the app to access gallery and you wont be able to let him select from gallery
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }*/
                if (requestCode == MY_CAMERA_PERMISSION_CODE) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                        Intent cameraIntent = new
                                Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, TAKE_PICTURE);
                    } else {
                        Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                    }
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                /*if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);

                    try {
                        profile_pic_text.setText(selectedImage.toString());
                        Toast.makeText(this, selectedImage.toString(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }

                }*/
                if (requestCode == TAKE_PICTURE && resultCode ==RESULT_OK && null!=data) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    //mImage.setImageBitmap(thumbnail);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    if (thumbnail != null) {
                        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    }
                    File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                    try {
                        file.createNewFile();
                        FileOutputStream fo = new FileOutputStream(file);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    profile_pic_text.setText(file.getName());
                }

                break;

            case PICK_FROM_GALLERY:
                if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    profile_pic_text.setText(picturePath);
                }
        }
    }

    public void profilepicuploadbutton(View view) {
        uploadButtonOptions();
    }
}
