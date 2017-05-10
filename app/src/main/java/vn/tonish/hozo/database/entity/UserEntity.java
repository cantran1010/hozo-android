package vn.tonish.hozo.database.entity;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by LongBui.
 */
public class UserEntity extends RealmObject implements Serializable {

    @PrimaryKey
    private int id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String facebookId;
    private String address;
    private String avatar;
    private String dateOfBirth;
    private String description;
    private int verified;
    private float posterAverageRating;
    private float taskerAverageRating;
    private float posterReviewCount;
    private float taskerReviewCount;
    private String accessToken;
    private String refreshToken;
    private String tokenExp;

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

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
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
}