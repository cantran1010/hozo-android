package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by LongBui on 5/11/2017.
 */

public class Poster extends RealmObject implements Serializable{

    private int taskId;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("poster_average_rating")
    private float posterAverageRating;
    private Integer verify;
    private String avatar;
    private Integer id;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public float getPosterAverageRating() {
        return posterAverageRating;
    }

    public void setPosterAverageRating(float posterAverageRating) {
        this.posterAverageRating = posterAverageRating;
    }

    public Integer getVerify() {
        return verify;
    }

    public void setVerify(Integer verify) {
        this.verify = verify;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Poster{" +
                "fullName='" + fullName + '\'' +
                ", posterAverageRating=" + posterAverageRating +
                ", verify=" + verify +
                ", avatar='" + avatar + '\'' +
                ", id=" + id +
                '}';
    }
}
