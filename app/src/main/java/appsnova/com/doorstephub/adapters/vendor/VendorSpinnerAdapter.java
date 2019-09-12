package appsnova.com.doorstephub.adapters.vendor;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.models.ServiceCategoryModel;
import appsnova.com.doorstephub.models.vendor.SpinnerPojoVendor;

public class VendorSpinnerAdapter extends RecyclerView.Adapter<VendorSpinnerAdapter.MyViewHolder>{

    List<ServiceCategoryModel> myDatalist;
    public VendorSpinnerAdapter(List<ServiceCategoryModel> spinnerPojo) {
        this.myDatalist = spinnerPojo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.spinner_row_vendor,viewGroup,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final ServiceCategoryModel myData = myDatalist.get(i);
        myViewHolder.spinner_checkbox.setText(myData.getName());
        myViewHolder.spinner_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             /*   List<CharSequence> modallist = new ArrayList<>();

                if(buttonView.isChecked()){
                    modallist.add( buttonView.getText());

                    for(CharSequence elements :  buttonView.getText())
                    {

                    }

                    Log.d("checked", String.valueOf(buttonView.getText()));
                    for (int i=0;i<modallist.size();i++){
                        Log.d("modalistsize", String.valueOf(modallist.size()+","+modallist.size()));
                        StringBuilder sb = new StringBuilder();
                        sb.append(i);
                        Log.d("sb", "onCheckedChanged: "+sb);

                    }

                }*/
             /*List<CharSequence> items = new ArrayList<CharSequence>();
            for(int i=0;i<myDatalist.size();i++) {
                if (buttonView.isChecked()) {
                    Log.d("checked", String.valueOf(buttonView.getText()));

                    items.add(buttonView.getText());
                    Log.d("modalistsize", String.valueOf(items.size()));
                }
            }
             for (CharSequence item : items)
              {

                  Log.d("item", "onCheckedChanged: "+item);
                  *//* if(item.isChecked())
                 {
                     String text = item.getText().toString();
                     Log.d("text", "onCheckedChanged: "+text);
                 }*//*

              }*/
                myData.setSelected(!myData.isSelected());
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
        return myDatalist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox spinner_checkbox;
        RelativeLayout spinner_layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            spinner_checkbox = itemView.findViewById(R.id.spinner_checkbox);
            spinner_layout = itemView.findViewById(R.id.spinnerlayout);
            /* spinner_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    List<String> modallist = new ArrayList<String>();

                   *//* int id = buttonView.getId();

                    switch (id)
                    {

                    }*//*
                   if( buttonView.isChecked())
                   {
                       modallist.add(String.valueOf(buttonView.getId()));
                   }


                    Log.d("checked",String.valueOf(buttonView.getId()));
                }
            });*/

        }
    }
}