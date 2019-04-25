package appsnova.com.doorstephub.adapters.vendor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;

public class Answer_Recyclerview_Adapter extends RecyclerView.Adapter<Answer_Recyclerview_Adapter.MyViewHolder> {
    Context mcontext;
    List<MyLeadsPojo> myLeadsPojoList;

    public Answer_Recyclerview_Adapter(List<MyLeadsPojo> myLeadsPojoList, Context mcontext) {
        this.myLeadsPojoList = myLeadsPojoList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.accepted_fragment_list_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int pos) {
        final MyLeadsPojo myLeadsPojo = myLeadsPojoList.get(pos);
        Log.d("Acceptedlistsize", "onBindViewHolder: "+myLeadsPojoList.size());
        Log.d("AcceptedlistName", "onBindViewHolder: "+myLeadsPojo.getName());
        myViewHolder.textView_name.setText("Name:"+myLeadsPojo.getName());
        myViewHolder.textView_city.setText("City:"+myLeadsPojo.getCity());
        myViewHolder.textView_description.setText("Description:"+myLeadsPojo.getDescription());
        myViewHolder.button_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext, "Accepeted", Toast.LENGTH_SHORT).show();
                //                Bundle bundle = new Bundle();
//                FollowUp_Fragment followUp_fragment = new FollowUp_Fragment();
//                Intent intent = new Intent(mcontext,FollowUp_Fragment.class);
//                bundle.putString("answered_name",myLeadsPojo.getName());
//                bundle.putString("answered_city",myLeadsPojo.getCity());
//                bundle.putString("answered_description",myLeadsPojo.getDescription());
//                intent.putExtra("bundle",bundle);
//                followUp_fragment.setArguments(bundle);
//                mcontext.startActivity(intent);
            }
        });
        myViewHolder.button_rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext, "Rejected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myLeadsPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_name,textView_city,textView_description;
        ImageButton button_accepted,button_rejected;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.accepted_textview_name);
            textView_city  = itemView.findViewById(R.id.accepted_textview_city);
            textView_description = itemView.findViewById(R.id.accepted_TV_description);
            button_accepted = itemView.findViewById(R.id.answered_accept_btn);
            button_rejected = itemView.findViewById(R.id.answered_reject_btn);
        }
    }
}
