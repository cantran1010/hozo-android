package vn.tonish.hozo.model;

import java.util.List;

/**
 * Created by CanTran on 5/16/17.
 */

public class TaskSetting {
    private int userId;
    private List<Category> categoryEntities;
    private String minWorkerRate;
    private String maxWorkerRate;
    private double latitude;
    private double longitude;
    private String radius;
    private String gender;
    private String minAge;
    private String maxAge;

    public TaskSetting() {
    }

    public TaskSetting(int userId, List<Category> categoryEntities, String minWorkerRate, String maxWorkerRate, double latitude, double longitude, String radius, String gender, String minAge, String maxAge) {
        this.userId = userId;
        this.categoryEntities = categoryEntities;
        this.minWorkerRate = minWorkerRate;
        this.maxWorkerRate = maxWorkerRate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.gender = gender;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Category> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<Category> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public String getMinWorkerRate() {
        return minWorkerRate;
    }

    public void setMinWorkerRate(String minWorkerRate) {
        this.minWorkerRate = minWorkerRate;
    }

    public String getMaxWorkerRate() {
        return maxWorkerRate;
    }

    public void setMaxWorkerRate(String maxWorkerRate) {
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

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public String toString() {
        return "TaskSetting{" +
                "userId=" + userId +
                ", categoryEntities=" + categoryEntities +
                ", minWorkerRate='" + minWorkerRate + '\'' +
                ", maxWorkerRate='" + maxWorkerRate + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius='" + radius + '\'' +
                ", gender='" + gender + '\'' +
                ", minAge='" + minAge + '\'' +
                ", maxAge='" + maxAge + '\'' +
                '}';
    }
}
