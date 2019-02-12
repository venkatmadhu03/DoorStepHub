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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.models.ServiceSelectionModel;

public class ServiceSelectionAdapter   extends RecyclerView.Adapter<ServiceSelectionAdapter.ServiceSelectionViewHolder> {
    //private int previousSelectedPosition = -1;
    Context context;
    public List<ServiceSelectionModel> serviceSelectionModelList;
    public List<ServiceSelectionModel> selectedItemsList;

    @NonNull
    @Override
    public ServiceSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.service_selection_list_row, viewGroup, false);
        final ServiceSelectionViewHolder holder = new ServiceSelectionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceSelectionViewHolder holder, final int i) {

        final ServiceSelectionModel serviceSelectionModel = serviceSelectionModelList.get(i);
        holder.servicetext.setText(serviceSelectionModel.getName());
        /*holder.servicerelativelayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    //  holder.itemView.setSelected(selectedPos == i);
                    serviceSelectionModel.setSelected(!serviceSelectionModel.isSelected());
                  //  holder.service_background.setBackgroundResource(R.drawable.tick_mark);
                    holder.selected_item.setVisibility(View.VISIBLE);


                   *//* int oldposition = holder.getOldPosition();

                    if(oldposition == i)
                    {
                        holder.selected_item.setVisibility(View.GONE);
                    }
*//*

                    return true;
                }
            });*/

        Log.d("selecteditemlistsize", String.valueOf(selectedItemsList.size()));
        if(selectedItemsList.size()>0){
            if(selectedItemsList.contains(serviceSelectionModel)){

                holder.selected_item.setVisibility(View.VISIBLE);
                //holder.servicetext.setVisibility(View.VISIBLE);

            }else{
                holder.selected_item.setVisibility(View.GONE);
//                    holder.icon_back.setVisibility(View.GONE);

            }

        }


        holder.servicerelativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


/*
                if(previousSelectedPosition == i) {
                 //   previousSelectedPosition = RecyclerView.NO_POSITION;
                    holder.selected_item.setVisibility(View.GONE);
                    notifyDataSetChanged();

                }
                previousSelectedPosition = i;*/

                /*serviceSelectionModel.setSelected(!serviceSelectionModel.isSelected());
                holder.selected_item.setVisibility(View.VISIBLE);*/
                // ImageView previousSelectedView = (ImageView) holder.servicerelativelayout.getChildAt(previousSelectedPosition);


                //previousSelectedPosition = i;

            }


        });


       /* holder.servicerow_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                serviceSelectionModel.setSelected(!serviceSelectionModel.isSelected());
            }
        });*/

    }

    public ServiceSelectionAdapter(Context context, List<ServiceSelectionModel> serviceSelectionModelList,List<ServiceSelectionModel> selectedItemList) {
        this.serviceSelectionModelList = serviceSelectionModelList;
        this.context = context;
        this.selectedItemsList = selectedItemList;
    }

    @Override
    public int getItemCount() {
        return serviceSelectionModelList.size();
    }


    public class ServiceSelectionViewHolder extends RecyclerView.ViewHolder {
        TextView servicetext;
        ImageView service_background, selected_item;
        // CheckBox servicerow_checkbox;

        RelativeLayout servicerelativelayout;

        public ServiceSelectionViewHolder(@NonNull final View itemView) {
            super(itemView);
            servicerelativelayout = itemView.findViewById(R.id.container);
            servicetext = itemView.findViewById(R.id.servicerow_text);
            service_background = itemView.findViewById(R.id.image_background);
            selected_item = itemView.findViewById(R.id.selected_item);
            // servicerow_checkbox = itemView.findViewById(R.id.service_row_checkbox);
        }
    }

    public  List<ServiceSelectionModel> getSelectedItem(){

        for (int i =0; i < serviceSelectionModelList.size(); i++){
            ServiceSelectionModel itemModel = serviceSelectionModelList.get(i);
            if (itemModel.isSelected()){
                selectedItemsList.add(itemModel);
            }
        }
        return selectedItemsList;
    }

    public int getSelectedItemCount(){

        return selectedItemsList.size();
    }

}