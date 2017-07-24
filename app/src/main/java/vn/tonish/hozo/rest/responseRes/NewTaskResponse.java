package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LongBui on 7/21/17.
 */

public class NewTaskResponse {
    @SerializedName("new_number")
    private int countNewTask;

    public int getCountNewTask() {
        return countNewTask;
    }

    public void setCountNewTask(int countNewTask) {
        this.countNewTask = countNewTask;
    }

    @Override
    public String toString() {
        return "NewTaskResponse{" +
                "countNewTask=" + countNewTask +
                '}';
    }

}
