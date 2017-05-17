package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by LongBui on 5/11/2017.
 */

public class Bidder extends RealmObject{
    private Integer id;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("poster_average_rating")
    private float posterAverageRating;
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

    public float getPosterAverageRating() {
        return posterAverageRating;
    }

    public void setPosterAverageRating(float posterAverageRating) {
        this.posterAverageRating = posterAverageRating;
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
                "fullName='" + fullName + '\'' +
                ", posterAverageRating=" + posterAverageRating +
                ", verify=" + verify +
                ", avatar='" + avatar + '\'' +
                ", id=" + id +
                ", bidedAt='" + bidedAt + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
