package appsnova.com.doorstephub.models.vendor;


public class SpinnerPojoVendor {
    String locationName="", id="";
    private boolean isSelected = false;

    public SpinnerPojoVendor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
}
