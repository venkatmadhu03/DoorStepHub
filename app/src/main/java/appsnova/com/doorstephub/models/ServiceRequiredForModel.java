package appsnova.com.doorstephub.models;

public class ServiceRequiredForModel {

    String name;
    String id;
    String service_required_for_id;

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    String isChecked;

    public ServiceRequiredForModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService_required_for_id() {
        return service_required_for_id;
    }

    public void setService_required_for_id(String service_required_for_id) {
        this.service_required_for_id = service_required_for_id;
    }
}
