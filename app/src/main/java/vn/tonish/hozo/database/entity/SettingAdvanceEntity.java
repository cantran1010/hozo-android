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
    @SerializedName("filter_task_status")
    private String status;
    @SerializedName("filter_categories")
    private RealmList<RealmInt> categories;
    @SerializedName("filter_worker_days")
    private RealmList<RealmInt> days;
    @SerializedName("filter_distance")
    private int distance;
    @SerializedName("filter_original_location")
    private RealmList<RealmDouble> latlon;
    @SerializedName("filter_worker_rate_min")
    private long minWorkerRate;
    @SerializedName("filter_worker_rate_max")
    private long maxWorkerRate;
    @SerializedName("filter_keywords")
    private RealmList<RealmString> keywords;

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

    public RealmList<RealmInt> getCategories() {
        return categories;
    }

    public void setCategories(RealmList<RealmInt> categories) {
        this.categories = categories;
    }

    public RealmList<RealmInt> getDays() {
        return days;
    }

    public void setDays(RealmList<RealmInt> days) {
        this.days = days;
    }


    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public RealmList<RealmDouble> getLatlon() {
        return latlon;
    }

    public void setLatlon(RealmList<RealmDouble> latlon) {
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

    public RealmList<RealmString> getKeywords() {
        return keywords;
    }

    public void setKeywords(RealmList<RealmString> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "SettingAdvanceEntity{" +
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
                '}';
    }
}