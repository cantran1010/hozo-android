package vn.tonish.hozo.database.manager;

import java.util.List;

import io.realm.Realm;
import vn.tonish.hozo.database.entity.StatusEntity;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by Cantran.
 */
public class StatusManager {

    private static final String TAG = StatusManager.class.getName();

    public static void insertStatusEntity(StatusEntity statusEntity) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(statusEntity);
        realm.commitTransaction();
        realm.close();
    }

    public static void insertStatusEntities(List<StatusEntity> statusEntities) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(statusEntities);
        realm.commitTransaction();
        realm.close();
    }

    public static List<StatusEntity> getStatuswithRole(String role) {
        LogUtils.d(TAG, "inserStatusEntity start ");
        Realm realm = Realm.getDefaultInstance();
        return realm.where(StatusEntity.class).equalTo("role", role).findAll();
    }

    public static void insertIsSelectedStatus(StatusEntity statusEntity, boolean selected) {
        LogUtils.d(TAG, "insertIsSelectedStatus start ");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        statusEntity.setSelected(selected);
        realm.commitTransaction();
        realm.close();
    }


}