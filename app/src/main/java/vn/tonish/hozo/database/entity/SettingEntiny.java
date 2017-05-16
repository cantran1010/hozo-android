package vn.tonish.hozo.database.entity;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by CanTran on 5/17/17.
 */

public class SettingEntiny extends RealmObject implements Serializable {
    @PrimaryKey
    private int id;
    private String taskType;
    private String price;
    private String location;
    private String time;
    private String gender;
    private String age;
    private boolean isNotification;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean notification) {
        isNotification = notification;
    }

    @Override
    public String toString() {
        return "SettingEntiny{" +
                "id=" + id +
                ", taskType='" + taskType + '\'' +
                ", price='" + price + '\'' +
                ", location='" + location + '\'' +
                ", time='" + time + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", isNotification=" + isNotification +
                '}';
    }
}
