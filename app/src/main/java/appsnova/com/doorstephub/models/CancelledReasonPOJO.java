package appsnova.com.doorstephub.models;

import androidx.annotation.NonNull;

public class CancelledReasonPOJO {
    String id,enquiry_update_name,reason,status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnquiry_update_name() {
        return enquiry_update_name;
    }

    public void setEnquiry_update_name(String enquiry_update_name) {
        this.enquiry_update_name = enquiry_update_name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {

        return getReason();
    }
}
