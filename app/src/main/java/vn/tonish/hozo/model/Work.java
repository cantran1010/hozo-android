package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LongBui on 4/12/17.
 */

public class Work implements Serializable {

    private int id;
    @SerializedName("type")
    private int workTypeId;
    private String workTypeName;
    @SerializedName("title")
    private String name;
    private String time;
    private String price;
    @SerializedName("description")
    private String description;
    private boolean isNew;
    private String date;
    private String address;
    private String timeAgo;
    // this is user post work
    private User user;
    @SerializedName("latitude")
    private Double lat;
    @SerializedName("longitude")
    private Double lon;
    private String status;
    private int numberDays;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("gender")
    private int genderWorker;
    @SerializedName("min_age")
    private int minAge;
    @SerializedName("max_age")
    private int maxAge;
    private String city;
    private String district;
    private Integer[] attachmentsImage;
    @SerializedName("worker_rate")
    private int workerRate;
    @SerializedName("worker_count")
    private int workerCount;

    public Integer[] getAttachmentsImage() {
        return attachmentsImage;
    }

    public void setAttachmentsImage(Integer[] attachmentsImage) {
        this.attachmentsImage = attachmentsImage;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getWorkerRate() {
        return workerRate;
    }

    public void setWorkerRate(int workerRate) {
        this.workerRate = workerRate;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

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

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
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

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
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
