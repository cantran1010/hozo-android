package vn.tonish.hozo.database.manager;

import io.realm.Realm;
import vn.tonish.hozo.database.entity.SettingAdvanceEntity;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by CanTran on 5/17/17.
 */

public class SettingAdvanceManager {
    private static final String TAG = CategoryManager.class.getName();

    public static void insertSettingAdvace(SettingAdvanceEntity settingEntiny) {
        LogUtils.d(TAG, "insertSettingAdvance : " + settingEntiny.toString());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(settingEntiny);
        realm.commitTransaction();
    }

    public static SettingAdvanceEntity getSettingAdvace() {
        LogUtils.d(TAG, "getSettingAdvance ");
        Realm realm = Realm.getDefaultInstance();
        // get last update
        SettingAdvanceEntity settingEntiny = realm.where(SettingAdvanceEntity.class).findFirst();
        if (settingEntiny != null)
            LogUtils.d(TAG, "getSettingAdvance : " + settingEntiny.toString());
        return settingEntiny;
    }

}
