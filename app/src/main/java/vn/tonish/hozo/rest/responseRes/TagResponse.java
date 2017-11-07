package vn.tonish.hozo.rest.responseRes;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by LongBui on 11/7/17.
 */

public class TagResponse extends RealmObject implements Serializable {

    private int id;
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TagResponse{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }

}
