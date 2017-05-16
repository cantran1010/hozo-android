package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LongBui on 4/12/17.
 */

public class Category implements Serializable{
    private int id;
    private String name;
    private String description;
    @SerializedName("suggest_title")
    private String suggestTitle;
    @SerializedName("suggest_description")
    private String suggestDescription;
    @SerializedName("avatar")
    private String presentPath;

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

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", suggestTitle='" + suggestTitle + '\'' +
                ", suggestDescription='" + suggestDescription + '\'' +
                ", presentPath='" + presentPath + '\'' +
                '}';
    }

}
