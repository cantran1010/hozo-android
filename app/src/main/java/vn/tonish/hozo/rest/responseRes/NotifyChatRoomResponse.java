package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LongBui on 9/22/17.
 */

public class NotifyChatRoomResponse {
    @SerializedName("notification_chat_group")
    private boolean notificationChatGroup;

    public boolean isNotificationChatGroup() {
        return notificationChatGroup;
    }

    public void setNotificationChatGroup(boolean notificationChatGroup) {
        this.notificationChatGroup = notificationChatGroup;
    }

    @Override
    public String toString() {
        return "NotifyChatRoomResponse{" +
                "notificationChatGroup=" + notificationChatGroup +
                '}';
    }

}
