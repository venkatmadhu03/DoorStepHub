package appsnova.com.doorstephub.activities.vendor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import appsnova.com.doorstephub.R;

public class CompletedBilingFormActivity extends AppCompatActivity {
    EditText tot_billing_amnt_ET,spare_parts_cost_ET,repairing_Cost_ET,visiting_Charges_ET;
    public TextView payable_amount_to_company_TV,upload_biling_TV,multiselectTV;
    public ImageView upload_billing_cpy,multiselect_IV;
    Button pay_amount_btn,calc_Total_Amount;
    double temporory_bill_amount;
    double thirtyPercentofTemporaryamnt;
    double eighteenPercentofresult;
    double finalAmountPayableToCompany;
    double visiting_chrgs_amt;
    public int GALLERY_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                if(!tot_billing_amnt_ET.getText().toString().isEmpty()){
                    temporory_bill_amount = Double.parseDouble(tot_billing_amnt_ET.getText().toString());
                    if((!spare_parts_cost_ET.getText().toString().isEmpty()) && (!spare_parts_cost_ET.getText().equals("")) ){
                        temporory_bill_amount = temporory_bill_amount-(Double.parseDouble(spare_parts_cost_ET.getText().toString()));
                        Log.d("sparerepair_initial", "onClick: "+temporory_bill_amount);
                    }

                    if(!repairing_Cost_ET.getText().toString().isEmpty()  && (!repairing_Cost_ET.getText().equals("")) ){
                        temporory_bill_amount = temporory_bill_amount-(Double.parseDouble(repairing_Cost_ET.getText().toString()));
                        Log.d("sparerepair_repair", "onClick:"+temporory_bill_amount);
                    }

                    thirtyPercentofTemporaryamnt=(30.0f/100.0f) * (temporory_bill_amount);
                    Log.d("sparerepair30", "onClick: "+thirtyPercentofTemporaryamnt);
                    eighteenPercentofresult = (18.0f/100.0f) * (thirtyPercentofTemporaryamnt);
                    Log.d("sparerepair18", "onClick: "+eighteenPercentofresult);
                    finalAmountPayableToCompany = eighteenPercentofresult + thirtyPercentofTemporaryamnt;
                    Log.d("sparerepairfinal", "onClick: "+finalAmountPayableToCompany);
                    payable_amount_to_company_TV.setText("Payable Amount to Company:"+String.format("%.2f", finalAmountPayableToCompany));

                    if((!visiting_Charges_ET.getText().toString().isEmpty())  && (!visiting_Charges_ET.getText().equals("")) ){
                        visiting_chrgs_amt =(Double.parseDouble(visiting_Charges_ET.getText().toString()))+((18.0f/100.0f)  *(Double.parseDouble(visiting_Charges_ET.getText().toString())));
                        payable_amount_to_company_TV.setText("Payable Amount to Company:"+visiting_chrgs_amt);
                        Log.d("visiting charges Amount", "onClick: "+String.format("%.2f", visiting_chrgs_amt));
                        spare_parts_cost_ET.setFocusable(false);
                        spare_parts_cost_ET.setClickable(false);
                        spare_parts_cost_ET.setActivated(false);
                        repairing_Cost_ET.setActivated(false);
                        repairing_Cost_ET.setClickable(false);
                        repairing_Cost_ET.setFocusable(false);
                        repairing_Cost_ET.setFocusable(false);
                    }
                }
                else{
                    Toast.makeText(CompletedBilingFormActivity.this, "Total Amount is Empty...", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CompletedBilingFormActivity.this, "Redirect To Payment Gateway...", Toast.LENGTH_SHORT).show();
            }
        });

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
}
