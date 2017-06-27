package vn.tonish.hozo.model;

/**
 * Created by CanTran on 6/21/17.
 */

public class TaskAlert {
    private int id;
    private String name;
    private boolean  selected;

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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
