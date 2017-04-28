package vn.tonish.hozo.model;

import java.io.Serializable;

/**
 * Created by LongBui on 4/12/17.
 */

public class Category implements Serializable {
    private int id;
    private String name;
    private String presentPath;
    private String description;

    public String getPresentPath() {
        return presentPath;
    }

    public void setPresentPath(String presentPath) {
        this.presentPath = presentPath;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
