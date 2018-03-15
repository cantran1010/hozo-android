package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Can Tran on 12/04/2017.
 */

public class Notification implements Serializable {
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("full_name")
    private String fullName;
    private String avatar;
    @SerializedName("task_id")
    private int taskId;
    @SerializedName("task_name")
    private String taskName;
    private String event;
    private Boolean read;
    @SerializedName("created_at")
    private String createdAt;
    private Date createdDateAt;
    private String content;
    @SerializedName("related_count")
    private int relatedCount;
    @SerializedName("external_link")
    private String externalLink;
    @SerializedName("task_start_time")
    private String taskStartTime;
    private String wallet;
    private int amount;
    @SerializedName("task_urgency")
    private boolean urgency;

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public int getRelatedCount() {
        return relatedCount;
    }

    public void setRelatedCount(int relatedCount) {
        this.relatedCount = relatedCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedDateAt() {
        return createdDateAt;
    }

    public void setCreatedDateAt(Date createdDateAt) {
        this.createdDateAt = createdDateAt;
    }

    public boolean isUrgency() {
        return urgency;
    }

    public void setUrgency(boolean urgency) {
        this.urgency = urgency;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", event='" + event + '\'' +
                ", read=" + read +
                ", createdAt='" + createdAt + '\'' +
                ", createdDateAt=" + createdDateAt +
                ", content='" + content + '\'' +
                ", relatedCount=" + relatedCount +
                ", externalLink='" + externalLink + '\'' +
                ", taskStartTime='" + taskStartTime + '\'' +
                ", wallet='" + wallet + '\'' +
                ", amount=" + amount +
                ", urgency=" + urgency +
                '}';
    }

}
