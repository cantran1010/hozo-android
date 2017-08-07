package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CanTran on 6/15/17.
 */

public class UpdateResponse {
    @SerializedName("force_update")
    private boolean forceUpdate;
    @SerializedName("recommend_update")
    private boolean recommendUpdate;

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public boolean isRecommendUpdate() {
        return recommendUpdate;
    }

    public void setRecommendUpdate(boolean recommendUpdate) {
        this.recommendUpdate = recommendUpdate;
    }

    @Override
    public String toString() {
        return "UpdateResponse{" +
                "forceUpdate=" + forceUpdate +
                ", recommendUpdate=" + recommendUpdate +
                '}';
    }

}
