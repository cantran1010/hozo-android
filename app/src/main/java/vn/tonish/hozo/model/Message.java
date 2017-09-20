package vn.tonish.hozo.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

/**
 * Created by LongBui on 9/13/17.
 */

public class Message {

    private String id;
    private int user_id;
    private String message;
    private Object created_at;

    public Message() {
        this.created_at = ServerValue.TIMESTAMP;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Object created_at) {
        this.created_at = created_at;
    }

    @Exclude
    public Long getCreated_atLong(boolean isLong) {
        if (created_at instanceof Long) return (Long) created_at;
        else return null;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", user_id=" + user_id +
                ", message='" + message + '\'' +
                ", created_at=" + created_at +
                '}';
    }

}
