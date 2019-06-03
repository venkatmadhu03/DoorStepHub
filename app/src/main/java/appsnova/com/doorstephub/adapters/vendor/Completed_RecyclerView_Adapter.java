package appsnova.com.doorstephub.adapters.vendor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;

public class Completed_RecyclerView_Adapter  extends RecyclerView.Adapter<Completed_RecyclerView_Adapter.CompletedViewHolder>{
    Context mcontext;
    List<MyLeadsPojo> myCompletedLeadsPojoList;

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
        MyLeadsPojo myLeadsPojo = myCompletedLeadsPojoList.get(i);
        completedViewHolder.textView_completedname.setText("Name:"+myLeadsPojo.getName());
        completedViewHolder.textView_completedcity.setText("Service:"+myLeadsPojo.getService());
        completedViewHolder.textView_completed_description.setText("Description:"+myLeadsPojo.getDescription());
        completedViewHolder.button_completedpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext, "Pay Amount...", Toast.LENGTH_SHORT).show();
            }
        });
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
