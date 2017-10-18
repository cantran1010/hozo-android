package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CanTran on 10/17/17.
 */

public class SettingAdvance {
    private int userId;
    @SerializedName("notification_chat_group")
    private boolean notification;
    @SerializedName("filter_task_status")
    private String status;
    @SerializedName("filter_categories")
    private List<Integer> categories;
    @SerializedName("filter_worker_days")
    private List<Integer> days;
    @SerializedName("filter_distance")
    private int distance;
    @SerializedName("filter_original_location")
    private List<Double> latlon;
    @SerializedName("filter_worker_rate_min")
    private long minWorkerRate;
    @SerializedName("filter_worker_rate_max")
    private long maxWorkerRate;
    @SerializedName("filter_keywords")
    private List<String> keywords;
    @SerializedName("filter_original_address")
    private String address;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public List<Integer> getDays() {
        return days;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<Double> getLatlon() {
        return latlon;
    }

    public void setLatlon(List<Double> latlon) {
        this.latlon = latlon;
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

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SettingAdvance{" +
                "userId=" + userId +
                ", notification=" + notification +
                ", status='" + status + '\'' +
                ", categories=" + categories +
                ", days=" + days +
                ", distance=" + distance +
                ", latlon=" + latlon +
                ", minWorkerRate=" + minWorkerRate +
                ", maxWorkerRate=" + maxWorkerRate +
                ", keywords=" + keywords +
                ", address='" + address + '\'' +
                '}';
    }
}
