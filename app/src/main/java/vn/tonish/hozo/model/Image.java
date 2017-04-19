package vn.tonish.hozo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ADMIN on 4/19/2017.
 */

public class Image implements Parcelable {

    public long id;
    public String name;
    public String path;
    public boolean isSelected;
    public boolean isAdd;

    public Image() {

    }

    private Image(Parcel in) {
        id = in.readLong();
        name = in.readString();
        path = in.readString();
    }

    public Image(long id, String name, String path, boolean isSelected, boolean isAdd) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.isSelected = isSelected;
        this.isAdd = isAdd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(path);
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", isSelected=" + isSelected +
                ", isAdd=" + isAdd +
                '}';
    }

}
