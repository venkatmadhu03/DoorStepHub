package appsnova.com.doorstephub.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.models.MyBookingsModel;

public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.MyBookingsViewHolder> implements Filterable {
    List<MyBookingsModel> myBookingsModelList;
    List<MyBookingsModel> mybookingsfilteredlist;
    ItemClickListener itemClickListener;
    Context context;


    public MyBookingsAdapter(Context context,List<MyBookingsModel> myBookingsModelList,ItemClickListener itemClickListener) {
        this.myBookingsModelList = myBookingsModelList;
        this.itemClickListener = itemClickListener;
        this.mybookingsfilteredlist = myBookingsModelList;
        this.context = context;

    }

    @NonNull
    @Override
    public MyBookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.mybooking_list_row,parent,false);
        final MyBookingsViewHolder myBookingsViewHolder =new MyBookingsViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClickItem(v,myBookingsViewHolder.getAdapterPosition());
            }
        });
        return myBookingsViewHolder;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyBookingsViewHolder holder, int position) {
        MyBookingsModel myBookingsModel = mybookingsfilteredlist.get(position);

        holder.mybooking_orderidtxt.setText(myBookingsModel.getAppointment_id());
        holder.mybooking_service_description.setText(myBookingsModel.getService_description());
       // holder.mybooking_SelectedService.setText("Service:"+myBookingsModel.getSelectedService());
        holder.mybooking_SelectedSubService.setText(myBookingsModel.getSelectedSubService());
        holder.mybooking_scheduleddatetxt.setText(myBookingsModel.getScheduleddate());

        holder.mybooking_status.setText(myBookingsModel.getStatus());

        if(myBookingsModel.getStatus().equalsIgnoreCase("Open")){
            holder.mybooking_status.setTextColor(context.getColor(android.R.color.holo_green_light));
            holder.bookings_view_id.setBackgroundColor(context.getColor(android.R.color.holo_green_light));
        }
        else if(myBookingsModel.getStatus().equalsIgnoreCase("Rejected")){
            holder.mybooking_status.setTextColor(context.getColor(android.R.color.holo_red_dark));
            holder.bookings_view_id.setBackgroundColor(context.getColor(android.R.color.holo_red_dark));
        }
        else if(myBookingsModel.getStatus().equalsIgnoreCase("Close")){
            holder.mybooking_status.setTextColor(context.getColor(android.R.color.holo_orange_dark));
            holder.bookings_view_id.setBackgroundColor(context.getColor(android.R.color.holo_orange_dark));

        }
        else
        {
            holder.mybooking_status.setTextColor(context.getColor(android.R.color.holo_blue_bright));
            holder.bookings_view_id.setBackgroundColor(context.getColor(android.R.color.holo_blue_bright));
        }

           /* for(int i=0;i<myBookingsModelList.size();i++){
                if(position<myBookingsModelList.size()-1){
                    holder.mybookings_horizontaldivider.setVisibility(View.GONE);
                }
            }*/

    }

    @Override
    public int getItemCount() {
        return mybookingsfilteredlist.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String charString = constraint.toString();

                List<MyBookingsModel> filteredList = new ArrayList<>();

                for (MyBookingsModel row : myBookingsModelList) {

                    if(row.getOrderid()!=null){

                        if (row.getSelectedService().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getSelectedSubService().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getOrderid().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }

                    }
                }
                mybookingsfilteredlist = filteredList;
                FilterResults filterResults = new FilterResults();
                filterResults.values = mybookingsfilteredlist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mybookingsfilteredlist = (List<MyBookingsModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyBookingsViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mybookings_rowlayout;
        TextView mybooking_orderidtxt,mybooking_service_description,mybooking_SelectedService,
                mybooking_SelectedSubService,
                mybooking_scheduleddatetxt,mybooking_status;
        View bookings_view_id;
        public MyBookingsViewHolder(@NonNull View itemView) {
            super(itemView);

            mybookings_rowlayout = itemView.findViewById(R.id.mybookings_rowlayout);
            mybooking_orderidtxt = itemView.findViewById(R.id.mybooking_orderidtxt);
            mybooking_service_description = itemView.findViewById(R.id.mybooking_service_description);
           // mybooking_SelectedService = itemView.findViewById(R.id.mybooking_SelectedService);
            mybooking_SelectedSubService = itemView.findViewById(R.id.mybooking_SelectedSubService);
            mybooking_scheduleddatetxt = itemView.findViewById(R.id.mybooking_scheduledatetxt);
            mybooking_status = itemView.findViewById(R.id.mybooking_status);
            bookings_view_id = itemView.findViewById(R.id.bookings_view_id);
        }
    }
    public interface ItemClickListener {
        void onClickItem(View v,int pos);
    }
}
