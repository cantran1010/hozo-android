package vn.tonish.hozo.model;

import java.io.Serializable;
import java.util.List;

import vn.tonish.hozo.rest.responseRes.TaskResponse;

/**
 * Created by CanTran on 1/31/18.
 */

public class ChatRoom implements Serializable {
    private int id;
    private String name;
    private String created_at;
    private List<Member> members;
    private TaskResponse taskResponse;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public TaskResponse getTaskResponse() {
        return taskResponse;
    }

    public void setTaskResponse(TaskResponse taskResponse) {
        this.taskResponse = taskResponse;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created_at='" + created_at + '\'' +
                ", members=" + members +
                ", taskResponse=" + taskResponse +
                '}';
    }
}
