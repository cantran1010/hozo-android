package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LongBui on 12/04/2017.
 */

public class User implements Serializable {

    private Integer id;

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
    private Integer verified;
    @SerializedName("poster_average_rating")
    private Integer posterAverageRating;
    @SerializedName("tasker_average_rating")
    private Integer taskerAverageRating;
    @SerializedName("poster_review_count")
    private Integer posterReviewCount;
    @SerializedName("tasker_review_count")
    private Integer taskerReviewCount;
    @SerializedName("poster_reviews")
    private List<Review> posterReviews = null;
    @SerializedName("tasker_reviews")
    private List<Review> taskerReviews = null;

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

    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public Integer getPosterAverageRating() {
        return posterAverageRating;
    }

    public void setPosterAverageRating(Integer posterAverageRating) {
        this.posterAverageRating = posterAverageRating;
    }

    public Integer getTaskerAverageRating() {
        return taskerAverageRating;
    }

    public void setTaskerAverageRating(Integer taskerAverageRating) {
        this.taskerAverageRating = taskerAverageRating;
    }

    public Integer getPosterReviewCount() {
        return posterReviewCount;
    }

    public void setPosterReviewCount(Integer posterReviewCount) {
        this.posterReviewCount = posterReviewCount;
    }

    public Integer getTaskerReviewCount() {
        return taskerReviewCount;
    }

    public void setTaskerReviewCount(Integer taskerReviewCount) {
        this.taskerReviewCount = taskerReviewCount;
    }

    public List<Review> getPosterReviews() {
        return posterReviews;
    }

    public void setPosterReviews(List<Review> posterReviews) {
        this.posterReviews = posterReviews;
    }

    public List<Review> getTaskerReviews() {
        return taskerReviews;
    }

    public void setTaskerReviews(List<Review> taskerReviews) {
        this.taskerReviews = taskerReviews;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
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
                ", posterReviews=" + posterReviews +
                ", taskerReviews=" + taskerReviews +
                '}';
    }
}