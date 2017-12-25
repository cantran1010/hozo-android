package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.database.entity.ReviewEntity;

/**
 * Created by CanTran on 12/25/17.
 */

public class BidResponse extends Bidder {
    private int id;
    @SerializedName("full_name")
    private String fullName;
    private String avatar;
    private int verify;
    @SerializedName("tasker_average_rating")
    private float taskerAverageRating;
    @SerializedName("tasker_done_count")
    private int taskerDoneCount;
    @SerializedName("tasker_done_rate")
    private int taskerDoneRate;
    private int price;
    private String message;
    private String description;
    @SerializedName("tasker_ratings")
    private List<Integer> taskerRatings = new ArrayList<>();
    @SerializedName("tasker_rating_count")
    private int taskerRatingCount;
    @SerializedName("tasker_count")
    private int taskerCount;
    @SerializedName("reviews")
    private List<ReviewEntity> reviews = new ArrayList<>();

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    @Override
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int getVerify() {
        return verify;
    }

    @Override
    public void setVerify(int verify) {
        this.verify = verify;
    }

    @Override
    public float getTaskerAverageRating() {
        return taskerAverageRating;
    }

    @Override
    public void setTaskerAverageRating(float taskerAverageRating) {
        this.taskerAverageRating = taskerAverageRating;
    }

    public int getTaskerDoneCount() {
        return taskerDoneCount;
    }

    public void setTaskerDoneCount(int taskerDoneCount) {
        this.taskerDoneCount = taskerDoneCount;
    }

    public int getTaskerDoneRate() {
        return taskerDoneRate;
    }

    public void setTaskerDoneRate(int taskerDoneRate) {
        this.taskerDoneRate = taskerDoneRate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getTaskerRatings() {
        return taskerRatings;
    }

    public void setTaskerRatings(List<Integer> taskerRatings) {
        this.taskerRatings = taskerRatings;
    }

    public int getTaskerRatingCount() {
        return taskerRatingCount;
    }

    public void setTaskerRatingCount(int taskerRatingCount) {
        this.taskerRatingCount = taskerRatingCount;
    }

    public int getTaskerCount() {
        return taskerCount;
    }

    public void setTaskerCount(int taskerCount) {
        this.taskerCount = taskerCount;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "BidResponse{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", verify=" + verify +
                ", taskerAverageRating=" + taskerAverageRating +
                ", taskerDoneCount=" + taskerDoneCount +
                ", taskerDoneRate=" + taskerDoneRate +
                ", price=" + price +
                ", message='" + message + '\'' +
                ", description='" + description + '\'' +
                ", taskerRatings=" + taskerRatings +
                ", taskerRatingCount=" + taskerRatingCount +
                ", taskerCount=" + taskerCount +
                ", reviews=" + reviews +
                '}';
    }
}
