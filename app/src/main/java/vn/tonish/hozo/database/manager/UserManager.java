package vn.tonish.hozo.database.manager;

import io.realm.Realm;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui.
 */
public class UserManager {

    private static final String TAG = UserManager.class.getName();

    public static boolean checkLogin() {
        Realm realm = Realm.getDefaultInstance();
        // get last update
        UserEntity userEntity = realm.where(UserEntity.class).findFirst();

        return !(userEntity == null || userEntity.getFullName().equals("") || userEntity.getFullName() == null);
    }

    public static UserEntity getUserLogin() {
        LogUtils.d(TAG, "getUserLogin start ");
        Realm realm = Realm.getDefaultInstance();
        // get last update
        UserEntity userEntity = realm.where(UserEntity.class).findFirst();
        if (userEntity != null) LogUtils.d(TAG, "getUserLogin : " + userEntity.toString());
        return userEntity;
    }

    public static String getUserToken() {
        String result = "";
        LogUtils.d(TAG, "getUserLogin start ");
        Realm realm = Realm.getDefaultInstance();
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

    public static void insertUserLogin(UserEntity userEntity) {
        LogUtils.d(TAG, "insertUserLogin : " + userEntity.toString());
        deleteAll();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(userEntity);
        realm.commitTransaction();
    }

    public static void deleteAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(UserEntity.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

}