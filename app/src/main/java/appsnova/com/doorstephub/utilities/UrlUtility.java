package appsnova.com.doorstephub.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;

import appsnova.com.doorstephub.R;

public class UrlUtility {
    public  static final String BASE_URL="https://www.doorstephub.com/api/index.php/";
//    public  static final String BASE_URL="http://10.10.10.142/api/index.php/";
   // public  static final String BASE_URL="http://192.168.43.77/api/index.php/";
    public static final String SERVICES_LIST_URL=BASE_URL+"Services/ServicesList";
    public static final String SUB_SERVICES_LIST_URL=BASE_URL+"Services/SubServicesList";
    public static final String SUBMIT_DETAILS_URL=BASE_URL+"Services/SubServicesList";
    public static final String CREATE_REQUEST_URL=BASE_URL+"Bookings/CreateRequest";
    public static final String GET_BOOKINGS_URL=BASE_URL+"Bookings/GetBookings";
    public static final String GET_FEEDBACK_URL=BASE_URL+"Bookings/GetFeedback";
    public static final String FEEDBACK_URL=BASE_URL+"Bookings/Feedback";
    public static final String LOGIN_URL =BASE_URL+"User/Login";
    public static final String VERIFY_OTP_URL =BASE_URL+"User/VerifyOTP";
    public  static  final String GET_PROFILE_URL = BASE_URL+"User/UserProfile";
    public  static  final String UPDATE_PROFILE_URL = BASE_URL+"User/UpdateProfile";
    //VENDOR API'S
    public  static final String VENDOR_LOGIN_URL = BASE_URL+"User/VendorLogin";
    public static  final String VENDOR_GETPROFILE_URL = BASE_URL+"User/VendorGetProfile";
    public static  final String VENDOR_UPDATEPROFILE_URL = BASE_URL+"User/VendorUpdateProfile";
    public static final String VENDOR_GETBOOKINGS_URL = BASE_URL+"Bookings/GetVendorBookings";
    public static final String UPDATE_VENDORBOOKINGS_URL = BASE_URL+"Bookings/UpdateVendorBookings";
    public static final String VENDOR_CANCELLED_REASONS_URL =BASE_URL+"Bookings/CancelledReasons";
    public static final String VENDOR_LATEST_BOOKINGS_URL =BASE_URL+"Bookings/VendorLatestBooking";
    public static final String VENDOR_BOOKINGS_DEDUCTBALANCE_URL =BASE_URL+"Bookings/DeductBalance";

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

    public  static  Dialog showCustomDialog(Context mcontext){
        Dialog customDialog = new Dialog(mcontext);
        View view = LayoutInflater.from(mcontext).inflate(R.layout.custom_progress_dialog,null);
        customDialog.setContentView(view,new LinearLayout.LayoutParams(300,300));
        final ProgressBar progressBar = customDialog.findViewById(R.id.progress_bar);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final float growTo = 1.2f;
        final long duration = 400;

        ScaleAnimation grow = new ScaleAnimation(1, growTo, 1, growTo,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        grow.setDuration(duration / 2);
        grow.setRepeatMode(Animation.REVERSE);
        grow.setRepeatCount(Animation.INFINITE);


        ScaleAnimation shrink = new ScaleAnimation(growTo, 1, growTo, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(duration / 2);
        shrink.setStartOffset(duration / 2);
        AnimationSet growAndShrink = new AnimationSet(true);
        growAndShrink.setInterpolator(new LinearInterpolator());
        growAndShrink.addAnimation(grow);
        growAndShrink.addAnimation(shrink);
        progressBar.startAnimation(growAndShrink);

        return customDialog;
    }

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
