package vn.tonish.hozo.database.entity;

import io.realm.RealmObject;

/**
 * Created by LongBui on 6/12/17.
 */

public class BidderEntity extends RealmObject{
    private int id;
    private String fullName;
    private float taskerAverageRating;
    private int verify;
    private String avatar;
    private String bidedAt;
    private String phone;

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
                '}';
    }
}

