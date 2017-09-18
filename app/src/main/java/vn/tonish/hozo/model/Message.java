package vn.tonish.hozo.model;

import java.util.Map;

/**
 * Created by LongBui on 9/13/17.
 */

public class Message {

    private int user_id;
    private String message;
    private Map<String, String> created_at;

    public Message() {

    }

    public Message(int user_id, String message, Map<String, String> created_at) {
        this.user_id = user_id;
        this.message = message;
        this.created_at = created_at;
    }

    public void setCreated_at(Map<String, String> created_at) {
        this.created_at = created_at;
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

    public Map<String, String> getCreated_at() {
        return created_at;
    }

    @Override
    public String toString() {
        return "Message{" +
                "user_id=" + user_id +
                ", message='" + message + '\'' +
                ", created_at=" + created_at +
                '}';
    }

}
