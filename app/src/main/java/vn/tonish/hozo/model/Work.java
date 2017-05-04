package vn.tonish.hozo.model;

import java.io.Serializable;

/**
 * Created by LongBui on 4/12/17.
 */

public class Work implements Serializable {

    private int id;
    private int workTypeId;
    private String name;
    private String time;
    private String type;
    private String price;
    private String description;
    private boolean isNew;
    private String date;
    private String address;
    private String timeAgo;
    private String workType;
    // this is user post work
    private User user;
    private Double lat;
    private Double lon;
    private String status;
    private int numberDays;
    private String startTime;
    private String endTime;
    private int genderWorker;
    private int ageFromWorker;
    private int ageToWorker;

    public int getWorkTypeId() {
        return workTypeId;
    }

    public void setWorkTypeId(int workTypeId) {
        this.workTypeId = workTypeId;
    }

    public int getGenderWorker() {
        return genderWorker;
    }

    public void setGenderWorker(int genderWorker) {
        this.genderWorker = genderWorker;
    }

    public int getAgeFromWorker() {
        return ageFromWorker;
    }

    public void setAgeFromWorker(int ageFromWorker) {
        this.ageFromWorker = ageFromWorker;
    }

    public int getAgeToWorker() {
        return ageToWorker;
    }

    public void setAgeToWorker(int ageToWorker) {
        this.ageToWorker = ageToWorker;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getNumberDays() {
        return numberDays;
    }

    public void setNumberDays(int numberDays) {
        this.numberDays = numberDays;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Work() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

}
