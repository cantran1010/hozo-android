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
                '}';
    }

}
