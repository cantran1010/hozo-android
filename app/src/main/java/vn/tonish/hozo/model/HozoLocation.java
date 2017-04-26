package vn.tonish.hozo.model;

import java.io.Serializable;

/**
 * Created by LongBui on 4/26/2017.
 */

public class HozoLocation implements Serializable{
    private String address;
    private double lat;
    private double lon;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
