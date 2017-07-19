package vn.tonish.hozo.database.entity;

import io.realm.RealmObject;

/**
 * Created by LongBui on 6/12/17.
 */

public class PosterEntity extends RealmObject{

    private int id;
    private int taskId;
    private String fullName;
    private float posterAverageRating;
    private int verify;
    private String avatar;
    private String phone;
    private String email;
    private String facebookId;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PosterEntity{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", fullName='" + fullName + '\'' +
                ", posterAverageRating=" + posterAverageRating +
                ", verify=" + verify +
                ", avatar='" + avatar + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", facebookId='" + facebookId + '\'' +
                '}';
    }
}