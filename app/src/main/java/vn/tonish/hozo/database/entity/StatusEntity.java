package vn.tonish.hozo.database.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by CanTran on 11/8/17.
 */

public class StatusEntity extends RealmObject {

    @PrimaryKey
    private String id;
    private String role;
    private String name;
    private String status;
    private boolean selected;

    public StatusEntity() {
    }

    public StatusEntity(String id, String role, String name, String status, boolean selected) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.status = status;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "StatusEntity{" +
                "id='" + id + '\'' +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", selected=" + selected +
                '}';
    }
}
