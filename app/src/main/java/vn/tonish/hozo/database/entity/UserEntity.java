package vn.tonish.hozo.database.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by LongBui.
 */
public class UserEntity extends RealmObject implements Serializable {

    @PrimaryKey
    private int id;
    @SerializedName("full_name")
    private String fullName;
    private String phone;
    private String email;
    @SerializedName("facebook_id")
    private String facebookId;
    private String address;
    private String avatar;
    @SerializedName("date_of_birth")
    private String dateOfBirth;
    private String description;
    private String gender;
    @SerializedName("poster_average_rating")
    private float posterAverageRating;
    @SerializedName("tasker_average_rating")
    private float taskerAverageRating;
    @SerializedName("poster_review_count")
    private int posterReviewCount;
    @SerializedName("tasker_review_count")
    private int taskerReviewCount;
    private RealmList<ReviewEntity> reviews;
    private String accessToken;
    private String refreshToken;
    private String tokenExp;
    private boolean isMyUser;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getPosterAverageRating() {
        return posterAverageRating;
    }

    public void setPosterAverageRating(float posterAverageRating) {
        this.posterAverageRating = posterAverageRating;
    }

    public float getTaskerAverageRating() {
        return taskerAverageRating;
    }

    public void setTaskerAverageRating(float taskerAverageRating) {
        this.taskerAverageRating = taskerAverageRating;
    }

    public int getPosterReviewCount() {
        return posterReviewCount;
    }

    public void setPosterReviewCount(int posterReviewCount) {
        this.posterReviewCount = posterReviewCount;
    }

    public int getTaskerReviewCount() {
        return taskerReviewCount;
    }

    public void setTaskerReviewCount(int taskerReviewCount) {
        this.taskerReviewCount = taskerReviewCount;
    }

    public RealmList<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(RealmList<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenExp() {
        return tokenExp;
    }

    public void setTokenExp(String tokenExp) {
        this.tokenExp = tokenExp;
    }

    public boolean isMyUser() {
        return isMyUser;
    }

    public void setMyUser() {
        isMyUser = true;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", facebookId='" + facebookId + '\'' +
                ", address='" + address + '\'' +
                ", avatar='" + avatar + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", description='" + description + '\'' +
                ", gender='" + gender + '\'' +
                ", posterAverageRating=" + posterAverageRating +
                ", taskerAverageRating=" + taskerAverageRating +
                ", posterReviewCount=" + posterReviewCount +
                ", taskerReviewCount=" + taskerReviewCount +
                ", reviews=" + reviews +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", tokenExp='" + tokenExp + '\'' +
                ", isMyUser=" + isMyUser +
                '}';
    }
}