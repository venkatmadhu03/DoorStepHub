package appsnova.com.doorstephub.adapters.vendor;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.ServiceSelectionActivity;
import appsnova.com.doorstephub.models.vendor.CategoryListPOJO;

public class CategoryListRecyclerViewAdapter extends RecyclerView.Adapter<CategoryListRecyclerViewAdapter.CategoryViewHolder> {
    Context mcontext;
    List<CategoryListPOJO> categoryList;

    public CategoryListRecyclerViewAdapter(Context mcontext, List<CategoryListPOJO> categoryList) {
        this.mcontext = mcontext;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryListRecyclerViewAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vendor_category_listrow,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListRecyclerViewAdapter.CategoryViewHolder holder, final int position) {
        CategoryListPOJO categoryListPOJO = categoryList.get(position);
        holder.categoryname.setText(categoryListPOJO.getCategory_name());
        holder.category_image.setImageResource(categoryListPOJO.getCategory_image());

        holder.categoryname.setTextColor(Color.BLACK);

        holder.recycler_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mcontext, ServiceSelectionActivity.class);
                intent.putExtra("service_name",categoryList.get(position).getCategory_name());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Explode explode = new Explode();
                    explode.setDuration(600);
                    explode.setInterpolator(new AccelerateDecelerateInterpolator());
                    ((Activity) mcontext).getWindow().setEnterTransition(explode);
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(((Activity) mcontext));
                    mcontext.startActivity(intent,activityOptions.toBundle());
                }
//                context.startActivity(new Intent(context, ServiceSelectionActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryname;
        ImageView category_image;
        RelativeLayout recycler_container;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryname  = itemView.findViewById(R.id.vendor_category_name);
            category_image = itemView.findViewById(R.id.vendor_categoryimage);
            recycler_container = itemView.findViewById(R.id.vendor_recycler_container);
        }
    }
}
