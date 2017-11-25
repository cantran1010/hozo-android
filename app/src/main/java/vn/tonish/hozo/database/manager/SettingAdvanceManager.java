package vn.tonish.hozo.database.manager;

import io.realm.Realm;
import vn.tonish.hozo.common.DataParse;
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

    public static void insertSettingAdvanceEntity(SettingAdvanceEntity settingEntiny) {
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
            LogUtils.d(TAG, "getSettingAdvance realm: " + settingEntiny.toString());
        return settingEntiny;
    }

    public static void insertOrderBy(SettingAdvanceEntity settingAdvanceEntity, String orderBy, String order) {
        LogUtils.d(TAG, "insertOrderBy start ");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        settingAdvanceEntity.setOrderBy(orderBy);
        settingAdvanceEntity.setOrder(order);
        realm.commitTransaction();
        LogUtils.d(TAG, "insertOrderBy start " + settingAdvanceEntity.toString());
    }

    public static SettingAdvance converToSettingAdvance(SettingAdvanceEntity settingAdvanceEntity) {
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
//            settingAdvance.setOrderBy(settingAdvanceEntity.getOrderBy());
//            settingAdvance.setOrder(settingAdvanceEntity.getOrder());

        }
        return settingAdvance;
    }


    public static SettingAdvanceEntity converToSettingAdvanceEntity(SettingAdvance settingAdvance) {
        SettingAdvanceEntity settingAdvanceEntity = new SettingAdvanceEntity();
        settingAdvanceEntity.setUserId(settingAdvance.getUserId());
        settingAdvanceEntity.setNotification(settingAdvance.isNotification());
        settingAdvanceEntity.setNtaNotification(settingAdvance.isNtaNotification());
        settingAdvanceEntity.setStatus(settingAdvance.getStatus());
        settingAdvanceEntity.setNtaFollowed(settingAdvance.isNtaFollowed());
        settingAdvanceEntity.setCategories(DataParse.listIntToRealmList(settingAdvance.getCategories()));
        settingAdvanceEntity.setNtaCategory(DataParse.listIntToRealmList(settingAdvance.getNtaCategory()));
        settingAdvanceEntity.setDays(DataParse.listIntToRealmList(settingAdvance.getDays()));
        settingAdvanceEntity.setNtaDays(DataParse.listIntToRealmList(settingAdvance.getNtaDays()));
        settingAdvanceEntity.setDistance(settingAdvance.getDistance());
        settingAdvanceEntity.setNtaDistance(settingAdvance.getNtaDistance());
        settingAdvanceEntity.setLatlon(DataParse.listDoubleToRealmList(settingAdvance.getLatlon()));
        settingAdvanceEntity.setNtaLatlon(DataParse.listDoubleToRealmList(settingAdvance.getNtaLatlon()));
        // set max min
        settingAdvanceEntity.setMinWorkerRate(settingAdvance.getMinWorkerRate());
        settingAdvanceEntity.setMaxWorkerRate(settingAdvance.getMaxWorkerRate());
        // set nta max min
        settingAdvanceEntity.setNtaMinWorkerRate(settingAdvance.getNtaMinWorkerRate());
        settingAdvanceEntity.setNtaMaxWorkerRate(settingAdvance.getNtaMaxWorkerRate());
        // set address
        settingAdvanceEntity.setAddress(settingAdvance.getAddress());
        // set nta_address
        settingAdvanceEntity.setNtaAddress(settingAdvance.getNtaAddress());
        settingAdvanceEntity.setKeywords(DataParse.listStringToRealmList(settingAdvance.getKeywords()));
        settingAdvanceEntity.setNtaKeywords(DataParse.listStringToRealmList(settingAdvance.getNtaKeywords()));
        return settingAdvanceEntity;
    }


}
