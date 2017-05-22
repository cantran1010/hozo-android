package vn.tonish.hozo.database.entity;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.rest.responseRes.Poster;

/**
 * Created by LongBui on 5/17/17.
 */

public class TaskEntity extends RealmObject {
    @PrimaryKey
    private Integer id;
    private Integer categoryId;
    private String title;
    private String description;
    private String startTime;
    private String endTime;
    private Date createdAt;
    private String status;
    private Integer commentsCount;
    private String gender;
    private Integer minAge;
    private Integer maxAge;
    private Double latitude;
    private Double longitude;
    private String city;
    private String district;
    private String address;
    private Integer workerRate;
    private Integer workerCount;
    private int assigneeCount;
    private int bidderCount;
    private String attachments;
    private String currency;
    private String role;
    private Poster poster;
    private RealmList<Bidder> bidders = null;
    private RealmList<Assigner> assignees = null;
    private RealmList<Comment> comments = null;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public RealmList<Bidder> getBidders() {
        return bidders;
    }

    public void setBidders(RealmList<Bidder> bidders) {
        this.bidders = bidders;
    }

    public RealmList<Assigner> getAssignees() {
        return assignees;
    }

    public void setAssignees(RealmList<Assigner> assignees) {
        this.assignees = assignees;
    }

    public RealmList<Comment> getComments() {
        return comments;
    }

    public void setComments(RealmList<Comment> comments) {
        this.comments = comments;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public Poster getPoster() {
        return poster;
    }

    public void setPoster(Poster poster) {
        this.poster = poster;
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
}
