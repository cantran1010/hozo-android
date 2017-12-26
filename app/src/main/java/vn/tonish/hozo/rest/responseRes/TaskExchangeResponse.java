package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LongBui on 12/26/17.
 */

public class TaskExchangeResponse {

    private  int id;
    private  String title;
    @SerializedName("worker_rate")
    private long workerRate;
    private List<AssignerExchangeResponse> assignees = new ArrayList<>();

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

    public long getWorkerRate() {
        return workerRate;
    }

    public void setWorkerRate(long workerRate) {
        this.workerRate = workerRate;
    }

    public List<AssignerExchangeResponse> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<AssignerExchangeResponse> assignees) {
        this.assignees = assignees;
    }

    @Override
    public String toString() {
        return "TaskExchangeResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", workerRate=" + workerRate +
                ", assignees=" + assignees +
                '}';
    }

}
