package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by LongBui on 20/04/2017.
 */

public class Comment extends RealmObject implements Serializable {
    private String id;
    @SerializedName("author_id")
    private String authorId;
    @SerializedName("full_name")
    private String fullName;
    private String avatar;
    @SerializedName("image_url")
    private String imgAttach;
    private String body;
    @SerializedName("created_at")
    private String createdAt;
    private String taskId;

    public Comment() {
    }

    public Comment(String id, String author_id, String avatar, String body, String created_at, String task_id) {
        this.id = id;
        this.authorId = author_id;
        this.avatar = avatar;
        this.body = body;
        this.createdAt = created_at;
        this.taskId = task_id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
