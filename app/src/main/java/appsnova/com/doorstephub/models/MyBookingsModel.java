package appsnova.com.doorstephub.models;

public class MyBookingsModel {

    String orderid;
    String username;
    String service_description;
    String scheduleddate;
    String status;
    String selectedService;
    String selectedSubService;

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    String appointment_id;

    public String getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(String selectedService) {
        this.selectedService = selectedService;
    }

    public String getSelectedSubService() {
        return selectedSubService;
    }

    public void setSelectedSubService(String selectedSubService) {
        this.selectedSubService = selectedSubService;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MyBookingsModel() {
    }

    public MyBookingsModel(String orderid, String username, String service_description, String scheduleddate, String status) {
        this.orderid = orderid;
        this.username = username;
        this.service_description = service_description;
        this.scheduleddate = scheduleddate;
        this.status = status;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getService_description() {
        return service_description;
    }

    public void setService_description(String service_description) {
        this.service_description = service_description;
    }

    public String getScheduleddate() {
        return scheduleddate;
    }

    public void setScheduleddate(String scheduleddate) {
        this.scheduleddate = scheduleddate;
    }
}
