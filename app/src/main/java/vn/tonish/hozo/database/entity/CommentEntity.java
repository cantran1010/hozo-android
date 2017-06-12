package vn.tonish.hozo.database.entity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by LongBui on 20/04/2017.
 */
public class CommentEntity extends RealmObject{
    @PrimaryKey
    private int id;
    private int authorId;
    private String fullName;
    private String avatar;
    private String imgAttach;
    private String body;
    private String createdAt;
    private Date createdDateAt;
    private int taskId;

    public CommentEntity() {

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
                ", fullName='" + fullName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", imgAttach='" + imgAttach + '\'' +
                ", body='" + body + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", createdDateAt=" + createdDateAt +
                ", taskId='" + taskId + '\'' +
                '}';
    }

}