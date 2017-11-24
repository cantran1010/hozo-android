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

    @SerializedName("notification_nta")
    private boolean ntaNotification;

    @SerializedName("filter_task_status")
    private String status;

    @SerializedName("nta_followed")
    private boolean ntaFollowed;

    @SerializedName("filter_categories")
    private List<Integer> categories;

    @SerializedName("nta_categories")
    private List<Integer> ntaCategory;

    @SerializedName("filter_worker_days")
    private List<Integer> days;

    @SerializedName("nta_worker_days")
    private List<Integer> ntaDays;

    @SerializedName("filter_distance")
    private int distance;

    @SerializedName("nta_distance")
    private int ntaDistance;

    @SerializedName("filter_original_location")
    private List<Double> latlon;

    @SerializedName("nta_origin_location")
    private List<Double> ntaLatlon;


    @SerializedName("filter_worker_rate_min")
    private long minWorkerRate;

    @SerializedName("nta_worker_rate_min")
    private long ntaMinWorkerRate;

    @SerializedName("filter_worker_rate_max")
    private long maxWorkerRate;

    @SerializedName("nta_worker_rate_max")
    private long ntaMaxWorkerRate;

    @SerializedName("filter_original_address")
    private String address;

    @SerializedName("nta_origin_address")
    private String ntaAddress;

    @SerializedName("filter_keywords")
    private List<String> keywords;

    @SerializedName("nta_keywords")
    private List<String> ntaKeywords;

    private String orderBy;

    private String order;

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

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

    public boolean isNtaNotification() {
        return ntaNotification;
    }

    public void setNtaNotification(boolean ntaNotification) {
        this.ntaNotification = ntaNotification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isNtaFollowed() {
        return ntaFollowed;
    }

    public void setNtaFollowed(boolean ntaFollowed) {
        this.ntaFollowed = ntaFollowed;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public List<Integer> getNtaCategory() {
        return ntaCategory;
    }

    public void setNtaCategory(List<Integer> ntaCategory) {
        this.ntaCategory = ntaCategory;
    }

    public List<Integer> getDays() {
        return days;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }

    public List<Integer> getNtaDays() {
        return ntaDays;
    }

    public void setNtaDays(List<Integer> ntaDays) {
        this.ntaDays = ntaDays;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getNtaDistance() {
        return ntaDistance;
    }

    public void setNtaDistance(int ntaDistance) {
        this.ntaDistance = ntaDistance;
    }

    public List<Double> getLatlon() {
        return latlon;
    }

    public void setLatlon(List<Double> latlon) {
        this.latlon = latlon;
    }

    public List<Double> getNtaLatlon() {
        return ntaLatlon;
    }

    public void setNtaLatlon(List<Double> ntaLatlon) {
        this.ntaLatlon = ntaLatlon;
    }

    public long getMinWorkerRate() {
        return minWorkerRate;
    }

    public void setMinWorkerRate(long minWorkerRate) {
        this.minWorkerRate = minWorkerRate;
    }

    public long getNtaMinWorkerRate() {
        return ntaMinWorkerRate;
    }

    public void setNtaMinWorkerRate(long ntaMinWorkerRate) {
        this.ntaMinWorkerRate = ntaMinWorkerRate;
    }

    public long getMaxWorkerRate() {
        return maxWorkerRate;
    }

    public void setMaxWorkerRate(long maxWorkerRate) {
        this.maxWorkerRate = maxWorkerRate;
    }

    public long getNtaMaxWorkerRate() {
        return ntaMaxWorkerRate;
    }

    public void setNtaMaxWorkerRate(long ntaMaxWorkerRate) {
        this.ntaMaxWorkerRate = ntaMaxWorkerRate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNtaAddress() {
        return ntaAddress;
    }

    public void setNtaAddress(String ntaAddress) {
        this.ntaAddress = ntaAddress;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getNtaKeywords() {
        return ntaKeywords;
    }

    public void setNtaKeywords(List<String> ntaKeywords) {
        this.ntaKeywords = ntaKeywords;
    }

    @Override
    public String toString() {
        return "SettingAdvance{" +
                "userId=" + userId +
                ", notification=" + notification +
                ", ntaNotification=" + ntaNotification +
                ", status='" + status + '\'' +
                ", ntaFollowed=" + ntaFollowed +
                ", categories=" + categories +
                ", ntaCategory=" + ntaCategory +
                ", days=" + days +
                ", ntaDays=" + ntaDays +
                ", distance=" + distance +
                ", ntaDistance=" + ntaDistance +
                ", latlon=" + latlon +
                ", ntaLatlon=" + ntaLatlon +
                ", minWorkerRate=" + minWorkerRate +
                ", ntaMinWorkerRate=" + ntaMinWorkerRate +
                ", maxWorkerRate=" + maxWorkerRate +
                ", ntaMaxWorkerRate=" + ntaMaxWorkerRate +
                ", address='" + address + '\'' +
                ", ntaAddress='" + ntaAddress + '\'' +
                ", keywords=" + keywords +
                ", ntaKeywords=" + ntaKeywords +
                ", orderBy='" + orderBy + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
