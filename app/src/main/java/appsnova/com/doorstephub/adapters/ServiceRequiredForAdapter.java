package appsnova.com.doorstephub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.models.ServiceRequiredForModel;

public class ServiceRequiredForAdapter extends RecyclerView.Adapter<ServiceRequiredForAdapter.ServiceRequiredViewholder> {

    List<ServiceRequiredForModel> serviceRequiredForModelList;
    Context context;
    ItemClickListener itemClickListener;

    RadioGroup lastCheckedRadioGroup;

    public ServiceRequiredForAdapter(List<ServiceRequiredForModel> serviceRequiredForModelList, Context context, ServiceRequiredForAdapter.ItemClickListener itemClickListener) {
        this.serviceRequiredForModelList = serviceRequiredForModelList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ServiceRequiredViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_required_for_list, null);
        return new ServiceRequiredViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceRequiredViewholder holder, final int position) {

        int id = (position+1)*100;
        final RadioButton rb = new RadioButton(ServiceRequiredForAdapter.this.context);
        rb.setId(id);
        rb.setText(serviceRequiredForModelList.get(position).getName());
        holder.service_required_for_radio_button.addView(rb);

    }

    @Override
    public int getItemCount() {
        return serviceRequiredForModelList.size();
    }

    public class ServiceRequiredViewholder extends RecyclerView.ViewHolder {
        RadioGroup service_required_for_radio_button;


        public ServiceRequiredViewholder(@NonNull View itemView) {
            super(itemView);
            service_required_for_radio_button = itemView.findViewById(R.id.service_required_for_radio_button);

            service_required_for_radio_button.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (lastCheckedRadioGroup != null
                            && lastCheckedRadioGroup.getCheckedRadioButtonId()
                            != service_required_for_radio_button.getCheckedRadioButtonId()
                            && lastCheckedRadioGroup.getCheckedRadioButtonId() != -1) {
                        lastCheckedRadioGroup.clearCheck();

//                        Toast.makeText(context,
//                                "Radio button clicked " + serviceRequiredForModelList.get(getAdapterPosition()),
//                                Toast.LENGTH_SHORT).show();

                        itemClickListener.onClickItem(serviceRequiredForModelList.get(getAdapterPosition()));

                    }else{
                        itemClickListener.onClickItem(serviceRequiredForModelList.get(getAdapterPosition()));
                    }
                    lastCheckedRadioGroup = service_required_for_radio_button;
                }
            });

        }
    }

    public interface ItemClickListener {
        void onClickItem(ServiceRequiredForModel serviceRequiredForModel);
    }
}
