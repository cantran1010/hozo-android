package vn.tonish.hozo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by huyquynh on 4/20/17.
 */

public class FeedBack extends RealmObject implements Serializable {

    private int id;
    private String avatar;
    private String content;
    private String name;
    private String time;
    private double rate;

    public FeedBack() {
    }

    public FeedBack(int id, String avatar, String content, String name, String time, double rate) {
        this.id = id;
        this.avatar = avatar;
        this.content = content;
        this.name = name;
        this.time = time;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
