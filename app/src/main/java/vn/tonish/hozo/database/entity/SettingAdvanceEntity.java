package vn.tonish.hozo.database.entity;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by CanTran on 9/23/17.
 */

public class SettingAdvanceEntity extends RealmObject {
    @PrimaryKey
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
    private RealmList<RealmInt> categories;

    @SerializedName("nta_categories")
    private RealmList<RealmInt> ntaCategory;

    @SerializedName("filter_worker_days")
    private RealmList<RealmInt> days;

    @SerializedName("nta_worker_days")
    private RealmList<RealmInt> ntaDays;

    @SerializedName("filter_distance")
    private int distance;

    @SerializedName("nta_distance")
    private int ntaDistance;

    @SerializedName("filter_original_location")
    private RealmList<RealmDouble> latlon;

    @SerializedName("nta_origin_location")
    private RealmList<RealmDouble> ntaLatlon;


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
    private RealmList<RealmString> keywords;

    @SerializedName("nta_keywords")
    private RealmList<RealmString> ntaKeywords;
    @SerializedName("order_by")
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

    public RealmList<RealmInt> getCategories() {
        return categories;
    }

    public void setCategories(RealmList<RealmInt> categories) {
        this.categories = categories;
    }

    public RealmList<RealmInt> getNtaCategory() {
        return ntaCategory;
    }

    public void setNtaCategory(RealmList<RealmInt> ntaCategory) {
        this.ntaCategory = ntaCategory;
    }

    public RealmList<RealmInt> getDays() {
        return days;
    }

    public void setDays(RealmList<RealmInt> days) {
        this.days = days;
    }

    public RealmList<RealmInt> getNtaDays() {
        return ntaDays;
    }

    public void setNtaDays(RealmList<RealmInt> ntaDays) {
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

    public RealmList<RealmDouble> getLatlon() {
        return latlon;
    }

    public void setLatlon(RealmList<RealmDouble> latlon) {
        this.latlon = latlon;
    }

    public RealmList<RealmDouble> getNtaLatlon() {
        return ntaLatlon;
    }

    public void setNtaLatlon(RealmList<RealmDouble> ntaLatlon) {
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

    public RealmList<RealmString> getKeywords() {
        return keywords;
    }

    public void setKeywords(RealmList<RealmString> keywords) {
        this.keywords = keywords;
    }

    public RealmList<RealmString> getNtaKeywords() {
        return ntaKeywords;
    }

    public void setNtaKeywords(RealmList<RealmString> ntaKeywords) {
        this.ntaKeywords = ntaKeywords;
    }

    @Override
    public String toString() {
        return "SettingAdvanceEntity{" +
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