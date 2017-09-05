package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vn.tonish.hozo.model.Comment;

/**
 * Created by LongBui on 5/11/2017.
 */

public class TaskResponse implements Serializable {

    private int id;
    @SerializedName("category_id")
    private int categoryId;
    private String title;
    private String description;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("created_at")
    private String createdAt = "0";
    private String status;
    @SerializedName("offer_status")
    private String offerStatus;
    @SerializedName("comments_count")
    private int commentsCount;
    private String gender;
    @SerializedName("min_age")
    private int minAge = 0;
    @SerializedName("max_age")
    private int maxAge = 0;
    private double latitude = 0;
    private double longitude = 0;
    private String city;
    private String district;
    private String address;
    @SerializedName("worker_rate")
    private int workerRate;
    @SerializedName("worker_count")
    private int workerCount;
    @SerializedName("assignee_count")
    private int assigneeCount;
    @SerializedName("bidder_count")
    private int bidderCount;
    private String role;
    private List<String> attachments = new ArrayList<>();
    private int[] attachmentsId;
    private String currency;
    @SerializedName("is_rate_poster")
    private boolean isRatePoster = false;
    private Poster poster = new Poster();
    private List<Bidder> bidders = new ArrayList<>();
    private List<Assigner> assignees = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    private boolean online;
    @SerializedName("auto_assign")
    private boolean autoAssign;
    private boolean advance;
    private boolean followed;

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public boolean isAdvance() {
        return advance;
    }

    public void setAdvance(boolean advance) {
        this.advance = advance;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isAutoAssign() {
        return autoAssign;
    }

    public void setAutoAssign(boolean autoAssign) {
        this.autoAssign = autoAssign;
    }

    public boolean isRatePoster() {
        return isRatePoster;
    }

    public void setRatePoster(boolean ratePoster) {
        isRatePoster = ratePoster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getWorkerRate() {
        return workerRate;
    }

    public void setWorkerRate(int workerRate) {
        this.workerRate = workerRate;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

    public int getAssigneeCount() {
        return assigneeCount;
    }

    public void setAssigneeCount(int assigneeCount) {
        this.assigneeCount = assigneeCount;
    }

    public int getBidderCount() {
        return bidderCount;
    }

    public void setBidderCount(int bidderCount) {
        this.bidderCount = bidderCount;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public int[] getAttachmentsId() {
        return attachmentsId;
    }

    public void setAttachmentsId(int[] attachmentsId) {
        this.attachmentsId = attachmentsId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Poster getPoster() {
        return poster;
    }

    public void setPoster(Poster poster) {
        this.poster = poster;
    }

    public List<Bidder> getBidders() {
        return bidders;
    }

    public void setBidders(List<Bidder> bidders) {
        this.bidders = bidders;
    }

    public List<Assigner> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<Assigner> assignees) {
        this.assignees = assignees;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "TaskResponse{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", status='" + status + '\'' +
                ", offerStatus='" + offerStatus + '\'' +
                ", commentsCount=" + commentsCount +
                ", gender='" + gender + '\'' +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", workerRate=" + workerRate +
                ", workerCount=" + workerCount +
                ", assigneeCount=" + assigneeCount +
                ", bidderCount=" + bidderCount +
                ", role='" + role + '\'' +
                ", attachments=" + attachments +
                ", attachmentsId=" + Arrays.toString(attachmentsId) +
                ", currency='" + currency + '\'' +
                ", isRatePoster=" + isRatePoster +
                ", poster=" + poster +
                ", bidders=" + bidders +
                ", assignees=" + assignees +
                ", comments=" + comments +
                ", online=" + online +
                ", autoAssign=" + autoAssign +
                ", advance=" + advance +
                ", followed=" + followed +
                '}';
    }

}