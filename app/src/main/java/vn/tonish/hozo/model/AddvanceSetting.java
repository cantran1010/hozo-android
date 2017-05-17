package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CanTran on 5/17/17.
 */

public class AddvanceSetting implements Serializable {
    private int userId;
    private ArrayList<Integer> categoryIds;
    @SerializedName("min_worker_rate")
    private String minWorkerRate;
    @SerializedName("max_worker_rate")
    private String maxWorkerRate;
    private double latitude;
    private double longitude;
    private String radius;
    @SerializedName("start_daytime")
    private String startDaytime;
    @SerializedName("end_daytime")
    private String endaytime;
    private String gender;
    @SerializedName("min_age")
    private String minAge;
    @SerializedName("max_age")
    private String maxAge;
    private boolean isNotification;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public ArrayList<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(ArrayList<Integer> categoryIds) {
        this.categoryIds = categoryIds;
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

    public String getStartDaytime() {
        return startDaytime;
    }

    public void setStartDaytime(String startDaytime) {
        this.startDaytime = startDaytime;
    }

    public String getEndaytime() {
        return endaytime;
    }

    public void setEndaytime(String endaytime) {
        this.endaytime = endaytime;
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

    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean notification) {
        isNotification = notification;
    }

    @Override
    public String toString() {
        return "AddvanceSetting{" +
                "userId=" + userId +
                ", categoryId=" + categoryIds +
                ", minWorkerRate='" + minWorkerRate + '\'' +
                ", maxWorkerRate='" + maxWorkerRate + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius='" + radius + '\'' +
                ", startDaytime='" + startDaytime + '\'' +
                ", endaytime='" + endaytime + '\'' +
                ", gender='" + gender + '\'' +
                ", minAge='" + minAge + '\'' +
                ", maxAge='" + maxAge + '\'' +
                ", isNotification=" + isNotification +
                '}';
    }
}
