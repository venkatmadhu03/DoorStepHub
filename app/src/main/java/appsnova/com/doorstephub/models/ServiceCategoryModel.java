package appsnova.com.doorstephub.models;

public class ServiceCategoryModel {

    String name,id,services_id;

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

    public ServiceCategoryModel() {
    }

    public ServiceCategoryModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
