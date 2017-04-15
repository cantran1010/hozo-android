/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vn.tonish.hozo.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import vn.tonish.hozo.utils.LogUtils;

/**
 * When implement FCM, add project on firebase console, and change google-service.json
 */
public class MyFireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFireBaseMessagingService.class.getName();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        LogUtils.d(TAG,"onMessageReceived start");

//        LogUtils.d(TAG, "From: " + remoteMessage.getFrom());
//        LogUtils.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//        LogUtils.d(TAG, "Notification Message Data " + remoteMessage.getData().toString());
//        LogUtils.d(TAG, "Notification Message Data data: " + remoteMessage.getData().get("body"));

//        String title = remoteMessage.getNotification().getTitle().toString();
//        String message = remoteMessage.getNotification().getBody().toString();
//        String click_action = remoteMessage.getNotification().getClickAction();
//
//        LogUtils.d("remoteMessage title", title);
//        LogUtils.d("remoteMessage message", message);
//        LogUtils.d("remoteMessage click", click_action);

    }
    // [END receive_message]

//    private void sendNotification(String titleP, String messageP, PushModel pushModel) {
//
//        // vibrator when receive push notification from server
//        Vibrator v = (Vibrator) getApplicationContext()
//                .getSystemService(Context.VIBRATOR_SERVICE);
//        v.vibrate(1000);
//
//        int requestID = (int) System.currentTimeMillis();
//
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra(Constants.EXTRA_PUSH_MODEL, pushModel);
//
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID /* Request code */, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.icon)
//                .setContentTitle(titleP)
//                .setContentText(messageP)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(requestID /* ID of notification */, notificationBuilder.build());
//    }
}
