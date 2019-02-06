package appsnova.com.doorstephub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.models.ServiceSelectionModel;

public class ServiceSelectionAdapter   extends RecyclerView.Adapter<ServiceSelectionAdapter.ServiceSelectionViewHolder> {
    List<ServiceSelectionModel> serviceSelectionModelList;
    @NonNull
    @Override
    public ServiceSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.service_selection_list_row,viewGroup,false);
        ServiceSelectionViewHolder holder = new ServiceSelectionViewHolder(view);

        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull ServiceSelectionViewHolder holder, int i) {

        final ServiceSelectionModel serviceSelectionModel = serviceSelectionModelList.get(i);
        holder.servicetext.setText(serviceSelectionModel.getName());
        holder.servicerow_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                serviceSelectionModel.setSelected(!serviceSelectionModel.isSelected());
            }
        });



    }

    public ServiceSelectionAdapter(List<ServiceSelectionModel> serviceSelectionModelList) {
        this.serviceSelectionModelList = serviceSelectionModelList;
    }

    @Override
    public int getItemCount() {
        return serviceSelectionModelList.size();
    }


    public class ServiceSelectionViewHolder extends  RecyclerView.ViewHolder {
        TextView servicetext;
        CheckBox servicerow_checkbox;
        RelativeLayout servicerelativelayout;

        public ServiceSelectionViewHolder(@NonNull View itemView) {
            super(itemView);
            servicerelativelayout = itemView.findViewById(R.id.container);
            servicetext =itemView.findViewById(R.id.servicerow_text);
            servicerow_checkbox = itemView.findViewById(R.id.service_row_checkbox);
        }
    }
}