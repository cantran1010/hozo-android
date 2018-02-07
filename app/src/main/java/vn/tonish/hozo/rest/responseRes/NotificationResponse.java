package vn.tonish.hozo.rest.responseRes;

/**
 * Created by LongBui on 2/7/18.
 */

public class NotificationResponse {

    private boolean notification;

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "NotificationResponse{" +
                "notification=" + notification +
                '}';
    }

}
