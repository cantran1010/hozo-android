package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LongBui on 5/9/2017.
 */

public class ImageResponse {

    @SerializedName("tmp_id")
    private int idTemp;

    public int getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(int idTemp) {
        this.idTemp = idTemp;
    }

    @Override
    public String toString() {
        return "ImageResponse{" +
                "idTemp=" + idTemp +
                '}';
    }

}
