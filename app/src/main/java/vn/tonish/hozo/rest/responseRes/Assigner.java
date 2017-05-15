package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LongBui on 5/15/17.
 */

public class Assigner {
    private Integer id;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("tasker_average_rating")
    private float taskerAverageRating;
    private Integer verify;
    private String avatar;
    @SerializedName("bidded_at")
    private String biddedAt;
    private String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getBiddedAt() {
        return biddedAt;
    }

    public void setBiddedAt(String biddedAt) {
        this.biddedAt = biddedAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
