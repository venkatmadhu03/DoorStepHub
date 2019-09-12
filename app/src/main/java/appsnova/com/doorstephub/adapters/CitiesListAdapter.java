package appsnova.com.doorstephub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.adapters.vendor.VendorSpinnerAdapter;
import appsnova.com.doorstephub.models.ServiceCategoryModel;
import appsnova.com.doorstephub.models.vendor.SpinnerPojoVendor;

public class CitiesListAdapter extends RecyclerView.Adapter<CitiesListAdapter.MyViewHolder> {

    List<SpinnerPojoVendor> spinnerPojoVendorList;
    Context context;

    public CitiesListAdapter(List<SpinnerPojoVendor> spinnerPojoVendorList, Context context) {
        this.spinnerPojoVendorList = spinnerPojoVendorList;
        this.context = context;
    }

    @NonNull
    @Override
    public CitiesListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.spinner_row_vendor,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesListAdapter.MyViewHolder holder, int position) {
        final SpinnerPojoVendor spinnerPojoVendor = spinnerPojoVendorList.get(position);
        holder.spinner_checkbox.setText(spinnerPojoVendor.getLocationName());
        holder.spinner_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spinnerPojoVendor.setSelected(!spinnerPojoVendor.isSelected());
            }
        });
       /* myViewHolder.spinner_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return spinnerPojoVendorList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox spinner_checkbox;
        RelativeLayout spinner_layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            spinner_checkbox = itemView.findViewById(R.id.spinner_checkbox);
            spinner_layout = itemView.findViewById(R.id.spinnerlayout);
        }
    }
}
