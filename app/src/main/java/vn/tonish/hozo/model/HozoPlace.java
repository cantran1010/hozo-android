package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by CanTran on 2/5/18.
 */

public class HozoPlace implements Serializable {
    @SerializedName("place_id")
    private String id;
    @SerializedName("formatted_address")
    private String address;
    @SerializedName("geometry")
    private Geometry geometry;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public String toString() {
        return "HozoPlace{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", geometry=" + geometry +
                '}';
    }
}
