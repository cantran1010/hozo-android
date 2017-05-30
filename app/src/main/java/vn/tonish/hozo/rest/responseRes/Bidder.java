package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by LongBui on 5/11/2017.
 */

public class Bidder extends RealmObject implements Serializable{
    private Integer id;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("tasker_average_rating")
    private float taskerAverageRating;
    private Integer verify;
    private String avatar;
    @SerializedName("bidded_at")
    private String bidedAt;
    private String phone;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public float getTaskerAverageRating() {
        return taskerAverageRating;
    }

    public void setTaskerAverageRating(float taskerAverageRating) {
        this.taskerAverageRating = taskerAverageRating;
    }

    public Integer getVerify() {
        return verify;
    }

    public void setVerify(Integer verify) {
        this.verify = verify;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBidedAt() {
        return bidedAt;
    }

    public void setBidedAt(String bidedAt) {
        this.bidedAt = bidedAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Bidder{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", taskerAverageRating=" + taskerAverageRating +
                ", verify=" + verify +
                ", avatar='" + avatar + '\'' +
                ", bidedAt='" + bidedAt + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

}
