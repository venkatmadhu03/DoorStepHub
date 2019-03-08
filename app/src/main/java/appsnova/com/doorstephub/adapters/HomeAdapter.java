package appsnova.com.doorstephub.adapters;

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
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.activities.ServiceSelectionActivity;
import appsnova.com.doorstephub.models.ServiceCategoryModel;
import appsnova.com.doorstephub.ownlibraries.MyTextView;

import static java.security.AccessController.getContext;

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
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder,final int i) {
        ServiceCategoryModel myData = serviceCategoryModelList.get(i);
        myViewHolder.recycler_text.setText(myData.getName());
        Picasso.get().load(myData.getService_image()).
                placeholder(R.drawable.placeholder).error(R.drawable.error).into(myViewHolder.text_logo);


       /* Random r = new Random();
        int red = r.nextInt(255 - 0 + 1) + 0;
        int green = r.nextInt(255 - 0 + 1) + 0;
        int blue = r.nextInt(255 - 0 + 1) + 0;

        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.RECTANGLE);
        draw.setColor(Color.rgb(red, green, blue));
        myViewHolder.recycler_text.setBackground(draw);*/
        myViewHolder.recycler_text.setTextColor(Color.BLACK);

        myViewHolder.recycler_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, ServiceSelectionActivity.class);
                intent.putExtra("service_id",serviceCategoryModelList.get(i).getId());
                intent.putExtra("service_name",serviceCategoryModelList.get(i).getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d("serviceId", "onClick: "+serviceCategoryModelList.get(i).getId()+","+serviceCategoryModelList.get(i).getName());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Explode explode = new Explode();
                    explode.setDuration(600);
                    explode.setInterpolator(new AccelerateDecelerateInterpolator());
                    ((Activity) context).getWindow().setEnterTransition(explode);
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(((Activity) context));
                    context.startActivity(intent,activityOptions.toBundle());
                }
//                context.startActivity(new Intent(context, ServiceSelectionActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceCategoryModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MyTextView recycler_text;
        RelativeLayout recycler_container;
        ImageView text_logo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recycler_text = itemView.findViewById(R.id.recycler_text);
            recycler_container = itemView.findViewById(R.id.recycler_container);
            text_logo = itemView.findViewById(R.id.text_logo);
        }
    }
}