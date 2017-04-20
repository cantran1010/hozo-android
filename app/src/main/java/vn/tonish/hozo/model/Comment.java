package vn.tonish.hozo.model;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by CanTran on 20/04/2017.
 */

public class Comment extends RealmObject implements Serializable {
    private String id;
    private String author_id;
    private String avatar;
    private String body;
    private String created_at;
    private String task_id;

    public Comment() {
    }

    public Comment(String id, String author_id, String avatar, String body, String created_at, String task_id) {
        this.id = id;
        this.author_id = author_id;
        this.avatar = avatar;
        this.body = body;
        this.created_at = created_at;
        this.task_id = task_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }
}
