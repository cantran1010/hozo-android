package vn.tonish.hozo.database.manager;

import java.util.List;

import io.realm.Realm;
import vn.tonish.hozo.database.entity.TaskEntity;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui on 5/17/17.
 */

@SuppressWarnings("TryFinallyCanBeTryWithResources")
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


    public static TaskEntity getTaskById(int taskId) {
        Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(TaskEntity.class).equalTo("id", taskId).findFirst();
        } finally {
            realm.close();
        }
    }


}
