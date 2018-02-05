package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import vn.tonish.hozo.model.HozoPlace;

/**
 * Created by CanTran on 2/5/18.
 */

public class PlaceReponse implements Serializable {
    @SerializedName("results")
    private List<HozoPlace> hozoPlaces;
    @SerializedName("status")
    private String status;

    public List<HozoPlace> getHozoPlaces() {
        return hozoPlaces;
    }

    public void setHozoPlaces(List<HozoPlace> hozoPlaces) {
        this.hozoPlaces = hozoPlaces;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PlaceReponse{" +
                "hozoPlaces=" + hozoPlaces +
                ", status='" + status + '\'' +
                '}';
    }
}
