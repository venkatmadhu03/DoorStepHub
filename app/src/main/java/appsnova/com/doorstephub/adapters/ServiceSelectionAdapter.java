package appsnova.com.doorstephub.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.SubServiceSelectionActivity;
import appsnova.com.doorstephub.models.ServiceSelectionModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class ServiceSelectionAdapter extends RecyclerView.Adapter<ServiceSelectionAdapter.ServiceSelectionViewHolder> {
    Context context;
    public List<ServiceSelectionModel> serviceSelectionModelList;
    String service_id, service_name;


    public ServiceSelectionAdapter(Context context, List<ServiceSelectionModel> serviceSelectionModelList,
                                   String service_id, String service_name) {
        this.serviceSelectionModelList = serviceSelectionModelList;
        this.context = context;
        this.service_id = service_id;
        this.service_name = service_name;
    }

    @NonNull
    @Override
    public ServiceSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.service_selection_list_row, viewGroup, false);
        return new ServiceSelectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceSelectionViewHolder holder, final int i) {

        final ServiceSelectionModel serviceSelectionModel = serviceSelectionModelList.get(i);
        holder.servicetext.setText(serviceSelectionModel.getName());
        Picasso.get().load(serviceSelectionModel.getService_selection_image()).
                placeholder(R.drawable.dhub_placeholder).error(R.drawable.error).into(holder.service_background);

//        if(selectedItemsList.size()>0){
//            if(selectedItemsList.contains(serviceSelectionModel)){
//
//                holder.selected_item.setVisibility(View.VISIBLE);
//
//            }else{
//                holder.selected_item.setVisibility(View.GONE);
//            }
//
//        }
//        if(selectedItemsList.size()==0){
//            holder.selected_item.setVisibility(View.GONE);
//        }

        holder.servicerelativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SubServiceSelectionActivity.class);
                intent.putExtra("sub_service_id", serviceSelectionModelList.get(i).getId());
                intent.putExtra("Service_Id", service_id);
                intent.putExtra("Service_Name", service_name);
                intent.putExtra("sub_service_name", serviceSelectionModelList.get(i).getName());

                context.startActivity(intent);
            }

        });

    }

    @Override
    public int getItemCount() {
        return serviceSelectionModelList.size();
    }


    public class ServiceSelectionViewHolder extends RecyclerView.ViewHolder {
       TextView servicetext;
        CardView service_item;
        ImageView selected_item;
        CircleImageView service_background;
        // CheckBox servicerow_checkbox;

        RelativeLayout servicerelativelayout;

        public ServiceSelectionViewHolder(@NonNull final View itemView) {
            super(itemView);
            servicerelativelayout = itemView.findViewById(R.id.container);
            servicetext = itemView.findViewById(R.id.problemTextView);
            service_background = itemView.findViewById(R.id.image_background);
            //selected_item = itemView.findViewById(R.id.selected_item);
            service_item=itemView.findViewById(R.id.service_item);

        }
    }

    /*public  List<ServiceSelectionModel> getSelectedItem(){

        for (int i =0; i < serviceSelectionModelList.size(); i++){
            ServiceSelectionModel itemModel = serviceSelectionModelList.get(i);
            if (itemModel.isSelected()){
                Log.d("ServiceSelection", "getSelectedItem: "+itemModel.getId());
                selectedItemsList.add(itemModel);
            }
        }
        return selectedItemsList;
    }

    public int getSelectedItemCount(){
        return selectedItemsList.size();
    }*/

}