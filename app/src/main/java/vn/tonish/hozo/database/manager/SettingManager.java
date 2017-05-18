package vn.tonish.hozo.database.manager;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmList;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.entity.SettingEntiny;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by CanTran on 5/17/17.
 */

public class SettingManager {
    private static final String TAG = CategoryManager.class.getName();
    public static void insertSetting(SettingEntiny settingEntiny, Context context) {
        LogUtils.d(TAG, "insertSetting : " + settingEntiny.toString());
        deleteAll(context);
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(settingEntiny);
        realm.commitTransaction();
    }

    public static SettingEntiny getSettingEntiny(Context context) {
        LogUtils.d(TAG, "getSettingEntiny ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        // get last update
        SettingEntiny settingEntiny = realm.where(SettingEntiny.class).findFirst();
        if (settingEntiny != null)
            LogUtils.d(TAG, "getSettingEntiny : " + settingEntiny.toString());
        return settingEntiny;
    }

    public static RealmList<CategoryEntity> getRealmListCategoryEntity(Context context) {
        RealmList<CategoryEntity> categoryEntities=new RealmList<>();
        LogUtils.d(TAG, "getUserLogin start ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        // get last update
        SettingEntiny settingEntiny;
        if (realm.where(SettingEntiny.class) != null) {
            settingEntiny = realm.where(SettingEntiny.class).findFirst();
            categoryEntities=settingEntiny.getCategoryEntities();
        }
        return categoryEntities;

    }


    public static void deleteAll(Context context) {
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        realm.beginTransaction();
        realm.where(SettingEntiny.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }
}
