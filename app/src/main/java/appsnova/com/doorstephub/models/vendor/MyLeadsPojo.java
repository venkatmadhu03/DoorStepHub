package appsnova.com.doorstephub.models.vendor;

public class MyLeadsPojo {
    String name,service,description;

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
