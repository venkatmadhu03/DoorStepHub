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
    public void onBindViewHolder(@NonNull CompletedViewHolder completedViewHolder, final int i) {
        final MyLeadsPojo myLeadsPojo = myCompletedLeadsPojoList.get(i);
        completedViewHolder.textView_completedname.setText("Name:"+myLeadsPojo.getName());
        completedViewHolder.textView_completedcity.setText("Service:"+myLeadsPojo.getService());
        completedViewHolder.textView_completed_description.setText("Description:"+myLeadsPojo.getDescription());

        if (myLeadsPojo.getTransaction_id().isEmpty() || myLeadsPojo.getTransaction_id().equalsIgnoreCase("0")
                || myLeadsPojo.getTransaction_id().equalsIgnoreCase("null")){
            completedViewHolder.button_completedpay.setVisibility(View.VISIBLE);
        }else{
            completedViewHolder.button_completedpay.setVisibility(View.GONE);
        }
        completedViewHolder.button_completedpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openForm(myCompletedLeadsPojoList.get(i).getAppointment_id(), myCompletedLeadsPojoList.get(i).getService(),
                        myCompletedLeadsPojoList.get(i).getBooking_id(), myCompletedLeadsPojoList.get(i).getEnquiry_id());
            }
        });
    }

    private void openForm(String orderId, String service, String bookingId, String enquiry_id) {
        Intent intent;
        intent = new Intent(mcontext, CompletedBilingFormActivity.class);
        intent.putExtra("appointmentId", orderId);
        intent.putExtra("service", service);
        intent.putExtra("bookingId", bookingId);
        intent.putExtra("enquiry_id", enquiry_id);

        mcontext.startActivity(intent);
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
