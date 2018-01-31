package vn.tonish.hozo.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by LongBui on 9/18/17.
 */

public class Member implements Serializable {
    private int id;
    private String full_name;
    private String avatar;
    private boolean block;
    private Map<String, Boolean> groups;
    private Message message;
    private String phone;
    private boolean read;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public Map<String, Boolean> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Boolean> groups) {
        this.groups = groups;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", full_name='" + full_name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", block=" + block +
                ", groups=" + groups +
                ", message=" + message +
                ", phone='" + phone + '\'' +
                ", read=" + read +
                '}';
    }
}
