package appsnova.com.doorstephub.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import appsnova.com.doorstephub.R;

public class UrlUtility {

    public  static final String BASE_URL="https://doorstephub.com/api/index.php/";
//    public  static final String BASE_URL="http://10.10.10.212/CodeIgniter/index.php/";
    public static final String SERVICES_LIST_URL=BASE_URL+"Services/ServicesList";
    public static final String SUB_SERVICES_LIST_URL=BASE_URL+"Services/SubServicesList";
    public static final String SUBMIT_DETAILS_URL=BASE_URL+"Services/SubServicesList";
    public static final String CREATE_REQUEST_URL=BASE_URL+"Bookings/CreateRequest";

    public static ProgressDialog showProgressDialog(Context mContext) {

        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(mContext.getResources().getString(R.string.progress_message));
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }// end of Progress dialog


    /**
     * Obtains the LayoutInflater from the given context.
     */
    public static LayoutInflater fromContext(Context context) {
        LayoutInflater layoutInflater = null;
        try {
            if (context != null) {
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            if (layoutInflater == null) {
                throw new AssertionError("LayoutInflater not found.");
            }
        } catch (Exception e) {
            if (e != null) {
                Log.w("LayoutInflater", e);
            }
            layoutInflater = null;
        }
        return layoutInflater;
    }


    public static void showCustomToast(String toastMessage, Activity activity){
        try {
            LayoutInflater inflater = UrlUtility.fromContext(activity);
            View layout = null;
            if (inflater != null) {
                layout = inflater.inflate(R.layout.no_network_toast, (ViewGroup) activity.findViewById(R.id.custom_toast_layout_id));
                TextView tv = layout.findViewById(R.id.toastText);
                // The actual toast generated here.
                Toast toast = new Toast(activity);
                tv.setText(toastMessage);
                toast.setDuration(Toast.LENGTH_SHORT);
                //tv.setTypeface(Utility.font_reg);
                if (layout != null) {
                    toast.setView(layout);
                    toast.show();
                }
            } else {
                Toast.makeText(activity, "" + toastMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (AssertionError e) {
            if (e != null) {
                Log.w("customToast", e);
                Toast.makeText(activity, "" + toastMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            if (e != null) {
                Log.w("customToast", e);
                Toast.makeText(activity, "" + toastMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
