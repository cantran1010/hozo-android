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
        SettingAdvanceEntity SettingAdvanceEntity = new SettingAdvanceEntity();
        SettingAdvanceEntity.setUserId(settingAdvance.getUserId());
        SettingAdvanceEntity.setNotification(settingAdvance.isNotification());
        SettingAdvanceEntity.setNtaNotification(settingAdvance.isNtaNotification());
        SettingAdvanceEntity.setStatus(settingAdvance.getStatus());
        SettingAdvanceEntity.setNtaFollowed(settingAdvance.isNtaFollowed());
        SettingAdvanceEntity.setCategories(DataParse.listIntToRealmList(settingAdvance.getCategories()));
        SettingAdvanceEntity.setNtaCategory(DataParse.listIntToRealmList(settingAdvance.getNtaCategory()));
        SettingAdvanceEntity.setDays(DataParse.listIntToRealmList(settingAdvance.getDays()));
        SettingAdvanceEntity.setNtaDays(DataParse.listIntToRealmList(settingAdvance.getNtaDays()));
        SettingAdvanceEntity.setDistance(settingAdvance.getDistance());
        SettingAdvanceEntity.setNtaDistance(settingAdvance.getNtaDistance());
        SettingAdvanceEntity.setLatlon(DataParse.listDoubleToRealmList(settingAdvance.getLatlon()));
        SettingAdvanceEntity.setNtaLatlon(DataParse.listDoubleToRealmList(settingAdvance.getNtaLatlon()));
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
        SettingAdvanceEntity.setKeywords(DataParse.listStringToRealmList(settingAdvance.getKeywords()));
        SettingAdvanceEntity.setNtaKeywords(DataParse.listStringToRealmList(settingAdvance.getNtaKeywords()));
        return settingAdvanceEntity;
    }


}
