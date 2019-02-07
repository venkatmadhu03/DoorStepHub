package appsnova.com.doorstephub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.models.MyBookingsModel;

public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.MyBookingsViewHolder> {
    List<MyBookingsModel> myBookingsModelList;

    public MyBookingsAdapter(List<MyBookingsModel> myBookingsModelList) {
        this.myBookingsModelList = myBookingsModelList;
    }

    @NonNull
    @Override
    public MyBookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.mybooking_list_row,parent,false);
        MyBookingsViewHolder myBookingsViewHolder =new MyBookingsViewHolder(view);
        return myBookingsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookingsViewHolder holder, int position) {
        MyBookingsModel myBookingsModel = myBookingsModelList.get(position);

        holder.mybooking_orderidtxt.setText("Order ID:"+myBookingsModel.getOrderid());
        holder.mybooking_nametxt.setText("Name:"+myBookingsModel.getUsername());
        holder.mybooking_servicerequiredtxt.setText("Service Required:"+myBookingsModel.getServicerequired());
        holder.mybooking_subservicetxt.setText("Sub Service:"+myBookingsModel.getSubservice());
        holder.mybooking_scheduleddatetxt.setText("Scheduled On:"+myBookingsModel.getScheduleddate());

           /* for(int i=0;i<myBookingsModelList.size();i++){
                if(position<myBookingsModelList.size()-1){
                    holder.mybookings_horizontaldivider.setVisibility(View.GONE);
                }
            }*/

    }



    @Override
    public int getItemCount() {
        return myBookingsModelList.size();
    }

    public class MyBookingsViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mybookings_rowlayout;
        View mybookings_horizontaldivider;
        TextView mybooking_orderidtxt,mybooking_nametxt,mybooking_servicerequiredtxt,mybooking_subservicetxt,
                mybooking_scheduleddatetxt;
        public MyBookingsViewHolder(@NonNull View itemView) {
            super(itemView);

            mybookings_rowlayout = itemView.findViewById(R.id.mybookings_rowlayout);
            mybookings_horizontaldivider = itemView.findViewById(R.id.mybookings_horizontaldivider);
            mybooking_orderidtxt = itemView.findViewById(R.id.mybooking_orderidtxt);
            mybooking_nametxt = itemView.findViewById(R.id.mybooking_nametxt);
            mybooking_servicerequiredtxt = itemView.findViewById(R.id.mybooking_servicerequiredtxt);
            mybooking_subservicetxt = itemView.findViewById(R.id.mybooking_subservicetxt);
            mybooking_scheduleddatetxt = itemView.findViewById(R.id.mybooking_scheduledatetxt);


        }
    }
}
