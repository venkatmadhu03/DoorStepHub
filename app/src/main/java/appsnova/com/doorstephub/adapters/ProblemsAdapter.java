package appsnova.com.doorstephub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.net.Inet4Address;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.ServiceScheduleActivity;
import appsnova.com.doorstephub.models.ProblemsModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProblemsAdapter extends RecyclerView.Adapter<ProblemsAdapter.ProblemsViewholder> {

    List<ProblemsModel> problemsModelList;
    Context context;
    String serviceId="", serviceName="", subServiceId="", subServiceName="";

    public ProblemsAdapter(List<ProblemsModel> problemsModelList, Context context, String serviceId, String serviceName, String subServiceId, String subServiceName) {
        this.problemsModelList = problemsModelList;
        this.context = context;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.subServiceId = subServiceId;
        this.subServiceName = subServiceName;
    }

    @NonNull
    @Override
    public ProblemsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.problems_layout, null);
        return new ProblemsViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProblemsViewholder holder, final int position) {
        holder.problemsTextView.setText(problemsModelList.get(position).getName());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ServiceScheduleActivity.class);
                intent.putExtra("problem_id", problemsModelList.get(position).getId());
                intent.putExtra("problem_name", problemsModelList.get(position).getName());
                intent.putExtra("Service_Id", serviceId);
                intent.putExtra("Service_Name", serviceName);
                intent.putExtra("serviceSelectionId", subServiceId);
                intent.putExtra("serviceSelections", subServiceName);
                intent.putExtra("IntentFrom", "subServiceSelection");


                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return problemsModelList.size();
    }

    public class ProblemsViewholder extends RecyclerView.ViewHolder {
        TextView problemsTextView;
        RelativeLayout container;
        public ProblemsViewholder(@NonNull View itemView)
        {
            super(itemView);
            problemsTextView = itemView.findViewById(R.id.problemsTextView);
            container = itemView.findViewById(R.id.container);
        }
    }

}
