package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LongBui on 5/11/2017.
 */

public class Bidder implements Serializable{
    private int id;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("tasker_average_rating")
    private float taskerAverageRating;
    private int verify;
    private String avatar;
    @SerializedName("bidded_at")
    private String bidedAt;
    private String phone;
    private String email;
    @SerializedName("facebook_id")
    private String facebookId;
    @SerializedName("email_active")
    private boolean emailActive;
    @SerializedName("tasker_done_rate")
    private float posterDoneRate;

    public float getPosterDoneRate() {
        return posterDoneRate;
    }

    public void setPosterDoneRate(float posterDoneRate) {
        this.posterDoneRate = posterDoneRate;
    }

    public boolean isEmailActive() {
        return emailActive;
    }

    public void setEmailActive(boolean emailActive) {
        this.emailActive = emailActive;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
                ", email='" + email + '\'' +
                ", facebookId='" + facebookId + '\'' +
                ", emailActive=" + emailActive +
                ", posterDoneRate=" + posterDoneRate +
                '}';
    }

}
