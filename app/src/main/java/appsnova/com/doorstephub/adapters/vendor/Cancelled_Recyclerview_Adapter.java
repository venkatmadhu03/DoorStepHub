package appsnova.com.doorstephub.adapters.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appsnova.com.doorstephub.R;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;
import appsnova.com.doorstephub.utilities.SharedPref;
import appsnova.com.doorstephub.utilities.UrlUtility;
import appsnova.com.doorstephub.utilities.VolleySingleton;

public class Cancelled_Recyclerview_Adapter  extends RecyclerView.Adapter<Cancelled_Recyclerview_Adapter.MyViewHolder> {
    Context mcontext;
    List<MyLeadsPojo> myLeadsPojoList;
    SharedPref sharedPref;
    ProgressDialog progressDialog;
    int statusCode;
    String statusMessage;

    public Cancelled_Recyclerview_Adapter(List<MyLeadsPojo> myLeadsPojoList, Context mcontext) {
        this.myLeadsPojoList = myLeadsPojoList;
        this.mcontext = mcontext;
        progressDialog = UrlUtility.showProgressDialog(mcontext);
        sharedPref = new SharedPref(mcontext);
    }

    @NonNull
    @Override
    public Cancelled_Recyclerview_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.cancelled_fragment_list_row,viewGroup,false);
        return new Cancelled_Recyclerview_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Cancelled_Recyclerview_Adapter.MyViewHolder myViewHolder, final int pos) {
        final MyLeadsPojo myLeadsPojo = myLeadsPojoList.get(pos);
        Log.d("Acceptedlistsize", "onBindViewHolder: " + myLeadsPojoList.size());
        Log.d("AcceptedlistName", "onBindViewHolder: " + myLeadsPojo.getName());
        myViewHolder.textView_name.setText("Name:" + myLeadsPojo.getName());
        myViewHolder.textView_city.setText("Service:" + myLeadsPojo.getService());
        myViewHolder.textView_description.setText("Description:" + myLeadsPojo.getDescription());
        myViewHolder.cancellation_reasonTV.setText("Cancellation_Reason:" + myLeadsPojo.getCancelled_reason());
    }



    @Override
    public int getItemCount() {
        return myLeadsPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_name,textView_city,textView_description,cancellation_reasonTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.cancelled_textview_name);
            textView_city  = itemView.findViewById(R.id.cancelled_textview_city);
            textView_description = itemView.findViewById(R.id.cancelled_TV_description);
            cancellation_reasonTV = itemView.findViewById(R.id.cancelledReasonTV);
        }
    }
}
