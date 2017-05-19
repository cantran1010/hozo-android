package vn.tonish.hozo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CanTran on 5/17/17.
 */

public class AddvanceSetting implements Serializable {
    private int userId;
    private List<Category> categories;
    private long minWorkerRate;
    private long maxWorkerRate;
    private double latitude;
    private double longitude;
    private int radius;
    private String gender;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public long getMinWorkerRate() {
        return minWorkerRate;
    }

    public void setMinWorkerRate(long minWorkerRate) {
        this.minWorkerRate = minWorkerRate;
    }

    public long getMaxWorkerRate() {
        return maxWorkerRate;
    }

    public void setMaxWorkerRate(long maxWorkerRate) {
        this.maxWorkerRate = maxWorkerRate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "AddvanceSetting{" +
                "userId=" + userId +
                ", categories=" + categories +
                ", minWorkerRate=" + minWorkerRate +
                ", maxWorkerRate=" + maxWorkerRate +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                ", gender='" + gender + '\'' +
                '}';
    }
}
