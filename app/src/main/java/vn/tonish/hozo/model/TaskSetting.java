package vn.tonish.hozo.model;

/**
 * Created by CanTran on 5/16/17.
 */

public class TaskSetting {
    private String taskType;
    private String price;
    private String location;
    private String time;
    private String gender;
    private String age;
    private boolean isNotification;

    public TaskSetting() {
    }

    public TaskSetting(String taskType, String price, String location, String time, String gender, String age, boolean isNotification) {
        this.taskType = taskType;
        this.price = price;
        this.location = location;
        this.time = time;
        this.gender = gender;
        this.age = age;
        this.isNotification = isNotification;
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
}
