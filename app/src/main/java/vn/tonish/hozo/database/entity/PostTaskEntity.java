package vn.tonish.hozo.database.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by CanTran on 10/26/17.
 */

public class PostTaskEntity extends RealmObject {
    @PrimaryKey
    private int id;
    private double lat;
    private double lon;
    private String address;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "PostTaskEntity{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", address='" + address + '\'' +
                '}';
    }
}
