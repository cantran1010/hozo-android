package vn.tonish.hozo.model;

/**
 * Created by CanTran on 11/8/17.
 */

class Status {
    private String name;
    private boolean selected;

    public Status(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
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

    @Override
    public String toString() {
        return "Status{" +
                "name='" + name + '\'' +
                ", selected=" + selected +
                '}';
    }
}
