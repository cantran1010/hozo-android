package vn.tonish.hozo.model;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by CanTran on 12/04/2017.
 */

public class User extends RealmObject implements Serializable {
    private String id;
    private String mobile;
    private String token_key;
    private String full_name;
    private String created_at;
    private String avatar;
    private String address;
    private String facebook_id;
    private String date_of_birth;
    private String gender;
    private String verify;
    private String average_rating;
    private String average_stars;
    private String description;
    private String education;
    private String email;
    private String languages;
    private String ranking;
    private String rating_percentage;
    private String review_counts;
    private String updated_at;
    private Comment review;

    public User() {
    }

    public User(String id, String mobile, String token_key, String full_name, String created_at, String avatar, String address, String facebook_id, String date_of_birth, String gender, String verify, String average_rating, String average_stars, String description, String education, String email, String languages, String ranking, String rating_percentage, String review_counts, String updated_at, Comment review) {
        this.id = id;
        this.mobile = mobile;
        this.token_key = token_key;
        this.full_name = full_name;
        this.created_at = created_at;
        this.avatar = avatar;
        this.address = address;
        this.facebook_id = facebook_id;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.verify = verify;
        this.average_rating = average_rating;
        this.average_stars = average_stars;
        this.description = description;
        this.education = education;
        this.email = email;
        this.languages = languages;
        this.ranking = ranking;
        this.rating_percentage = rating_percentage;
        this.review_counts = review_counts;
        this.updated_at = updated_at;
        this.review = review;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken_key() {
        return token_key;
    }

    public void setToken_key(String token_key) {
        this.token_key = token_key;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public String getAverage_stars() {
        return average_stars;
    }

    public void setAverage_stars(String average_stars) {
        this.average_stars = average_stars;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getRating_percentage() {
        return rating_percentage;
    }

    public void setRating_percentage(String rating_percentage) {
        this.rating_percentage = rating_percentage;
    }

    public String getReview_counts() {
        return review_counts;
    }

    public void setReview_counts(String review_counts) {
        this.review_counts = review_counts;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Comment getReview() {
        return review;
    }

    public void setReview(Comment review) {
        this.review = review;
    }
}
