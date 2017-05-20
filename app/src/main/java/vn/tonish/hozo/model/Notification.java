package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Can Tran on 12/04/2017.
 */

public class Notification extends RealmObject implements Serializable {

    private Integer id;
    @SerializedName("user_id")
    private Integer userId;
    @SerializedName("full_name")
    private String fullName;
    private String avatar;
    @SerializedName("task_id")
    private Integer taskId;
    @SerializedName("task_name")
    private String taskName;
    private String event;
    private Boolean read;
    @SerializedName("created_at")
    private String createdAt;
    private Date dateAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
                '}';
    }

}
