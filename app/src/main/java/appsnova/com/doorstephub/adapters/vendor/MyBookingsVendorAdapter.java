package appsnova.com.doorstephub.adapters.vendor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.models.vendor.MyBookingsVendorModel;


public class MyBookingsVendorAdapter extends RecyclerView.Adapter<MyBookingsVendorAdapter.MyBookingsViewHolder> {
    Context context;
    ItemClickListener itemClickListener;
    List<MyBookingsVendorModel> myBookingsVendorModelList;


    public MyBookingsVendorAdapter(Context context, List<MyBookingsVendorModel> myBookingsVendorModelList, ItemClickListener itemClickListener) {
        this.itemClickListener=itemClickListener;
        this.context=context;
        this.myBookingsVendorModelList = myBookingsVendorModelList;
    }

    @NonNull
    @Override
    public MyBookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.my_bookings_listrow_vendor,parent,false);
        final MyBookingsViewHolder myBookingsViewHolder =new MyBookingsViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClickItem(v,myBookingsViewHolder.getAdapterPosition());
            }
        });


        return myBookingsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookingsViewHolder holder, int position) {
        MyBookingsVendorModel myBookingsVendorModel = myBookingsVendorModelList.get(position);

        holder.mybooking_orderidtxt.setText("Order ID:"+ myBookingsVendorModel.getOrderid());
        holder.mybooking_nametxt.setText("Name:"+ myBookingsVendorModel.getUsername());
        holder.mybooking_servicerequiredtxt.setText("Service Required:"+ myBookingsVendorModel.getServicerequired());
        holder.mybooking_subservicetxt.setText("Sub Service:"+ myBookingsVendorModel.getSubservice());
        holder.mybooking_scheduleddatetxt.setText("Scheduled On:"+ myBookingsVendorModel.getScheduleddate());
        holder.mybooking_status.setText(myBookingsVendorModel.getStatus());

        if(myBookingsVendorModel.getStatus().equalsIgnoreCase("completed")){
            holder.mybooking_status.setTextColor(Color.GREEN);
        }
        else if(myBookingsVendorModel.getStatus().equalsIgnoreCase("rejected")){
            holder.mybooking_status.setTextColor(Color.RED);
        }
        else
        {
            holder.mybooking_status.setTextColor(Color.BLUE);

        }

           /* for(int i=0;i<myBookingsVendorModelList.size();i++){
                if(position<myBookingsVendorModelList.size()-1){
                    holder.mybookings_horizontaldivider.setVisibility(View.GONE);
                }
            }*/

    }



    @Override
    public int getItemCount() {
        return myBookingsVendorModelList.size();
    }

    public class MyBookingsViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mybookings_rowlayout;
        View mybookings_horizontaldivider;
        TextView mybooking_orderidtxt,mybooking_nametxt,mybooking_servicerequiredtxt,mybooking_subservicetxt,
                mybooking_scheduleddatetxt,mybooking_status;
        public MyBookingsViewHolder(@NonNull View itemView) {
            super(itemView);

            mybookings_rowlayout = itemView.findViewById(R.id.mybookings_rowlayout);
            mybookings_horizontaldivider = itemView.findViewById(R.id.mybookings_horizontaldivider);
            mybooking_orderidtxt = itemView.findViewById(R.id.mybooking_orderidtxt);
            mybooking_nametxt = itemView.findViewById(R.id.mybooking_nametxt);
            mybooking_servicerequiredtxt = itemView.findViewById(R.id.mybooking_servicerequiredtxt);
            mybooking_subservicetxt = itemView.findViewById(R.id.mybooking_subservicetxt);
            mybooking_scheduleddatetxt = itemView.findViewById(R.id.mybooking_scheduledatetxt);
            mybooking_status  = itemView.findViewById(R.id.mybooking_status);

        }
    }
    public interface ItemClickListener {
        void onClickItem(View v, int pos);
    }
}
