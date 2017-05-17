package vn.tonish.hozo.database.manager;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import vn.tonish.hozo.database.entity.SettingEntiny;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by CanTran on 5/17/17.
 */

public class SettingManager {
    private static final String TAG = CategoryManager.class.getName();
    public static Context context;


    public static void insertCategories(Context context, List<SettingEntiny> settingEntinies) {
        LogUtils.d(TAG, "insertSettingEntinies start ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        realm.beginTransaction();

        for (int i = 0; i < settingEntinies.size(); i++) {
            realm.insertOrUpdate(settingEntinies.get(i));
        }

        realm.commitTransaction();
    }

    public static List<SettingEntiny> getAllCategories(Context context) {
        LogUtils.d(TAG, "getAllSettingEntiniesstart ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        return realm.where(SettingEntiny.class).findAll();
    }

    public static void deleteAll(Context context) {
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        realm.beginTransaction();
        realm.where(SettingEntiny.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }
}
