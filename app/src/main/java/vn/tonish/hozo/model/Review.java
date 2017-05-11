package vn.tonish.hozo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Can Tran on 4/11/17.
 */


public class Review  implements Parcelable{

    @SerializedName("author_id")
    private Integer authorId;
    @SerializedName("author_avatar")
    private String authorAvatar;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("task_name")
    private String taskName;
    private Integer type;
    private String body;
    private Integer rating;
    @SerializedName("created_at")
    private String createdAt;

    public Review() {
    }

    protected Review(Parcel in) {
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

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
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
        return "TaskerReview{" +
                "authorId=" + authorId +
                ", authorAvatar='" + authorAvatar + '\'' +
                ", authorName='" + authorName + '\'' +
                ", taskName='" + taskName + '\'' +
                ", type=" + type +
                ", body='" + body + '\'' +
                ", rating=" + rating +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

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
}
