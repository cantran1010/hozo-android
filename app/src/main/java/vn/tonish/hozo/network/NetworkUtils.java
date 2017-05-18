package vn.tonish.hozo.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.Token;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui on 5/9/2017.
 */
public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getName();

    // callback refresh listener
    public interface RefreshListener {
        void onRefreshFinish();
    }

    //    //refresh token
    public static void RefreshToken(final Context context, final RefreshListener refreshListener) {

        JSONObject jsonRequest = new JSONObject();
        try {
//            jsonRequest.put("refresh_token", UserManager.getUserLogin(context).getRefreshToken());
            jsonRequest.put("refresh_token", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().refreshToken(UserManager.getUserToken(), body).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.code() == 200) {
                    //retry call api after refresh success
                    if (refreshListener != null) refreshListener.onRefreshFinish();

                    Token token = response.body();

                    LogUtils.d(TAG, "refreshToken token : " + token.toString());
                    //update new token to database - user table
                    UserManager.getUserLogin().setAccessToken(token.getAccessToken());
                    UserManager.getUserLogin().setRefreshToken(token.getRefreshToken());
                    UserManager.getUserLogin().setTokenExp(token.getTokenExpires());

                } else {
                    //log out
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                // show error dialog
            }
        });

    }

}
