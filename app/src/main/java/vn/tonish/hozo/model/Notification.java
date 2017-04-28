package vn.tonish.hozo.model;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Can Tran on 12/04/2017.
 */

public class Notification extends RealmObject implements Serializable {

    private int status;
    private int id;
    private String title;
    private String content;
    private String created_date;

    public Notification() {
    }

    public Notification(int status, int id, String title, String content, String created_date) {
        this.status = status;
        this.id = id;
        this.title = title;
        this.content = content;
        this.created_date = created_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
