package vn.tonish.hozo.model;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by huy_quynh on 4/12/17.
 */


public class Work extends RealmObject implements Serializable {

    private String id;
    private String name;
    private String time;
    private String type;
    private String price;
    private String des;
    private boolean isNew;
    private String status;

    public Work() {
    }

    public Work(String id, String name, String time, String type, String price, String des, boolean isNew, String status) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.type = type;
        this.price = price;
        this.des = des;
        this.isNew = isNew;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
