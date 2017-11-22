package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LongBui on 5/11/2017.
 */

public class Poster implements Serializable {

    private int id;
    private int taskId;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("poster_average_rating")
    private float posterAverageRating;
    private int verify;
    private String avatar;
    private String phone;
    private String email;
    @SerializedName("facebook_id")
    private String facebookId;
    @SerializedName("email_active")
    private boolean emailActive;
    @SerializedName("date_of_birth")
    private String  dateOfBirth;
    private String gender;
    @SerializedName("privacy_hide_date_of_birth")
    private boolean isHideAge;
    @SerializedName("privacy_hide_gender")
    private boolean isHideGender;

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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isHideAge() {
        return isHideAge;
    }

    public void setHideAge(boolean hideAge) {
        isHideAge = hideAge;
    }

    public boolean isHideGender() {
        return isHideGender;
    }

    public void setHideGender(boolean hideGender) {
        isHideGender = hideGender;
    }

    @Override
    public String toString() {
        return "Poster{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", fullName='" + fullName + '\'' +
                ", posterAverageRating=" + posterAverageRating +
                ", verify=" + verify +
                ", avatar='" + avatar + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", facebookId='" + facebookId + '\'' +
                ", emailActive=" + emailActive +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", isHideAge=" + isHideAge +
                ", isHideGender=" + isHideGender +
                '}';
    }
}