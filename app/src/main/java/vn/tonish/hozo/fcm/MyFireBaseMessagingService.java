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
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
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
import vn.tonish.hozo.utils.Utils;

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

        String title;
        String message;
        Intent intent;

        if (notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_USER)) {
            intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(Constants.NOTIFICATION_EXTRA, notification);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        if (notification.getEvent().equals(Constants.PUSH_TYPE_ADMIN_PUSH)
                || notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_USER)
                || notification.getEvent().equals(Constants.PUSH_TYPE_ACTIVE_USER)
                || notification.getEvent().equals(Constants.PUSH_TYPE_ACTIVE_TASK)
                || notification.getEvent().equals(Constants.PUSH_TYPE_ACTIVE_COMMENT)
                || notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_TASK)
                || notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_COMMENT)
                || notification.getEvent().equals(Constants.PUSH_TYPE_ADMIN_NEW_TASK_ALERT)
                ) {
            title = getString(R.string.app_name);
            message = notification.getContent();
        } else {
            title = notification.getTaskName();
            message = Utils.getContentFromNotification(getApplicationContext(), notification);
        }

        if (!notification.getEvent().equals(Constants.PUSH_TYPE_CHAT)) {
            // vibrator when receive push notification from server
            Vibrator v = (Vibrator) getApplicationContext()
                    .getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, notification.getId() /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

//        notificationBuilder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.push_sound));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x008000;
            notificationBuilder.setColor(color);
            notificationBuilder.setSmallIcon(R.mipmap.notification_icon);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.notification_icon);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notification.getId() /* ID of notification */, notificationBuilder.build());

        if (notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_USER)) {
            Intent intentBlock = new Intent();
            intentBlock.setAction(Constants.BROAD_CAST_BLOCK_USER);
            sendBroadcast(intentBlock);
        }

        if (!notification.getEvent().equals(Constants.PUSH_TYPE_CHAT)) {
            PreferUtils.setNewPushCount(getApplicationContext(), PreferUtils.getNewPushCount(getApplicationContext()) + 1);

            Intent intentPushCount = new Intent();
            intentPushCount.setAction(Constants.BROAD_CAST_PUSH_COUNT);
            sendBroadcast(intentPushCount);
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
