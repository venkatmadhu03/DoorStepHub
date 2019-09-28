package appsnova.com.doorstephub.models;

public class ProblemsModel {
    String name;
    String id;
    String sub_service_id;

    public ProblemsModel() {
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

    public String getSub_service_id() {
        return sub_service_id;
    }

    public void setSub_service_id(String sub_service_id) {
        this.sub_service_id = sub_service_id;
    }
}
