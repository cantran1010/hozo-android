package vn.tonish.hozo.database.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tonish1 on 5/10/17.
 */

public class ReviewEntity extends RealmObject {
    @PrimaryKey
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("author_id")
    private int authorId;
    @SerializedName("author_avatar")
    private String authorAvatar;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("task_name")
    private String taskName;
    private String type;
    private String body;
    private int rating;
    @SerializedName("created_at")
    private String createdAt;
    private Date craeatedDateAt;

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

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCraeatedDateAt() {
        return craeatedDateAt;
    }

    public void setCraeatedDateAt(Date craeatedDateAt) {
        this.craeatedDateAt = craeatedDateAt;
    }

    @Override
    public String toString() {
        return "ReviewEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", authorId=" + authorId +
                ", authorAvatar='" + authorAvatar + '\'' +
                ", authorName='" + authorName + '\'' +
                ", taskName='" + taskName + '\'' +
                ", type='" + type + '\'' +
                ", body='" + body + '\'' +
                ", rating=" + rating +
                ", createdAt='" + createdAt + '\'' +
                ", craeatedDateAt=" + craeatedDateAt +
                '}';
    }
}
