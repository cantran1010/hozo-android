package vn.tonish.hozo.database.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.TaskEntity;
import vn.tonish.hozo.fragment.BrowseTaskFragment;
import vn.tonish.hozo.fragment.MyTaskFragment;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui on 5/17/17.
 */

public class TaskManager {
    private static final String TAG = TaskManager.class.getName();

    public static void insertTask(TaskEntity taskEntity) {
        LogUtils.d(TAG, "insertTasks start , taskEntity : " + taskEntity.toString());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(taskEntity);
        realm.commitTransaction();
        realm.close();
    }

    public static void insertTasks(List<TaskEntity> taskEntities) {
        LogUtils.d(TAG, "insertTasks start , task size : " + taskEntities.size());
        LogUtils.d(TAG, "insertTasks start , tasks: " + taskEntities.toString());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(taskEntities);
        realm.commitTransaction();
        realm.close();
    }

    public static List<TaskEntity> getAllTasks() {
        Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(TaskEntity.class).findAll();
        } finally {
            realm.close();
        }
    }

    public static TaskEntity getTaskById(int taskId) {
        Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(TaskEntity.class).equalTo("id", taskId).findFirst();
        }finally {
            realm.close();
        }
    }


    public static List<TaskEntity> getTaskEntitiesOpen(Date sinceDate, String role) {
        LogUtils.d(TAG, "getTaskEntitiesSince");
        List<TaskEntity> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<TaskEntity> taskEntityRealmQuery;
        if (sinceDate == null) {
            taskEntityRealmQuery = realm.where(TaskEntity.class).equalTo("role", role).equalTo("status", Constants.TASK_STATUS_OPEN);
        } else {
            taskEntityRealmQuery = realm.where(TaskEntity.class).equalTo("role", role).equalTo("status", Constants.TASK_STATUS_OPEN).lessThan("createdAt", sinceDate);
        }
        List<TaskEntity> taskEntities = taskEntityRealmQuery.findAll().sort("createdAt", Sort.DESCENDING);
        if (taskEntities.size() > 0) {

            if (taskEntities.size() >= BrowseTaskFragment.limit)
                result = taskEntities.subList(0, BrowseTaskFragment.limit);
            else result = taskEntities;

        }
        LogUtils.d(TAG, "result" + result.toString() + " getTaskEntitiesOpen size" + result.size());
        LogUtils.d(TAG, " getTaskEntitiesOpen size" + result.size());
        realm.close();
        return result;
    }


    public static List<TaskEntity> getTaskSince(Date sinceDate, String role) {
        LogUtils.d(TAG, "getTaskSince start , sinceDate : " + sinceDate + " , role : " + role);
        List<TaskEntity> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();

        RealmQuery<TaskEntity> taskEntityRealmQuery;
        if (sinceDate == null) {
            taskEntityRealmQuery = realm.where(TaskEntity.class).equalTo("role", role);
        } else {
            taskEntityRealmQuery = realm.where(TaskEntity.class).equalTo("role", role).lessThan("createdAt", sinceDate);
        }

        List<TaskEntity> taskEntities = taskEntityRealmQuery.findAll().sort("createdAt", Sort.DESCENDING);

        if (taskEntities.size() > 0) {

            if (taskEntities.size() >= MyTaskFragment.LIMIT)
                result = taskEntities.subList(0, MyTaskFragment.LIMIT);
            else result = taskEntities;

        }

        LogUtils.d(TAG, "getTaskSince " + result.toString());
        LogUtils.d(TAG, "getTaskSince taskEntities size : " + result.size());
        realm.close();
        return result;
    }

    public static void deleteAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(TaskEntity.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }


}
