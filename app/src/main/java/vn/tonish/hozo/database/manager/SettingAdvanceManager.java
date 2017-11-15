package vn.tonish.hozo.database.manager;

import io.realm.Realm;
import io.realm.RealmList;
import vn.tonish.hozo.database.entity.RealmDouble;
import vn.tonish.hozo.database.entity.RealmInt;
import vn.tonish.hozo.database.entity.RealmString;
import vn.tonish.hozo.database.entity.SettingAdvanceEntity;
import vn.tonish.hozo.model.SettingAdvance;
import vn.tonish.hozo.utils.LogUtils;

import static vn.tonish.hozo.common.DataParse.convertRealmToListDouble;
import static vn.tonish.hozo.common.DataParse.convertRealmToListInt;
import static vn.tonish.hozo.common.DataParse.convertRealmToListString;

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
        SettingAdvanceEntity.setNtaNotification(settingAdvance.isNtaNotification());
        SettingAdvanceEntity.setStatus(settingAdvance.getStatus());
        SettingAdvanceEntity.setNtaFollowed(settingAdvance.isNtaFollowed());
        // set category
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

        // set nta_category

        realmInts = new RealmList<>();

        if (settingAdvance.getNtaCategory() != null && settingAdvance.getNtaCategory().size() > 0) {
            for (int integer : settingAdvance.getNtaCategory()
                    ) {
                RealmInt anInt = new RealmInt();
                anInt.setVal(integer);
                realmInts.add(anInt);
                LogUtils.d(TAG, "add nta categoly" + integer);

            }
        }
        SettingAdvanceEntity.setNtaCategory(realmInts);

        // set days

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

        // set nta_days

        realmInts = new RealmList<>();
        if (settingAdvance.getNtaDays() != null && settingAdvance.getNtaDays().size() > 0) {
            for (int integer : settingAdvance.getNtaDays()
                    ) {
                RealmInt anInt = new RealmInt();
                anInt.setVal(integer);
                realmInts.add(anInt);

            }
        }
        SettingAdvanceEntity.setNtaDays(realmInts);

        // set distance
        SettingAdvanceEntity.setDistance(settingAdvance.getDistance());

        // set nta_distance
        SettingAdvanceEntity.setNtaDistance(settingAdvance.getNtaDistance());

        // set location
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

        // set nta_location
        realmDoubles = new RealmList<>();
        if (settingAdvance.getNtaLatlon() != null && settingAdvance.getNtaLatlon().size() > 0) {
            for (Double aDouble : settingAdvance.getNtaLatlon()
                    ) {
                RealmDouble realmDouble = new RealmDouble();
                realmDouble.setVal(aDouble);
                realmDoubles.add(realmDouble);
            }
        }
        SettingAdvanceEntity.setNtaLatlon(realmDoubles);
        // set max min
        SettingAdvanceEntity.setMinWorkerRate(settingAdvance.getMinWorkerRate());
        SettingAdvanceEntity.setMaxWorkerRate(settingAdvance.getMaxWorkerRate());
        // set nta max min
        SettingAdvanceEntity.setNtaMinWorkerRate(settingAdvance.getNtaMinWorkerRate());
        SettingAdvanceEntity.setNtaMaxWorkerRate(settingAdvance.getNtaMaxWorkerRate());
        // set address
        SettingAdvanceEntity.setAddress(settingAdvance.getAddress());
        // set nta_address
        SettingAdvanceEntity.setNtaAddress(settingAdvance.getNtaAddress());
        // set keyword
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

        // set nta_keyword
        realmStrings = new RealmList<>();
        if (settingAdvance.getNtaKeywords() != null && settingAdvance.getNtaKeywords().size() > 0) {
            for (String s : settingAdvance.getNtaKeywords()
                    ) {
                RealmString realmString = new RealmString();
                realmString.setValue(s);
                realmStrings.add(realmString);
            }
        }
        SettingAdvanceEntity.setNtaKeywords(realmStrings);

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

    public static  SettingAdvance converToSettingAdvance(SettingAdvanceEntity settingAdvanceEntity) {
        SettingAdvance settingAdvance = new SettingAdvance();
        if (settingAdvanceEntity != null) {
            settingAdvance.setUserId(settingAdvanceEntity.getUserId());
            settingAdvance.setNotification(settingAdvanceEntity.isNotification());
            settingAdvance.setNtaNotification(settingAdvanceEntity.isNtaNotification());
            settingAdvance.setStatus(settingAdvanceEntity.getStatus());
            settingAdvance.setNtaFollowed(settingAdvanceEntity.isNtaFollowed());
            settingAdvance.setCategories(convertRealmToListInt(settingAdvanceEntity.getCategories()));
            settingAdvance.setNtaCategory(convertRealmToListInt(settingAdvanceEntity.getNtaCategory()));
            settingAdvance.setDays(convertRealmToListInt(settingAdvanceEntity.getDays()));
            settingAdvance.setNtaDays(convertRealmToListInt(settingAdvanceEntity.getNtaDays()));
            settingAdvance.setDistance(settingAdvanceEntity.getDistance());
            settingAdvance.setNtaDistance(settingAdvanceEntity.getNtaDistance());
            settingAdvance.setLatlon(convertRealmToListDouble(settingAdvanceEntity.getLatlon()));
            settingAdvance.setNtaLatlon(convertRealmToListDouble(settingAdvanceEntity.getNtaLatlon()));
            settingAdvance.setMinWorkerRate(settingAdvanceEntity.getMinWorkerRate());
            settingAdvance.setNtaMinWorkerRate(settingAdvanceEntity.getNtaMinWorkerRate());
            settingAdvance.setMaxWorkerRate(settingAdvanceEntity.getMaxWorkerRate());
            settingAdvance.setNtaMaxWorkerRate(settingAdvanceEntity.getNtaMaxWorkerRate());
            settingAdvance.setKeywords(convertRealmToListString(settingAdvanceEntity.getKeywords()));
            settingAdvance.setNtaKeywords(convertRealmToListString(settingAdvanceEntity.getNtaKeywords()));

        }

        return settingAdvance;
    }


}
