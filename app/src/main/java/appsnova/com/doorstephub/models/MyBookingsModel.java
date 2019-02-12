package appsnova.com.doorstephub.models;

public class MyBookingsModel {

    String orderid,username,servicerequired,subservice,scheduleddate,status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MyBookingsModel(String orderid, String username, String servicerequired, String subservice, String scheduleddate, String status) {
        this.orderid = orderid;
        this.username = username;
        this.servicerequired = servicerequired;
        this.subservice = subservice;
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

    public String getServicerequired() {
        return servicerequired;
    }

    public void setServicerequired(String servicerequired) {
        this.servicerequired = servicerequired;
    }

    public String getSubservice() {
        return subservice;
    }

    public void setSubservice(String subservice) {
        this.subservice = subservice;
    }

    public String getScheduleddate() {
        return scheduleddate;
    }

    public void setScheduleddate(String scheduleddate) {
        this.scheduleddate = scheduleddate;
    }
}
