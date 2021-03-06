/*
  Copyright 2016 Google Inc. All Rights Reserved.
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package vn.tonish.hozo.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.MainActivity;
import vn.tonish.hozo.activity.SplashActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PreferUtils;

/**
 * When implement FCM, add project on fire base console, and change google-service.json
 */
public class MyFireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFireBaseMessagingService.class.getName();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Fire Base Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        LogUtils.d(TAG, "onMessageReceived start");
        LogUtils.d(TAG, "onMessageReceived Message Data : " + remoteMessage.getData());
        LogUtils.d(TAG, "onMessageReceived Message Data --> data : " + remoteMessage.getData().get("data"));

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Notification notification = gson.fromJson(remoteMessage.getData().get("data"), Notification.class);
        sendNotification(notification);

//        vn.tonish.hozo.database.manager.NotificationManager.insertNotification(notification);
    }
    // [END receive_message]

    private void sendNotification(Notification notification) {
        // fix crash on crashlytic
        if (!UserManager.checkLogin()) return;
        if (notification == null || notification.getEvent() == null) return;
        if (notification.getEvent().equals(Constants.PUSH_TYPE_CHAT) && !PreferUtils.isPushShow(getApplicationContext()))
            return;
        if (notification.getEvent().equals(Constants.PUSH_TYPE_PRIVATE_CHAT) && !PreferUtils.isPushPrivateShow(getApplicationContext()))
            return;
//        String title;
//        String message;
        Intent intent;

        if (notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_USER)) {
            intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(Constants.NOTIFICATION_EXTRA, notification);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
//        switch (notification.getEvent()) {
//            case Constants.PUSH_TYPE_ADMIN_PUSH:
//            case Constants.PUSH_TYPE_BLOCK_USER:
//            case Constants.PUSH_TYPE_ACTIVE_USER:
//            case Constants.PUSH_TYPE_ACTIVE_TASK:
//            case Constants.PUSH_TYPE_ACTIVE_COMMENT:
//            case Constants.PUSH_TYPE_BLOCK_TASK:
//            case Constants.PUSH_TYPE_BLOCK_COMMENT:
//            case Constants.PUSH_TYPE_ADMIN_NEW_TASK_ALERT:
//                title = getString(R.string.app_name);
//                message = notification.getContent();
//                break;
//            case Constants.PUSH_TYPE_NEW_TASK_ALERT:
//                if (notification.isUrgency()) {
//                    title = getString(R.string.push_title_urgency, DateTimeUtils.getOnlyDateFromIso(notification.getTaskStartTime()));
//                    message = notification.getTaskName();
//                } else {
//                    title = getString(R.string.push_title);
//                    message = notification.getTaskName();
//                }
//                break;
//            case Constants.PUSH_TYPE_MONEY_RECEIVED:
//            case Constants.PUSH_TYPE_WAGE_RECEIVED:
//            case Constants.PUSH_TYPE_POSTING_BONUS_RECEIVED:
//            case Constants.PUSH_TYPE_REF_BONUS_RECEIVED:
//                title = getString(R.string.app_name);
//                message = Utils.getContentFromNotification(getApplicationContext(), notification);
//                break;
////            case Constants.PUSH_TYPE_PRIVATE_CHAT:
////                title = getString(R.string.notification_private_chat);
////                message = Utils.getContentFromNotification(getApplicationContext(), notification);
////                break;
//            default:
//                title = notification.getTaskName();
//                message = Utils.getContentFromNotification(getApplicationContext(), notification);
//                break;
//        }

//        if (!notification.getEvent().equals(Constants.PUSH_TYPE_CHAT) || !notification.getEvent().equals(Constants.PUSH_TYPE_PRIVATE_CHAT)) {
//            // vibrator when receive push notification from server
//            Vibrator v = (Vibrator) getApplicationContext()
//                    .getSystemService(Context.VIBRATOR_SERVICE);
//            assert v != null;
//            v.vibrate(500);
//        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, notification.getId() /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(notification.getTitle())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notification.getBody()))
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

//        notificationBuilder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.push_sound));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(Color.parseColor("#d4e6f1"));
            notificationBuilder.setSmallIcon(R.mipmap.notification_icon);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.notification_icon);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//        assert notificationManager != null;
//        notificationManager.notify(notification.getId() /* ID of notification */, notificationBuilder.build());

        // group notification on notification bar
        switch (notification.getEvent()) {
            case Constants.PUSH_TYPE_COMMENT_RECEIVED:
            case Constants.PUSH_TYPE_COMMENT_REPLIED:
                notificationManager.notify(notification.getTaskId() * 10 + 1, notificationBuilder.build());
                break;

            case Constants.PUSH_TYPE_BID_RECEIVED:
                notificationManager.notify(notification.getTaskId() * 10 + 2, notificationBuilder.build());
                break;

            case Constants.PUSH_TYPE_CHAT:
                notificationManager.notify(notification.getTaskId() * 10 + 3, notificationBuilder.build());
                break;

            case Constants.PUSH_TYPE_PRIVATE_CHAT:
                notificationManager.notify(notification.getTaskId() * 10 + 4, notificationBuilder.build());
                break;

            default:
                notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
                break;

        }


        if (notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_USER)) {
            Intent intentBlock = new Intent();
            intentBlock.setAction(Constants.BROAD_CAST_BLOCK_USER);
            sendBroadcast(intentBlock);
        }
        switch (notification.getEvent()) {
            case Constants.PUSH_TYPE_NEW_TASK_ALERT: {
                PreferUtils.setPushNewTaskCount(getApplicationContext(), PreferUtils.getPushNewTaskCount(getApplicationContext()) + 1);
                Intent intentPushCount = new Intent();
                intentPushCount.setAction(Constants.BROAD_CAST_PUSH_COUNT);
                sendBroadcast(intentPushCount);

                break;
            }
            case Constants.PUSH_TYPE_CHAT:
            case Constants.PUSH_TYPE_PRIVATE_CHAT:
                PreferUtils.setNewPushChatCount(getApplicationContext(), PreferUtils.getNewPushChatCount(getApplicationContext()) + 1);
                Intent intentP = new Intent();
                intentP.setAction(Constants.BROAD_CAST_PUSH_COUNT);
                sendBroadcast(intentP);
                break;

            default: {
                PreferUtils.setNewPushCount(getApplicationContext(), PreferUtils.getNewPushCount(getApplicationContext()) + 1);
                Intent intentPushCount = new Intent();
                intentPushCount.setAction(Constants.BROAD_CAST_PUSH_COUNT);
                sendBroadcast(intentPushCount);
                break;
            }
        }

        if ((notification.getEvent().equals(Constants.PUSH_TYPE_COMMENT_RECEIVED) || notification.getEvent().equals(Constants.PUSH_TYPE_COMMENT_REPLIED)) && notification.getTaskId() != 0) {
            Intent intentComment = new Intent();
            intentComment.setAction(Constants.BROAD_CAST_MY);
            intentComment.putExtra(Constants.TASK_ID_EXTRA, notification.getTaskId());
            intentComment.putExtra(Constants.PUSH_COUNT_EXTRA, Constants.COUNT_COMMENT);
            sendBroadcast(intentComment);
        } else if (notification.getEvent().equals(Constants.PUSH_TYPE_BID_RECEIVED) && notification.getTaskId() != 0) {
            Intent intentComment = new Intent();
            intentComment.setAction(Constants.BROAD_CAST_MY);
            intentComment.putExtra(Constants.TASK_ID_EXTRA, notification.getTaskId());
            intentComment.putExtra(Constants.PUSH_COUNT_EXTRA, Constants.COUNT_BIDDER);
            sendBroadcast(intentComment);
        }

    }

}
