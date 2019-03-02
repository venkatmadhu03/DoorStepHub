package appsnova.com.doorstephub.models;

public class ServiceSelectionModel {
    String name;
    String id;
    String service_selection_image;

    public String getService_selection_image() {
        return service_selection_image;
    }

    public void setService_selection_image(String service_selection_image) {
        this.service_selection_image = service_selection_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceSelectionModel() {
    }

    String title;
    String description;
    private boolean isSelected = false;
    public ServiceSelectionModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;

    }


    public boolean isSelected() {
        return isSelected;
    }
}
