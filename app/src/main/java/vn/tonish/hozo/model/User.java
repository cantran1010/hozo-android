package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LongBui on 12/04/2017.
 */

public class User implements Serializable {

    private int id;

    @SerializedName("full_name")
    private String fullName;
    private String phoneNumber;
    private String email;
    @SerializedName("facebook_id")
    private String facebookId;
    private String address;
    private String avatar;
    @SerializedName("date_of_birth")
    private String dateOfBirth;
    private String description;
    private Integer verified;
    @SerializedName("poster_average_rating")
    private float posterAverageRating;
    @SerializedName("tasker_average_rating")
    private float taskerAverageRating;
    @SerializedName("poster_review_count")
    private float posterReviewCount;
    @SerializedName("tasker_review_count")
    private float taskerReviewCount;
    private List<Review> reviews = new ArrayList<>();

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public void setTaskerReviewCount(Integer taskerReviewCount) {
        this.taskerReviewCount = taskerReviewCount;
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

    public float getPosterReviewCount() {
        return posterReviewCount;
    }

    public void setPosterReviewCount(float posterReviewCount) {
        this.posterReviewCount = posterReviewCount;
    }

    public float getTaskerReviewCount() {
        return taskerReviewCount;
    }

    public void setTaskerReviewCount(float taskerReviewCount) {
        this.taskerReviewCount = taskerReviewCount;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", facebookId='" + facebookId + '\'' +
                ", address='" + address + '\'' +
                ", avatar='" + avatar + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", description='" + description + '\'' +
                ", verified=" + verified +
                ", posterAverageRating=" + posterAverageRating +
                ", taskerAverageRating=" + taskerAverageRating +
                ", posterReviewCount=" + posterReviewCount +
                ", taskerReviewCount=" + taskerReviewCount +
                ", reviews=" + reviews +
                '}';
    }
}