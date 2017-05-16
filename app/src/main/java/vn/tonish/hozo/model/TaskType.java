package vn.tonish.hozo.model;

/**
 * Created by CanTran on 5/16/17.
 */

public class TaskType {
    private String taskType;
    private boolean isSelected;

    public TaskType() {
    }

    public TaskType(String taskType, boolean isSelected) {
        this.taskType = taskType;
        this.isSelected = isSelected;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
