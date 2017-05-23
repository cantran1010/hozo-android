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

    private Integer id;
    @SerializedName("category_id")
    private Integer categoryId;
    private String title;
    private String description;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("created_at")
    private String createdAt;
    private String status;
    @SerializedName("comments_count")
    private Integer commentsCount;
    private String gender;
    @SerializedName("min_age")
    private Integer minAge;
    @SerializedName("max_age")
    private Integer maxAge;
    private Double latitude;
    private Double longitude;
    private String city;
    private String district;
    private String address;
    @SerializedName("worker_rate")
    private Integer workerRate;
    @SerializedName("worker_count")
    private Integer workerCount;
    @SerializedName("assignee_count")
    private int assigneeCount;
    @SerializedName("bidder_count")
    private int bidderCount;
    private String role;
    private List<String> attachments = new ArrayList<>();
    private Integer[] attachmentsId;
    private String currency;
    private Poster poster;
    private List<Bidder> bidders = new ArrayList<>();
    private List<Assigner> assignees = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Assigner> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<Assigner> assignees) {
        this.assignees = assignees;
    }

    public Integer[] getAttachmentsId() {
        return attachmentsId;
    }

    public void setAttachmentsId(Integer[] attachmentsId) {
        this.attachmentsId = attachmentsId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
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

    public Integer getWorkerRate() {
        return workerRate;
    }

    public void setWorkerRate(Integer workerRate) {
        this.workerRate = workerRate;
    }

    public Integer getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(Integer workerCount) {
        this.workerCount = workerCount;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
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
                ", poster=" + poster +
                ", bidders=" + bidders +
                ", assignees=" + assignees +
                ", comments=" + comments +
                '}';
    }
}