package vn.tonish.hozo.rest.responseRes;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by LongBui on 5/9/2017.
 */

public class ImageProfileResponse extends RealmObject implements Serializable {

    private int id;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ImageProfileResponse{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }

}
