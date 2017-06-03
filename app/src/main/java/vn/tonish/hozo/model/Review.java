package vn.tonish.hozo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CanTran on 4/11/17.
 */


public class Review implements Parcelable {

    private int id;
    @SerializedName("author_id")
    private int authorId;
    @SerializedName("author_avatar")
    private String authorAvatar;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("task_name")
    private String taskName;
    private String type;
    private String body;
    private int rating;
    @SerializedName("created_at")
    private String createdAt;

    public Review() {

    }

    private Review(Parcel in) {
        authorAvatar = in.readString();
        authorName = in.readString();
        taskName = in.readString();
        body = in.readString();
        createdAt = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authorAvatar);
        dest.writeString(authorName);
        dest.writeString(taskName);
        dest.writeString(body);
        dest.writeString(createdAt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", authorAvatar='" + authorAvatar + '\'' +
                ", authorName='" + authorName + '\'' +
                ", taskName='" + taskName + '\'' +
                ", type='" + type + '\'' +
                ", body='" + body + '\'' +
                ", rating=" + rating +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
