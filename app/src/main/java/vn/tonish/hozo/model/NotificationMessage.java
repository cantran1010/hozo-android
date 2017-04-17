package vn.tonish.hozo.model;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by CanTran on 12/04/2017.
 */

public class NotificationMessage extends RealmObject implements Serializable {
    private int status;
    private String id;
    private String type;
    private String avata;
    private String name;
    private String created_date;

    public NotificationMessage() {
    }

    public NotificationMessage(int status, String id, String type, String avata, String name, String created_date) {
        this.status = status;
        this.id = id;
        this.type = type;
        this.avata = avata;
        this.name = name;
        this.created_date = created_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvata() {
        return avata;
    }

    public void setAvata(String avata) {
        this.avata = avata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
