package vn.tonish.hozo.database.manager;

import io.realm.Realm;
import vn.tonish.hozo.database.entity.PostTaskEntity;

/**
 * Created by CanTran on 10/26/17.
 */

@SuppressWarnings("TryFinallyCanBeTryWithResources")
public class PostTaskManager {

    public static void insertPostTask(PostTaskEntity postTaskEntity) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(postTaskEntity);
        realm.commitTransaction();
        realm.close();
    }

    public static PostTaskEntity getPostTaskEntity() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(PostTaskEntity.class).findFirst();
    }


}
