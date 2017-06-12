package vn.tonish.hozo.database.entity;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by LongBui on 5/17/17.
 */

public class TaskEntity extends RealmObject {
    @PrimaryKey
    private int id;
    private int categoryId;
    private String title;
    private String description;
    private Date startTime;
    private Date endTime;
    private Date createdAt;
    private String status;
    private String offerStatus;
    private int commentsCount;
    private String gender;
    private int minAge;
    private int maxAge;
    private double latitude;
    private double longitude;
    private String city;
    private String district;
    private String address;
    private int workerRate;
    private int workerCount;
    private int assigneeCount;
    private int bidderCount;
    private String attachments;
    private String currency;
    private String role;
    private PosterEntity poster;
    private RealmList<BidderEntity> bidders = null;
    private RealmList<AssignerEntity> assignees = null;
    private RealmList<CommentEntity> comments = null;

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
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

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public PosterEntity getPoster() {
        return poster;
    }

    public void setPoster(PosterEntity poster) {
        this.poster = poster;
    }

    public RealmList<BidderEntity> getBidders() {
        return bidders;
    }

    public void setBidders(RealmList<BidderEntity> bidders) {
        this.bidders = bidders;
    }

    public RealmList<AssignerEntity> getAssignees() {
        return assignees;
    }

    public void setAssignees(RealmList<AssignerEntity> assignees) {
        this.assignees = assignees;
    }

    public RealmList<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(RealmList<CommentEntity> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createdAt=" + createdAt +
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
                ", attachments='" + attachments + '\'' +
                ", currency='" + currency + '\'' +
                ", role='" + role + '\'' +
                ", poster=" + poster +
                ", bidders=" + bidders +
                ", assignees=" + assignees +
                ", comments=" + comments +
                '}';
    }
}
