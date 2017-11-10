package vn.tonish.hozo.database.manager;

import io.realm.Realm;
import io.realm.RealmList;
import vn.tonish.hozo.database.entity.RealmDouble;
import vn.tonish.hozo.database.entity.RealmInt;
import vn.tonish.hozo.database.entity.RealmString;
import vn.tonish.hozo.database.entity.SettingAdvanceEntity;
import vn.tonish.hozo.model.SettingAdvance;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by CanTran on 5/17/17.
 */

public class SettingAdvanceManager {
    private static final String TAG = CategoryManager.class.getName();

    private static void insertSettingAdvanceEntity(SettingAdvanceEntity settingEntiny) {
        LogUtils.d(TAG, "insertSettingAdvance : " + settingEntiny.toString());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(settingEntiny);
        realm.commitTransaction();
    }

    public static void insertSettingAdvance(SettingAdvance settingAdvance) {
        SettingAdvanceEntity SettingAdvanceEntity = new SettingAdvanceEntity();
        SettingAdvanceEntity.setUserId(settingAdvance.getUserId());
        SettingAdvanceEntity.setNotification(settingAdvance.isNotification());
        SettingAdvanceEntity.setStatus(settingAdvance.getStatus());
        RealmList<RealmInt> realmInts = new RealmList<>();
        if (settingAdvance.getCategories() != null && settingAdvance.getCategories().size() > 0) {
            for (int integer : settingAdvance.getCategories()
                    ) {
                RealmInt anInt = new RealmInt();
                anInt.setVal(integer);
                realmInts.add(anInt);
                LogUtils.d(TAG, "add categoly" + integer);

            }
        }
        SettingAdvanceEntity.setCategories(realmInts);
        realmInts = new RealmList<>();
        if (settingAdvance.getDays() != null && settingAdvance.getDays().size() > 0) {
            for (int integer : settingAdvance.getDays()
                    ) {
                RealmInt anInt = new RealmInt();
                anInt.setVal(integer);
                realmInts.add(anInt);

            }
        }
        SettingAdvanceEntity.setDays(realmInts);

        SettingAdvanceEntity.setDistance(settingAdvance.getDistance());

        RealmList<RealmDouble> realmDoubles = new RealmList<>();
        if (settingAdvance.getLatlon() != null && settingAdvance.getLatlon().size() > 0) {
            for (Double aDouble : settingAdvance.getLatlon()
                    ) {
                RealmDouble realmDouble = new RealmDouble();
                realmDouble.setVal(aDouble);
                realmDoubles.add(realmDouble);
            }
        }
        SettingAdvanceEntity.setLatlon(realmDoubles);

        SettingAdvanceEntity.setMinWorkerRate(settingAdvance.getMinWorkerRate());
        SettingAdvanceEntity.setMaxWorkerRate(settingAdvance.getMaxWorkerRate());
        SettingAdvanceEntity.setAddress(settingAdvance.getAddress());

        RealmList<RealmString> realmStrings = new RealmList<>();
        if (settingAdvance.getKeywords() != null && settingAdvance.getKeywords().size() > 0) {
            for (String s : settingAdvance.getKeywords()
                    ) {
                RealmString realmString = new RealmString();
                realmString.setValue(s);
                realmStrings.add(realmString);
            }
        }
        SettingAdvanceEntity.setKeywords(realmStrings);
        insertSettingAdvanceEntity(SettingAdvanceEntity);
        LogUtils.d(TAG, "addSettingAdvance realm: " + SettingAdvanceEntity.toString());
    }


    public static SettingAdvanceEntity getSettingAdvace() {
        LogUtils.d(TAG, "getSettingAdvance ");
        Realm realm = Realm.getDefaultInstance();
        // get last update
        SettingAdvanceEntity settingEntiny = realm.where(SettingAdvanceEntity.class).findFirst();
        if (settingEntiny != null)
            LogUtils.d(TAG, "getSettingAdvance realm: " + settingEntiny.toString());
        return settingEntiny;
    }

}
