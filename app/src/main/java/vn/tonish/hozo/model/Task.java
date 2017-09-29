package vn.tonish.hozo.model;

import java.util.Map;

/**
 * Created by LongBui on 9/13/17.
 */

public class Task {

    private int id;
    private String title;
    private int poster_id;
    private boolean block;
    private Map<String,Boolean> members;

    public Task() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPoster_id() {
        return poster_id;
    }

    public void setPoster_id(int poster_id) {
        this.poster_id = poster_id;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public Map<String, Boolean> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Boolean> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", poster_id=" + poster_id +
                ", block=" + block +
                ", members=" + members +
                '}';
    }

}