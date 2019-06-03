package appsnova.com.doorstephub.adapters.vendor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;

public class FollowUp_RecyclerView_Adapter extends RecyclerView.Adapter<FollowUp_RecyclerView_Adapter.AcceptedViewHolder> {
    Context mcontext;
    List<MyLeadsPojo> myaccepetedLeadsPojoList;

    public FollowUp_RecyclerView_Adapter(Context mcontext, List<MyLeadsPojo> myaccepetedLeadsPojoList) {
        this.mcontext = mcontext;
        this.myaccepetedLeadsPojoList = myaccepetedLeadsPojoList;
    }

    @NonNull
    @Override
    public AcceptedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.followup_list_row,viewGroup,false);
        AcceptedViewHolder acceptedViewHolder = new AcceptedViewHolder(view);
        return acceptedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedViewHolder acceptedViewHolder, int i) {
        MyLeadsPojo myLeadsPojo = myaccepetedLeadsPojoList.get(i);
        acceptedViewHolder.textView_acceptedname.setText("Name:"+myLeadsPojo.getName());
        acceptedViewHolder.textView_acceptedcity.setText("Service:"+myLeadsPojo.getService());
        acceptedViewHolder.textView_accepteddescription.setText("Description:"+myLeadsPojo.getDescription());
        acceptedViewHolder.button_accepetedcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "7207777712"));
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myaccepetedLeadsPojoList.size();
    }

    public class AcceptedViewHolder extends RecyclerView.ViewHolder {
        TextView textView_acceptedname,textView_acceptedcity,textView_accepteddescription;
        ImageButton button_accepetedcall,button_accepetedstart;
        public AcceptedViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_acceptedname = itemView.findViewById(R.id.followup_textview_name);
            textView_acceptedcity = itemView.findViewById(R.id.followup_textview_city);
            textView_accepteddescription = itemView.findViewById(R.id.followup_textview_description);
            button_accepetedcall = itemView.findViewById(R.id.followup_call_btn);
            button_accepetedstart = itemView.findViewById(R.id.followup_start_btn);
        }
    }
}
