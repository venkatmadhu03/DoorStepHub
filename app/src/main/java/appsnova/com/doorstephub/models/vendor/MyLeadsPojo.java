package appsnova.com.doorstephub.models.vendor;

public class MyLeadsPojo {
    String name="",service="",description="",cancelled_reason="",phone_number="";
    String status_name="", booking_id="";

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCancelled_reason() {
        return cancelled_reason;
    }

    public void setCancelled_reason(String cancelled_reason) {
        this.cancelled_reason = cancelled_reason;
    }

    public MyLeadsPojo(String name, String service, String description) {
        this.name = name;
        this.service = service;
        this.description = description;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public MyLeadsPojo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
