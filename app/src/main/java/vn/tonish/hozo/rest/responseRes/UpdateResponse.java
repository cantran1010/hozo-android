package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CanTran on 6/15/17.
 */

public class UpdateResponse {
    @SerializedName("force_update")
    private String forceUpdate;
    @SerializedName("recommend_update")
    private String recommendUpdate;

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getRecommendUpdate() {
        return recommendUpdate;
    }

    public void setRecommendUpdate(String recommendUpdate) {
        this.recommendUpdate = recommendUpdate;
    }

    @Override
    public String toString() {
        return "UpdateResponse{" +
                "forceUpdate='" + forceUpdate + '\'' +
                ", recommendUpdate='" + recommendUpdate + '\'' +
                '}';
    }
}
