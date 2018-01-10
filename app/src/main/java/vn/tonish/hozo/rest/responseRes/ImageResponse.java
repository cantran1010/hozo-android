package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LongBui on 5/9/2017.
 */

public class ImageResponse {

    @SerializedName("tmp_id")
    private int idTemp;
    private String url;

    public int getIdTemp() {
        return idTemp;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "ImageResponse{" +
                "idTemp=" + idTemp +
                ", url='" + url + '\'' +
                '}';
    }
}
