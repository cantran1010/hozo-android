package vn.tonish.hozo.model;

import java.util.Map;

/**
 * Created by LongBui on 9/18/17.
 */

public class Member {

    private int id;
    private String full_name;
    private String avatar;
    private boolean block;
    private Map<String, Boolean> groups;

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

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", full_name='" + full_name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", block=" + block +
                ", groups=" + groups +
                '}';
    }

}
