package appsnova.com.doorstephub.adapters.vendor;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.vendor.CompletedBilingFormActivity;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;

import static android.app.Activity.RESULT_OK;

public class Completed_RecyclerView_Adapter  extends RecyclerView.Adapter<Completed_RecyclerView_Adapter.CompletedViewHolder>{
    Context mcontext;
    List<MyLeadsPojo> myCompletedLeadsPojoList;
      /* EditText tot_billing_amnt_ET,spare_parts_cost_ET,repairing_Cost_ET,visiting_Charges_ET;
    public TextView payable_amount_to_company_TV,upload_biling_TV,multiselectTV;
    public ImageView upload_billing_cpy,multiselect_IV;
    Button pay_amount_btn,calc_Total_Amount;
    double temporory_bill_amount;
    double thirtyPercentofTemporaryamnt;
    double eighteenPercentofresult;
    double finalAmountPayableToCompany;
    double visiting_chrgs_amt;
    public int GALLERY_REQUEST = 1;
    int dataSize=0;
    int file_size;
    String path="",fileName;
    File uploadedFile;*/

    public Completed_RecyclerView_Adapter(Context mcontext, List<MyLeadsPojo> myCompletedLeadsPojoList) {
        this.mcontext = mcontext;
        this.myCompletedLeadsPojoList = myCompletedLeadsPojoList;
    }
    @NonNull
    @Override
    public CompletedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.completed_fragment_list_row,viewGroup,false);
        return new CompletedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedViewHolder completedViewHolder, int i) {
        final MyLeadsPojo myLeadsPojo = myCompletedLeadsPojoList.get(i);
        completedViewHolder.textView_completedname.setText("Name:"+myLeadsPojo.getName());
        completedViewHolder.textView_completedcity.setText("Service:"+myLeadsPojo.getService());
        completedViewHolder.textView_completed_description.setText("Description:"+myLeadsPojo.getDescription());
        completedViewHolder.button_completedpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openForm(myLeadsPojo.getBooking_id());
            }
        });
    }

    private void openForm(String orderId) {
        Intent intent;
        intent = new Intent(mcontext, CompletedBilingFormActivity.class);
        intent.putExtra("orderId", orderId);
        mcontext.startActivity(intent);
        /*  Dialog dialog = new Dialog(mcontext);
        dialog.setContentView(R.layout.completed_payform_dialog);

        tot_billing_amnt_ET = dialog.findViewById(R.id.total_billing_amt_ET);
        spare_parts_cost_ET = dialog.findViewById(R.id.spare_parts_cost_ET);
        repairing_Cost_ET = dialog.findViewById(R.id.repairing_cost_ET);
        visiting_Charges_ET = dialog.findViewById(R.id.visiting_charges_ET);
        payable_amount_to_company_TV = dialog.findViewById(R.id.payable_amt_TV);
        upload_biling_TV = dialog.findViewById(R.id.upload_bill_TV);
        multiselectTV =dialog.findViewById(R.id.multiselect_TV);
        upload_billing_cpy = dialog.findViewById(R.id.upload_Bill_IV);
        multiselect_IV = dialog.findViewById(R.id.multi_select_IV);
        pay_amount_btn = dialog.findViewById(R.id.pay_btn);
        calc_Total_Amount =dialog.findViewById(R.id.calc_Total_Amount);

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
                    Toast.makeText(mcontext, "Total Amount is Empty...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        upload_billing_cpy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("image/*");

//                ((Activity) mcontext).startActivityForResult(Intent.createChooser(intent,"ChooseImage"), GALLERY_REQUEST);
                ((Activity)mcontext).startActivityForResult(intent,GALLERY_REQUEST);
            }
        });
        pay_amount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext, "Redirect To Payment Gateway...", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();*/
    }
    @Override
    public int getItemCount() {
        return myCompletedLeadsPojoList.size();
    }

    public class CompletedViewHolder extends RecyclerView.ViewHolder {
        TextView textView_completedname,textView_completedcity,textView_completed_description;
        Button button_completedpay;
        public CompletedViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_completedname = itemView.findViewById(R.id.completed_textview_name);
            textView_completedcity = itemView.findViewById(R.id.completed_textview_city);
            textView_completed_description = itemView.findViewById(R.id.completed_textview_description);
            button_completedpay = itemView.findViewById(R.id.completed_pay_btn);
        }
    }


}
