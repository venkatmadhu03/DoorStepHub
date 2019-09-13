package appsnova.com.doorstephub.activities.vendor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.instamojo.android.Instamojo;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.utilities.NetworkUtils;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

public class CompletedBilingFormActivity extends AppCompatActivity implements PaymentResultListener {
    EditText tot_billing_amnt_ET,spare_parts_cost_ET,repairing_Cost_ET,visiting_Charges_ET;
    public TextView payable_amount_to_company_TV,upload_biling_TV,multiselectTV;
    public ImageView upload_billing_cpy,multiselect_IV;
    Button pay_amount_btn,calc_Total_Amount;
    double temporory_bill_amount=0, spare_parts_bill_amount=0, repairing_bill_amount=0;
    double thirtyPercentofTemporaryamnt=0;
    double eighteenPercentofresult=0;
    double finalAmountPayableToCompany=0;
    double visiting_chrgs_amt=0;
    public int GALLERY_REQUEST = 1;
    String bookingId="", service="", uploaded_filename="", appointmentId="";


    Bundle bundle;


    //Utils object creation
    SharedPref sharedPref;
    NetworkUtils networkUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        networkUtils = new NetworkUtils(this);

        bundle = getIntent().getExtras();
        if (bundle !=null){
            bookingId = bundle.getString("bookingId");
            service = bundle.getString("service");
            appointmentId = bundle.getString("appointmentId");

            Log.d("CompleteBilling", "onCreate: "+bookingId+"<"+service+","+appointmentId);
        }

         /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());

        setContentView(R.layout.completed_payform_dialog);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tot_billing_amnt_ET = findViewById(R.id.total_billing_amt_ET);
        spare_parts_cost_ET = findViewById(R.id.spare_parts_cost_ET);
        repairing_Cost_ET = findViewById(R.id.repairing_cost_ET);
        visiting_Charges_ET = findViewById(R.id.visiting_charges_ET);
        payable_amount_to_company_TV =findViewById(R.id.payable_amt_TV);
        upload_biling_TV = findViewById(R.id.upload_bill_TV);
        multiselectTV =findViewById(R.id.multiselect_TV);
        upload_billing_cpy =findViewById(R.id.upload_Bill_IV);
        multiselect_IV = findViewById(R.id.multi_select_IV);
        pay_amount_btn = findViewById(R.id.pay_btn);
        calc_Total_Amount =findViewById(R.id.calc_Total_Amount);

        calc_Total_Amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spare_parts_cost_ET.setFocusable(true);
                spare_parts_cost_ET.setClickable(true);
                repairing_Cost_ET.setClickable(true);
                repairing_Cost_ET.setFocusable(true);

                if(!tot_billing_amnt_ET.getText().toString().isEmpty() && !uploaded_filename.isEmpty() ){

                    if(repairing_Cost_ET.getText().toString().isEmpty()  && (repairing_Cost_ET.getText().equals("")) ){
                        repairing_bill_amount = 0;
                    }else{
                        repairing_bill_amount = Double.parseDouble(repairing_Cost_ET.getText().toString());
                    }
                    if((!visiting_Charges_ET.getText().toString().isEmpty())  && (!visiting_Charges_ET.getText().equals("")) ){
                        visiting_chrgs_amt =Double.parseDouble(visiting_Charges_ET.getText().toString());
                    }else{
                        visiting_chrgs_amt = 0;
                    }
                    if(spare_parts_cost_ET.getText().toString().isEmpty()  && (repairing_Cost_ET.getText().equals("")) ){
                        spare_parts_bill_amount = 0;
                    }else{
                        spare_parts_bill_amount = Double.parseDouble(repairing_Cost_ET.getText().toString());
                    }

                    //temporory_bill_amount = repairing_bill_amount+visiting_chrgs_amt;
                    temporory_bill_amount = repairing_bill_amount+visiting_chrgs_amt+spare_parts_bill_amount;
                    if (Double.parseDouble(tot_billing_amnt_ET.getText().toString()) == temporory_bill_amount){
                        thirtyPercentofTemporaryamnt=(10.0f/100.0f) * (temporory_bill_amount);
                        //eighteenPercentofresult = (18.0f/100.0f) * (thirtyPercentofTemporaryamnt);
                        finalAmountPayableToCompany = eighteenPercentofresult + thirtyPercentofTemporaryamnt;

                        payable_amount_to_company_TV.setText("Payable Amount to Company:"+String.format("%.2f", finalAmountPayableToCompany));
                    }
//                    if (Double.parseDouble(tot_billing_amnt_ET.getText().toString()) == temporory_bill_amount+ spare_parts_bill_amount){
//                        thirtyPercentofTemporaryamnt=(30.0f/100.0f) * (temporory_bill_amount);
//                        eighteenPercentofresult = (18.0f/100.0f) * (thirtyPercentofTemporaryamnt);
//                        finalAmountPayableToCompany = eighteenPercentofresult + thirtyPercentofTemporaryamnt;
//
//                        payable_amount_to_company_TV.setText("Payable Amount to Company:"+String.format("%.2f", finalAmountPayableToCompany));
//                    }


                }
                else{
                    Toast.makeText(CompletedBilingFormActivity.this, "Total Amount and bill upload is Mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });
        upload_billing_cpy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,GALLERY_REQUEST);
            }
        });
        pay_amount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkUtils.checkConnection()){
                    //sendRequestToServer();
                    startPayment();
                }else{
                    Toast.makeText(CompletedBilingFormActivity.this, getApplicationContext().getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }

               // initiateSDKPayment(orderId);
            }
        });

    }

    private void startPayment(){
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", sharedPref.getStringValue("vendor_name"));
            options.put("description", service);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://www.doorstephub.com//themes/dhb/img/logo.png");
            options.put("currency", "INR");
            options.put("amount", String.format("%.0f", finalAmountPayableToCompany*100));

            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            preFill.put("contact", sharedPref.getStringValue("mobile"));

            options.put("prefill", preFill);

            Log.d("CompleteBilling", "startPayment: "+options.toString());

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    private void sendRequestToServer(final String transactionId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtility.CREATE_PAYMENT_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CompletePayment", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("statusCode") == 200){
                        buildNotification();
                        startActivity(new Intent(CompletedBilingFormActivity.this, MainActivityVendor.class));
                    }else{
                        Toast.makeText(CompletedBilingFormActivity.this, jsonObject.getString("statusMessage"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CompletePayment", "onResponse: "+error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("amount", String.format("%.2f", finalAmountPayableToCompany));
                params.put("appointment_id", appointmentId);
                params.put("booking_id", bookingId);
                params.put("user_id", sharedPref.getStringValue("Vendor_User_id"));
                params.put("role_id", sharedPref.getStringValue("role_id"));
                params.put("transaction_id", transactionId);

                Log.d("CompletePayment", "getParams: "+new JSONObject(params).toString());
                return params;
            }
        };
        VolleySingleton.getmApplication().getmRequestQueue().getCache().clear();
        VolleySingleton.getmApplication().getmRequestQueue().add(stringRequest);
    } //end of sendRequestToSerer

    private void buildNotification(){
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext());

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            String channelId="DoorStepHub";
            NotificationChannel notificationChannel=new NotificationChannel(channelId,"DoorStepHub", NotificationManager.IMPORTANCE_HIGH);
            Notification testNotification=new Notification.Builder(getApplicationContext(),channelId)
                    .setContentTitle("DoorStepHub")
                    .setContentText("Payment Successful")
                    .setSmallIcon(R.drawable.applogo)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build();
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(0,testNotification);
        }else {
            Notification foregroundNote=builder.setContentTitle("DoorStepHub")
                    .setSmallIcon(R.drawable.applogo)
                    .setContentTitle("DoorStepHub")
                    .setAutoCancel(true)
                    .setContentText("Payment Successful")
                    .build();
            notificationManager.notify(0,foregroundNote);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST) {
            if (data != null) {
                Uri contentURI = data.getData();
                String selectedImagePath = getImagePath(contentURI);
                Log.d("selectedImagePath", "onActivityResult:CompleteBillingForm"+selectedImagePath);
                File file = new File(selectedImagePath);
               /* Picasso.get().load(selectedImagePath)
                        .placeholder(android.R.drawable.ic_menu_report_image)
                        .error(android.R.drawable.stat_notify_error)
                        .into(upload_billing_cpy);*/
//                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uploaded_filename = file.getName();
                upload_billing_cpy.setImageBitmap(bitmap);
                upload_biling_TV.setText(file.getName());
            }
        }
    }
    public String getImagePath(Uri uri){
        String[] imageprojection = {MediaStore.Images.Media.DATA};
        Cursor image_cursor = getContentResolver().query(uri,imageprojection,null,null,null);
        if(image_cursor!= null){
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
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public void onPaymentSuccess(String s) {
       // Toast.makeText(this, "Success "+s, Toast.LENGTH_SHORT).show();
        Log.d("PaymentSuccess", "onPaymentSuccess: "+s);
        sendRequestToServer(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Error "+i+","+s, Toast.LENGTH_SHORT).show();
    }
}
