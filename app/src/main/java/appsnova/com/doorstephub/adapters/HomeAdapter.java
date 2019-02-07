package appsnova.com.doorstephub.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.ServiceSelectionActivity;
import appsnova.com.doorstephub.models.ServiceCategoryModel;
import appsnova.com.doorstephub.ownlibraries.MyTextView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    List<ServiceCategoryModel> serviceCategoryModelList;
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.services_categories_list_row, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    public HomeAdapter(List<ServiceCategoryModel> serviceCategoryModelList, Context context) {
        this.serviceCategoryModelList = serviceCategoryModelList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ServiceCategoryModel myData = serviceCategoryModelList.get(i);
        myViewHolder.recycler_text.setText(myData.getName());


        Random r = new Random();
        int red = r.nextInt(255 - 0 + 1) + 0;
        int green = r.nextInt(255 - 0 + 1) + 0;
        int blue = r.nextInt(255 - 0 + 1) + 0;

        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.RECTANGLE);
        draw.setColor(Color.rgb(red, green, blue));
        myViewHolder.recycler_text.setBackground(draw);
        myViewHolder.recycler_text.setTextColor(Color.WHITE);


       /* if(i %2 == 1)
        {
            myViewHolder.recycler_text.setBackgroundColor(Color.parseColor("#000000"));
            myViewHolder.recycler_text.setTextColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            myViewHolder.recycler_text.setBackgroundColor(Color.parseColor("#1849AA"));
            myViewHolder.recycler_text.setTextColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }*/
        myViewHolder.recycler_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ServiceSelectionActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceCategoryModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MyTextView recycler_text;
        LinearLayout recycler_container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recycler_text = itemView.findViewById(R.id.recycler_text);
            recycler_container = itemView.findViewById(R.id.recycler_container);
        }
    }
}