package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonish1 on 5/10/17.
 */

public class Review {

    @SerializedName("author_id")
    private Integer authorId;
    @SerializedName("author_avatar")
    private String authorAvatar;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("task_name")
    private String taskName;
    private Integer type;
    private String body;
    private Integer rating;
    @SerializedName("created_at")
    private String createdAt;

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TaskerReview{" +
                "authorId=" + authorId +
                ", authorAvatar='" + authorAvatar + '\'' +
                ", authorName='" + authorName + '\'' +
                ", taskName='" + taskName + '\'' +
                ", type=" + type +
                ", body='" + body + '\'' +
                ", rating=" + rating +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
