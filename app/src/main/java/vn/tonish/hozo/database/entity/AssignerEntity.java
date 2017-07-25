package vn.tonish.hozo.database.entity;

import io.realm.RealmObject;

/**
 * Created by LongBui on 5/15/17.
 */

public class AssignerEntity extends RealmObject{
    private int id;
    private String fullName;
    private float taskerAverageRating;
    private int verify;
    private String avatar;
    private String biddedAt;
    private String phone;
    private int rating;
    private String email;
    private String facebookId;
    private boolean emailActive;

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    @Override
    public String toString() {
        return "AssignerEntity{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", taskerAverageRating=" + taskerAverageRating +
                ", verify=" + verify +
                ", avatar='" + avatar + '\'' +
                ", biddedAt='" + biddedAt + '\'' +
                ", phone='" + phone + '\'' +
                ", rating=" + rating +
                ", email='" + email + '\'' +
                ", facebookId='" + facebookId + '\'' +
                ", emailActive=" + emailActive +
                '}';
    }
}
