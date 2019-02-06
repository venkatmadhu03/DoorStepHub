package appsnova.com.doorstephub.models;

public class ServiceSelectionModel {
    String name;
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
