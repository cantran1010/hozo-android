package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LongBui on 4/12/17.
 */

public class Category implements Serializable {
    private int id;
    private String name;
    private String description;
    @SerializedName("suggest_title")
    private String suggestTitle;
    @SerializedName("suggest_description")
    private String suggestDescription;
    @SerializedName("avatar")
    private String presentPath;
    private boolean isSelected;
    private ArrayList<Category> categories;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

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
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", suggestTitle='" + suggestTitle + '\'' +
                ", suggestDescription='" + suggestDescription + '\'' +
                ", presentPath='" + presentPath + '\'' +
                ", isSelected=" + isSelected +
                ", categories=" + categories +
                ", status=" + status +
                '}';
    }

}
