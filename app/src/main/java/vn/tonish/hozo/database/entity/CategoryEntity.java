package vn.tonish.hozo.database.entity;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by LongBui on 4/12/17.
 */

public class CategoryEntity extends RealmObject implements Serializable{
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String suggestTitle;
    private String suggestDescription;
    private String presentPath;
    private boolean isSelected;

    public String getSuggestTitle() {
        return suggestTitle;
    }

    public void setSuggestTitle(String suggestTitle) {
        this.suggestTitle = suggestTitle;
    }

    public String getSuggestDescription() {
        return suggestDescription;
    }

    public void setSuggestDescription(String suggestDescription) {
        this.suggestDescription = suggestDescription;
    }

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "CategoryEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", suggestTitle='" + suggestTitle + '\'' +
                ", suggestDescription='" + suggestDescription + '\'' +
                ", presentPath='" + presentPath + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

}
