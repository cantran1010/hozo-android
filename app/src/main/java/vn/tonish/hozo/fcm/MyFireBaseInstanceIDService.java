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

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.LogUtils;


public class MyFireBaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFireBaseInstanceIDService.class.getSimpleName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p/>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        if (UserManager.checkLogin()) {
            final JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put(Constants.UPDATE_TOKEN_DEVICE_TOKEN, token);
                jsonRequest.put(Constants.UPDATE_TOKEN_DEVICE_TYPE, Constants.UPDATE_TOKEN_DEVICE_TYPE_ANDROID);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            LogUtils.d(TAG, "sendRegistrationToServer , jsonRequest : " + jsonRequest.toString());
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
            ApiClient.getApiService().updateDeviceToken(UserManager.getUserToken(), body).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    LogUtils.d(TAG, "sendRegistrationToServer , body : " + response.body());
                    LogUtils.d(TAG, "sendRegistrationToServer , code : " + response.code());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    LogUtils.e(TAG, "sendRegistrationToServer , onFailure : " + t.getMessage());
                }
            });
        }
    }

}
