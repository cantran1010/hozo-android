package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import io.realm.annotations.PrimaryKey;

/**
 * Created by LongBui on 20/04/2017.
 */
public class Comment implements Serializable {
    @PrimaryKey
    private int id;
    @SerializedName("author_id")
    private int authorId;
    @SerializedName("task_id")
    private int taskId;
    @SerializedName("parent_id")
    private int parentId;
    @SerializedName("full_name")
    private String fullName;
    private String avatar;
    @SerializedName("image_url")
    private String imgAttach;
    private String body;
    @SerializedName("created_at")
    private String createdAt;
    private Date createdDateAt;
    @SerializedName("replies_count")
    private int repliesCount;
    @SerializedName("replies")
    private ArrayList<Comment> comments = new ArrayList<>();

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(int repliesCount) {
        this.repliesCount = repliesCount;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
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
        return createdDateAt;
    }

    public void setCreatedDateAt(Date createdDateAt) {
        this.createdDateAt = createdDateAt;
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
                ", taskId=" + taskId +
                ", parentId=" + parentId +
                ", fullName='" + fullName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", imgAttach='" + imgAttach + '\'' +
                ", body='" + body + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", createdDateAt=" + createdDateAt +
                ", repliesCount=" + repliesCount +
                ", comments=" + comments +
                '}';
    }
}