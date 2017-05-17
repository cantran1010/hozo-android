package vn.tonish.hozo.database.manager;

import android.content.Context;

import io.realm.Realm;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui.
 */
public class UserManager {

    private static final String TAG = UserManager.class.getName();

    public static boolean checkLogin(Context context) {
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        // get last update
        UserEntity userEntity = realm.where(UserEntity.class).findFirst();

        return !(userEntity == null || userEntity.getFullName().equals("") || userEntity.getFullName() == null);
    }

    public static UserEntity getUserLogin(Context context) {
        LogUtils.d(TAG, "getUserLogin start ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        // get last update
        UserEntity userEntity = realm.where(UserEntity.class).findFirst();
        if (userEntity != null) LogUtils.d(TAG, "getUserLogin : " + userEntity.toString());
        return userEntity;
    }

    public static String getUserToken(Context context) {
        String result = "";
        LogUtils.d(TAG, "getUserLogin start ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        // get last update
        UserEntity userEntity;
        if (realm.where(UserEntity.class) != null) {
            userEntity = realm.where(UserEntity.class).findFirst();
            if (userEntity != null) {
                result = userEntity.getAccessToken();
            }
        }
        return result;

    }

    public static void insertUserLogin(UserEntity userEntity, Context context) {
        LogUtils.d(TAG, "insertUserLogin : " + userEntity.toString());
        deleteAll(context);
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(userEntity);
        realm.commitTransaction();
    }

    public static void deleteAll(Context context) {
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        realm.beginTransaction();
        realm.where(UserEntity.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

}