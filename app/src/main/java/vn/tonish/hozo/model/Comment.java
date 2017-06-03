package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by LongBui on 20/04/2017.
 */
public class Comment extends RealmObject implements Serializable {
    @PrimaryKey
    private int id;
    @SerializedName("author_id")
    private int authorId;
    @SerializedName("full_name")
    private String fullName;
    private String avatar;
    @SerializedName("image_url")
    private String imgAttach;
    private String body;
    @SerializedName("created_at")
    private String createdAt;
    private Date craetedDateAt;
    private int taskId;

    public Comment() {

    }

    public String getImgAttach() {
        return imgAttach;
    }

    public void setImgAttach(String imgAttach) {
        this.imgAttach = imgAttach;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedDateAt() {
        return craetedDateAt;
    }

    public void setCraetedDateAt(Date craetedDateAt) {
        this.craetedDateAt = craetedDateAt;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", fullName='" + fullName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", imgAttach='" + imgAttach + '\'' +
                ", body='" + body + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", craetedDateAt=" + craetedDateAt +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}