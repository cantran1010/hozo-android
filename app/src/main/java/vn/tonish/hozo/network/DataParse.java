package vn.tonish.hozo.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import vn.tonish.hozo.database.entity.UserEntity;

import static vn.tonish.hozo.common.Constants.ACCESS_TOCKEN;
import static vn.tonish.hozo.common.Constants.DATA;
import static vn.tonish.hozo.common.Constants.EXP_TOCKEN;
import static vn.tonish.hozo.common.Constants.USER_FULL_NAME;
import static vn.tonish.hozo.common.Constants.USER_LOGIN_AT;
import static vn.tonish.hozo.common.Constants.USER_MOBILE;
import static vn.tonish.hozo.common.Constants.REFRESH_TOCKEN;
import static vn.tonish.hozo.common.Constants.TOCKEN;
import static vn.tonish.hozo.common.Constants.USER;
import static vn.tonish.hozo.common.Constants.USER_ID;
import static vn.tonish.hozo.utils.Utils.getStringInJsonObj;

/**
 * Created by Can Tran on 24/04/2017.
 */

public class DataParse {

    public static UserEntity getUserEntiny(Context context, JSONObject object) {
        // save new token
        UserEntity userEntity = new UserEntity();
        try {
            if (!(object == null)) {
                JSONObject jData = object.getJSONObject(DATA);
                JSONObject jToken = jData.getJSONObject(TOCKEN);
                JSONObject jUser = jData.getJSONObject(USER);

                userEntity.setToken(getStringInJsonObj(jToken, ACCESS_TOCKEN));
                userEntity.setRefreshToken(getStringInJsonObj(jToken, REFRESH_TOCKEN));
                userEntity.setTokenExp(getStringInJsonObj(jToken, EXP_TOCKEN));

                userEntity.setId(Integer.parseInt(getStringInJsonObj(jUser, USER_ID)));
                userEntity.setFullName(getStringInJsonObj(jUser, USER_FULL_NAME));
                userEntity.setPhoneNumber(getStringInJsonObj(jUser, USER_MOBILE));
                userEntity.setLoginAt(getStringInJsonObj(jUser, USER_LOGIN_AT));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userEntity;
    }
}
