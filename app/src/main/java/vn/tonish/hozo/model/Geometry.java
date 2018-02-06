package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by CanTran on 2/5/18.
 */

public class Geometry implements Serializable {
    private Location location;
    @SerializedName("location_type")
    private String type;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Geometry{" +
                "location=" + location +
                ", type='" + type + '\'' +
                '}';
    }
}
