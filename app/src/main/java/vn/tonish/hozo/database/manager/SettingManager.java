package vn.tonish.hozo.database.manager;

import io.realm.Realm;
import vn.tonish.hozo.database.entity.SettingEntiny;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by CanTran on 5/17/17.
 */

public class SettingManager {
    private static final String TAG = CategoryManager.class.getName();

    public static void insertSetting(SettingEntiny settingEntiny) {
        LogUtils.d(TAG, "insertSetting : " + settingEntiny.toString());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(settingEntiny);
        realm.commitTransaction();
    }

    public static SettingEntiny getSettingEntiny() {
        LogUtils.d(TAG, "getSettingEntiny ");
        Realm realm = Realm.getDefaultInstance();
        // get last update
        SettingEntiny settingEntiny = realm.where(SettingEntiny.class).findFirst();
        if (settingEntiny != null)
            LogUtils.d(TAG, "getSettingEntiny : " + settingEntiny.toString());
        return settingEntiny;
    }

}
