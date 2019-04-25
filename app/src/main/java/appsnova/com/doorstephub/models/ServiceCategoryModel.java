package appsnova.com.doorstephub.models;

public class ServiceCategoryModel {

    String name;
    String id;
    String services_id;
    String service_image;

    public ServiceCategoryModel() {
    }

    public ServiceCategoryModel(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServices_id() {
        return services_id;
    }

    public void setServices_id(String services_id) {
        this.services_id = services_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService_image() {
        return service_image;
    }

    public void setService_image(String service_image) {
        this.service_image = service_image;
    }
}
