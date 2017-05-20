package vn.tonish.hozo.database.manager;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import vn.tonish.hozo.database.entity.TaskEntity;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui on 5/17/17.
 */

public class TaskManager {
    private static final String TAG = TaskManager.class.getName();

    public static void insertTask(TaskEntity taskEntity) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(taskEntity);
        realm.commitTransaction();
    }

    public static void insertTasks(List<TaskEntity> taskEntities) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (int i = 0; i < taskEntities.size(); i++) {
            realm.insertOrUpdate(taskEntities.get(i));
        }
        realm.commitTransaction();
    }

    public static List<TaskEntity> getAllTasks() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(TaskEntity.class).findAll();
    }

    public static TaskEntity getTaskById(Context context,int taskId) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(TaskEntity.class).equalTo("id", taskId).findFirst();
    }
    public static List<TaskEntity> getFirstPage() {
        LogUtils.d(TAG, "getFirstPage ");
        List<TaskEntity> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        List<TaskEntity> taskEntities = realm.where(TaskEntity.class).findAll().sort("createdAt");
        if (taskEntities.size() > 0) {

            if (taskEntities.size() >= ListTaskFragment.limit)
                result = taskEntities.subList(0, ListTaskFragment.limit);
            else
                result = taskEntities;

        }
        return result;
    }

    public static List<TaskEntity> getTaskEntitiesSince(Date sinceDate) {
        LogUtils.d(TAG, "getTaskEntitiesSince");
        List<TaskEntity> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        List<TaskEntity> taskEntities = realm.where(TaskEntity.class).lessThan("createdAt", sinceDate).findAll().sort("createdAt");
        if (taskEntities.size() > 0) {

            if (taskEntities.size() >= ListTaskFragment.limit)
                result = taskEntities.subList(0, ListTaskFragment.limit);
            else result = taskEntities;

        }
        return result;
    }
    public static void deleteAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(TaskEntity.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }
}
