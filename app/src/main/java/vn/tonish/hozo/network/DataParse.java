package vn.tonish.hozo.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import vn.tonish.hozo.database.entity.UserEntity;

import static vn.tonish.hozo.utils.Utils.getStringInJsonObj;

/**
 * Created by CanTran on 24/04/2017.
 */

public class DataParse {

    public static UserEntity getUserEntiny(Context context, JSONObject object) {
        // save new token
        UserEntity userEntity = new UserEntity();
        try {
            if (!object.equals(null)) {
                JSONObject jData = object.getJSONObject("data");
                JSONObject jToken = jData.getJSONObject("token");
                JSONObject jUser = jData.getJSONObject("user");

                userEntity.setToken(getStringInJsonObj(jToken, "access_token"));
                userEntity.setRefreshToken(getStringInJsonObj(jToken, "refresh_token"));
                userEntity.setTokenExp(getStringInJsonObj(jToken, "token_exp"));

                userEntity.setId(Integer.parseInt(getStringInJsonObj(jUser, "Id")));
                userEntity.setFullName(getStringInJsonObj(jUser, "full_name"));
                userEntity.setPhoneNumber(getStringInJsonObj(jUser, "mobile"));
                userEntity.setLoginAt(getStringInJsonObj(jUser, "login_at"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userEntity;
    }
}
