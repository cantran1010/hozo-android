package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LongBui on 9/26/17.
 */

public class NofifySystemResponse {

    @SerializedName("notification_system")
    private boolean notificationSystem;


    public boolean isNotificationSystem() {
        return notificationSystem;
    }

    public void setNotificationSystem(boolean notificationSystem) {
        this.notificationSystem = notificationSystem;
    }

    @Override
    public String toString() {
        return "NofifySystemResponse{" +
                "notificationSystem=" + notificationSystem +
                '}';
    }

}
