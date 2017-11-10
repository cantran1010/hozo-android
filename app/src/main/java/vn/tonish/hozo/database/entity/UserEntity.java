package vn.tonish.hozo.database.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import vn.tonish.hozo.rest.responseRes.ImageProfileResponse;
import vn.tonish.hozo.rest.responseRes.TagResponse;

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
    @SerializedName("email_active")
    private boolean emailActive;
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
    @SerializedName("tasker_done_count")
    private int taskerDoneCount;
    @SerializedName("poster_done_count")
    private int posterDoneCount;
    @SerializedName("tasker_done_rate")
    private float taskerDoneRate;
    @SerializedName("poster_done_rate")
    private float posterDoneRate;
    private double latitude;
    private double longitude;

//    // version 1.8.0
    private RealmList<TagResponse> skills;
    private RealmList<TagResponse> languages;
    private RealmList<ImageProfileResponse> images;
    private String experiences;
    @SerializedName("privacy_hide_gender")
    private boolean privacyGender;
    @SerializedName("privacy_hide_date_of_birth")
    private boolean privacyBirth;
    @SerializedName("activities_count")
    private int activitiesCount;
    @SerializedName("followers_count")
    private int followersCount;
    private String role;
    private boolean followed;
    private String background;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setMyUser(boolean myUser) {
        isMyUser = myUser;
    }

    public int getTaskerDoneCount() {
        return taskerDoneCount;
    }

    public void setTaskerDoneCount(int taskerDoneCount) {
        this.taskerDoneCount = taskerDoneCount;
    }

    public int getPosterDoneCount() {
        return posterDoneCount;
    }

    public void setPosterDoneCount(int posterDoneCount) {
        this.posterDoneCount = posterDoneCount;
    }

    public float getTaskerDoneRate() {
        return taskerDoneRate;
    }

    public void setTaskerDoneRate(float taskerDoneRate) {
        this.taskerDoneRate = taskerDoneRate;
    }

    public float getPosterDoneRate() {
        return posterDoneRate;
    }

    public void setPosterDoneRate(float posterDoneRate) {
        this.posterDoneRate = posterDoneRate;
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

    public boolean isEmailActive() {
        return emailActive;
    }

    public void setEmailActive(boolean emailActive) {
        this.emailActive = emailActive;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public RealmList<ImageProfileResponse> getImages() {
        return images;
    }

    public void setImages(RealmList<ImageProfileResponse> images) {
        this.images = images;
    }

    public String getExperiences() {
        return experiences;
    }

    public void setExperiences(String experiences) {
        this.experiences = experiences;
    }

    public boolean isPrivacyGender() {
        return privacyGender;
    }

    public void setPrivacyGender(boolean privacyGender) {
        this.privacyGender = privacyGender;
    }

    public boolean isPrivacyBirth() {
        return privacyBirth;
    }

    public void setPrivacyBirth(boolean privacyBirth) {
        this.privacyBirth = privacyBirth;
    }

    public int getActivitiesCount() {
        return activitiesCount;
    }

    public void setActivitiesCount(int activitiesCount) {
        this.activitiesCount = activitiesCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public RealmList<TagResponse> getSkills() {
        return skills;
    }

    public void setSkills(RealmList<TagResponse> skills) {
        this.skills = skills;
    }

    public RealmList<TagResponse> getLanguages() {
        return languages;
    }

    public void setLanguages(RealmList<TagResponse> languages) {
        this.languages = languages;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", emailActive=" + emailActive +
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
                ", taskerDoneCount=" + taskerDoneCount +
                ", posterDoneCount=" + posterDoneCount +
                ", taskerDoneRate=" + taskerDoneRate +
                ", posterDoneRate=" + posterDoneRate +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", skills=" + skills +
                ", languages=" + languages +
                ", images=" + images +
                ", experiences='" + experiences + '\'' +
                ", privacyGender=" + privacyGender +
                ", privacyBirth=" + privacyBirth +
                ", activitiesCount=" + activitiesCount +
                ", followersCount=" + followersCount +
                ", role='" + role + '\'' +
                ", followed=" + followed +
                ", background='" + background + '\'' +
                '}';
    }
}