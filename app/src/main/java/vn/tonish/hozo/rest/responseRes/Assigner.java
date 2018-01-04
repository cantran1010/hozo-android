package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LongBui on 5/15/17.
 */

public class Assigner implements Serializable {
    private int id;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("tasker_average_rating")
    private float taskerAverageRating;
    private int verify;
    private String avatar;
    @SerializedName("bidded_at")
    private String biddedAt;
    private String phone;
    private int rating;
    private String email;
    @SerializedName("facebook_id")
    private String facebookId;
    @SerializedName("email_active")
    private boolean emailActive;
    @SerializedName("date_of_birth")
    private String dateOfBrirth;
    private String gender;
    @SerializedName("privacy_hide_date_of_birth")
    private boolean isHideAge;
    @SerializedName("privacy_hide_gender")
    private boolean isHideGender;
    @SerializedName("tasker_done_rate")
    private float posterDoneRate;
    @SerializedName("rating_body")
    private String ratingBody;
    @SerializedName("rating_confirm")
    private boolean ratingConfirm;
    private int price;

    public boolean isRatingConfirm() {
        return ratingConfirm;
    }

    public void setRatingConfirm(boolean ratingConfirm) {
        this.ratingConfirm = ratingConfirm;
    }

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

    public String getDateOfBrirth() {
        return dateOfBrirth;
    }

    public void setDateOfBrirth(String dateOfBrirth) {
        this.dateOfBrirth = dateOfBrirth;
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

    public String getRatingBody() {
        return ratingBody;
    }

    public void setRatingBody(String ratingBody) {
        this.ratingBody = ratingBody;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "Assigner{" +
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
                ", dateOfBrirth='" + dateOfBrirth + '\'' +
                ", gender='" + gender + '\'' +
                ", isHideAge=" + isHideAge +
                ", isHideGender=" + isHideGender +
                ", posterDoneRate=" + posterDoneRate +
                ", ratingBody='" + ratingBody + '\'' +
                ", ratingConfirm=" + ratingConfirm +
                ", price=" + price +
                '}';
    }
}
